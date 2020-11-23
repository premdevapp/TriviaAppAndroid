package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                questionCounterText.setText((currentQuestionIndex + 1) + " / "+ questionList.size());
                Log.d("Inside", "processFinished: "+ questionArrayList);
            }
        });

        //Log.d("Main", "onCreate: "+ questionList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prevButton:
                if(currentQuestionIndex > 0){
                    currentQuestionIndex = (currentQuestionIndex-1) % questionList.size();
                }else {
                    currentQuestionIndex = questionList.size()-1;
                }
                    updateQuestion();
                break;
            case R.id.nextButton:
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                break;
            case R.id.trueButton:
                checkAnswer(true);
                Log.d("Check", "onClick: True ");
                break;
            case R.id.falseButton:
                checkAnswer(false);
                Log.d("Check", "onClick: False ");
                break;

        }

    }
    private void updateQuestion(){
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterText.setText((currentQuestionIndex + 1) + " / "+ questionList.size());
    }
    private void checkAnswer(boolean userChoice){
        int toastMessage = 0;
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        if(answer == userChoice){
            toastMessage = R.string.correct_answer;
        }else {
            toastMessage = R.string.wrong_answer;
        }
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }
}