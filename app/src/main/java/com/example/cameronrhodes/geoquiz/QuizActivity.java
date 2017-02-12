package com.example.cameronrhodes.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button cheatButton;
    private Button nextButton;
    private Button scoreButton;
    private Button restartButton;

    private TextView questionTextView;

    private Question[] questionBank = new Question[]{
            new Question(R.string.question_turkey, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int currentIndex = 0;

    protected static final String PREFIX = "com.example.cameronrhodes.geoquiz.";

    protected static final String EXTRA_ANSWER_IS_TRUE  = PREFIX + "answer_is_true";
    protected static final String EXTRA_CORRECT = PREFIX + "num_correct";
    protected static final String EXTRA_INCORRECT = PREFIX + "num_incorrect";
    protected static final String EXTRA_UNANSWERED = PREFIX + "num_unanswered";
    protected static final String EXTRA_CHEATED = PREFIX + "times_cheated";

    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEAT_NUM = "times_cheated";
    private static final String KEY_CORRECT_NUM = "times_correct";
    private static final String KEY_INCORRECT_NUM = "times_incorrect";
    private static final String KEY_WAS_ANSWERED = "was_answered";
    private static final String KEY_NUM_UNANSWERED = "num_unanswered";

    private static final int REQUEST_CODE_CHEAT = 0;

    private boolean isCheater; //necessary?
    private boolean wasQuestionAnswered = false;

    private int timesCheated = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private int unansweredQuestions;

    private void updateQuestion() {
//        if(wasQuestionAnswered)
//            unansweredQuestions--;
        if(currentIndex < questionBank.length) {
            int question = questionBank[currentIndex].getTextResId();
            questionTextView.setText(question);
            wasQuestionAnswered = false;
            if(isCheater)
                cheatButton.setEnabled(true);
            isCheater = false;
        } else {
            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
            nextButton.setEnabled(false);
            cheatButton.setEnabled(false);
            int finishedText = R.string.quiz_done;
            questionTextView.setText(finishedText);

        }
    }

    private void checkAnswer(boolean pressedTrue) {
        boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();
        int messageResId;
        if(pressedTrue == answerIsTrue) {
            messageResId = R.string.response_correct;
            if(!wasQuestionAnswered)
                correctAnswers++;
        } else {
            messageResId = R.string.response_incorrect;
            if(!wasQuestionAnswered)
                incorrectAnswers++;
        }
        if(!wasQuestionAnswered) {
            unansweredQuestions--;
        }
        wasQuestionAnswered = true;
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = (TextView) findViewById(R.id.txtQuestion);

        unansweredQuestions = questionBank.length;

        if(savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            timesCheated = savedInstanceState.getInt(KEY_CHEAT_NUM, 0);
            correctAnswers = savedInstanceState.getInt(KEY_CORRECT_NUM, 0);
            incorrectAnswers = savedInstanceState.getInt(KEY_INCORRECT_NUM, 0);
            unansweredQuestions = savedInstanceState.getInt(KEY_NUM_UNANSWERED, questionBank.length);
            wasQuestionAnswered = savedInstanceState.getBoolean(KEY_WAS_ANSWERED, false);
        }

        updateQuestion();

        trueButton = (Button) findViewById(R.id.btnTrue);
        trueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                String responseText = getString(R.string.response_correct);
//                Toast.makeText(QuizActivity.this, responseText,Toast.LENGTH_SHORT).show();
                checkAnswer(true);
            }
        });

        falseButton = (Button) findViewById(R.id.btnFalse);
        falseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                String responseText = getString(R.string.response_incorrect);
//                Toast.makeText(QuizActivity.this, responseText, Toast.LENGTH_SHORT).show();
                checkAnswer(false);
            }
        });

        nextButton = (Button) findViewById(R.id.btnNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                currentIndex = (currentIndex + 1) % questionBank.length;
                currentIndex++;
                updateQuestion();
            }
        });

        cheatButton = (Button) findViewById(R.id.btnCheat);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start cheat activity
                boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();
                Intent i = newCheatIntent(QuizActivity.this, answerIsTrue);
//                startActivity(i);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        scoreButton = (Button) findViewById(R.id.btnShowScore);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = newScoreIntent(QuizActivity.this, correctAnswers,incorrectAnswers,unansweredQuestions,timesCheated);
                startActivity(i);
            }
        });

        restartButton = (Button) findViewById(R.id.btnRestart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //set everything to base
                currentIndex = 0;

                wasQuestionAnswered = false;
                isCheater = false;

                correctAnswers = 0;
                incorrectAnswers = 0;
                timesCheated = 0;
                unansweredQuestions = questionBank.length;

                trueButton.setEnabled(true);
                falseButton.setEnabled(true);
                cheatButton.setEnabled(true);
                nextButton.setEnabled(true);

                updateQuestion();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT) {
            if(data == null) {
                return;
            }
//            isCheater = CheatActivity.wasAnswerShown(data);
            if(isCheater) //if already cheated for this question
                return;

            isCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
            if(isCheater) {
                timesCheated++;
                cheatButton.setEnabled(false);
            }
        }

    }

    public static Intent newCheatIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static Intent newScoreIntent(Context packageContext, int correct, int incorrect, int unanswered, int cheated) {
        Intent i = new Intent(packageContext, ScoreActivity.class);
        i.putExtra(EXTRA_CORRECT, correct);
        i.putExtra(EXTRA_INCORRECT, incorrect);
        i.putExtra(EXTRA_UNANSWERED, unanswered);
        i.putExtra(EXTRA_CHEATED, cheated);
        return i;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, currentIndex);
        savedInstanceState.putInt(KEY_CORRECT_NUM, correctAnswers);
        savedInstanceState.putInt(KEY_INCORRECT_NUM, incorrectAnswers);
        savedInstanceState.putInt(KEY_CHEAT_NUM, timesCheated);
        savedInstanceState.putInt(KEY_NUM_UNANSWERED, unansweredQuestions);
        savedInstanceState.putBoolean(KEY_WAS_ANSWERED, wasQuestionAnswered);
    }
}
