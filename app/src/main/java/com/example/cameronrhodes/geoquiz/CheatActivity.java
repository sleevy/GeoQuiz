package com.example.cameronrhodes.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {


    private TextView answerTextView;
    private Button showAnswer;
    private boolean answerIsTrue;
    protected static final String EXTRA_ANSWER_SHOWN = "com.example.cameronrhodes.geoquiz.answer_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        answerIsTrue = getIntent().getBooleanExtra(QuizActivity.EXTRA_ANSWER_IS_TRUE,false);

        answerTextView = (TextView) findViewById(R.id.txtAnswer);

        showAnswer = (Button) findViewById(R.id.btnShowAnswer);
        showAnswer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(answerIsTrue) {
                    answerTextView.setText(R.string.true_button);
                } else {
                    answerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);
            }
        });

    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
