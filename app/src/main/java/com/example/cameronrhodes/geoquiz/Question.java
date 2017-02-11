package com.example.cameronrhodes.geoquiz;

/**
 * Created by Cameron Rhodes on 2/11/2017.
 */

public class Question {
    private int textResId;
    private boolean answerTrue;

    public Question(int sTextResId, boolean isAnswerTrue) {
        textResId = sTextResId;
        answerTrue = isAnswerTrue;
    }

    public int getTextResId() {
        return textResId;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }
}
