package com.example.cameronrhodes.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ScoreActivity extends AppCompatActivity {

    private TextView numCorrect;
    private TextView numIncorrect;
    private TextView numCheated;
    private TextView numUnanswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent data = getIntent();

        numCorrect = (TextView) findViewById(R.id.txtNumCorrect);
        int correct = data.getIntExtra(QuizActivity.EXTRA_CORRECT, 0);
        numCorrect.setText(Integer.toString(correct));

        numIncorrect = (TextView) findViewById(R.id.txtNumIncorrect);
        int incorrect = data.getIntExtra(QuizActivity.EXTRA_INCORRECT, 0);
        numIncorrect.setText(Integer.toString(incorrect));

        numUnanswered = (TextView) findViewById(R.id.txtNumUnanswered);
        int unanswered = data.getIntExtra(QuizActivity.EXTRA_UNANSWERED, 0);
        numUnanswered.setText(Integer.toString(unanswered));

        numCheated = (TextView) findViewById(R.id.txtTimesCheated);
        int cheated = data.getIntExtra(QuizActivity.EXTRA_CHEATED, 0);
        numCheated.setText(Integer.toString(cheated));
    }
}
