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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTextView, questionCounterText;
    private Button trueButton, falseButton;
    private ImageButton prevButton, nextButton;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterText.setText((currentQuestionIndex + 1) + " / " + questionList.size());
                Log.d("Inside", "processFinished: " + questionArrayList);
            }
        });

        //Log.d("Main", "onCreate: "+ questionList);

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
            toastMessage = R.string.correct_answer;
        } else {
            shakeAnimation();
            toastMessage = R.string.wrong_answer;
        }
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}