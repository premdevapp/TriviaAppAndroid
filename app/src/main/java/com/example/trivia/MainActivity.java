package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTextView, questionCounterText, scoreTextView, highScore;
    private Button trueButton, falseButton;
    private ImageButton prevButton, nextButton;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    private int scoreCounter = 0;

    private Score score;

    private CardView cardView;

    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //highScore
        //prefs
        prefs = new Prefs(MainActivity.this);
        //Log.d("Prefs", "onClick: "+ prefs.getHighScore());

        highScore = findViewById(R.id.highScore_view);
        highScore.setText(MessageFormat.format("HIGH SCORE :  {0}", prefs.getHighScore()));

        //score board
        scoreTextView = findViewById(R.id.scoreText);
        scoreTextView.setText(MessageFormat.format("SCORE :  {0}", prefs.getCurrentScore()));
        //scoring system
        score = new Score();

        cardView = findViewById(R.id.cardView);

        //textView
        questionTextView = findViewById(R.id.questionView);
        questionCounterText = findViewById(R.id.conter_text);

        //button
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);

        //imageButton
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);


        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        currentQuestionIndex = prefs.getState();

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterText.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex + 1, questionList.size()));
                Log.d("Inside", "processFinished: " + questionArrayList);
            }
        });

       // Log.d("Main", "onCreate: "+ questionList);

    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.savedHighScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        prefs.setCurrentScore(score.getScore());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prevButton:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                } else {
                    currentQuestionIndex = questionList.size() - 1;
                }
                updateQuestion();
                break;
            case R.id.nextButton:
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                break;
            case R.id.trueButton:
                checkAnswer(true);
                updateQuestion();
                //Log.d("Check", "onClick: True ");
                break;
            case R.id.falseButton:
                checkAnswer(false);
                updateQuestion();
                //Log.d("Check", "onClick: False ");
                break;

        }

    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterText.setText((currentQuestionIndex + 1) + " / " + questionList.size());
    }

    private void checkAnswer(boolean userChoice) {
        int toastMessage = 0;
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        if (answer == userChoice) {
            fadeView();
            if(score.getScore()>prefs.getHighScore())
                highScore.setText(MessageFormat.format("NEW HIGH SCORE :  {0}", score.getScore()+100));
            toastMessage = R.string.correct_answer;
            addPoints();
        } else {
            shakeAnimation();
            toastMessage = R.string.wrong_answer;
            deductPoints();
        }
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    private void addPoints(){
        scoreCounter += 100;
        score.setScore(scoreCounter);
       // scoreTextView.setText(Integer.toString(score.getScore()));
        scoreTextView.setText(MessageFormat.format("SCORE :  {0}", String.valueOf(score.getScore())));
        Log.d("score", "addPoints: " + score.getScore());
    }
    private void deductPoints(){
        if(scoreCounter>0) {
            scoreCounter -= 100;
            score.setScore(scoreCounter);
        }else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
        }
        scoreTextView.setText(MessageFormat.format("SCORE :  {0}", String.valueOf(score.getScore())));
        Log.d("score", "deductPoints: " + score.getScore());
    }

    private void fadeView(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);

        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}