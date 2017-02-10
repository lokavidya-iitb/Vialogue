package com.comp.iitb.vialogue.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.comp.iitb.vialogue.adapters.QuestionAnswerAdapter;
import com.comp.iitb.vialogue.models.QuestionAnswer;

/**
 * Created by shubh on 10-02-2017.
 */

public class SingleOptionQuestion extends QuestionAnswerAdapter {

    private LayoutInflater layoutInflater;
    private QuestionAnswer questionAnswer;


    public SingleOptionQuestion(Context context, QuestionAnswer questionAnswer) {
        super(context);

    }

    @Override
    protected View onCreateQuestionView() {

        return null;
    }

    @Override
    protected View onCreateOptionsView() {
        return null;
    }

    @Override
    protected View onCreateHintsView() {
        return null;
    }

    @Override
    protected View onCreateSolutionView() {
        return null;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return false;
    }

    @Override
    protected void onAnswerIncorrect() {

    }

    @Override
    protected void onAnswerCorrect() {

    }


}
