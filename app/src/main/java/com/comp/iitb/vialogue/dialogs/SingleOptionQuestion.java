package com.comp.iitb.vialogue.dialogs;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.QuestionAnswerDialog;
import com.comp.iitb.vialogue.models.QuestionAnswer;

/**
 * Created by shubh on 10-02-2017.
 */

public class SingleOptionQuestion extends QuestionAnswerDialog {

    private LayoutInflater mLayoutInflater;
    private QuestionAnswer mQuestionAnswer;
    private View mQuestionView;
    private View[] mOptionViews;
    private CompoundButton mSelectedAnswer;

    public SingleOptionQuestion(Context context, QuestionAnswer questionAnswer) {
        super(context);
        mLayoutInflater = getLayoutInflater();
        mQuestionAnswer = questionAnswer;
        mOptionViews = new View[questionAnswer.getOptions().length];
    }

    @Override
    protected void onSkipPressed() {
        dismiss();
    }

    @Override
    protected View onCreateQuestionView() {
        mQuestionView = mLayoutInflater.inflate(R.layout.string_view, null);
        if (mQuestionView instanceof TextView) {
            ((TextView) mQuestionView).setText(mQuestionAnswer.getQuestion());
        }
        return mQuestionView;
    }

    @Override
    protected View onCreateOptionsView() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < mQuestionAnswer.getOptions().length; i++) {
            View view = mLayoutInflater.inflate(R.layout.radio_view, null);
            if (view instanceof AppCompatRadioButton) {
                AppCompatRadioButton button = ((AppCompatRadioButton) view);
                button.setText(mQuestionAnswer.getOptions()[i]);
                button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (mSelectedAnswer != null) {
                            mSelectedAnswer.setChecked(false);
                        }
                        mSelectedAnswer = buttonView;
                    }
                });
            }
            linearLayout.addView(view);
        }
        return linearLayout;
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
    public boolean isAnswerCorrect(){
        if(mQuestionAnswer.getAnswers()[0].toString().equals(mSelectedAnswer.getText().toString()))
            return true;
        else
            return false;
    }

    @Override
    protected void onAnswerIncorrect() {
        dismiss();
    }

    @Override
    protected void onAnswerCorrect() {
        dismiss();
    }

    @Override
    protected boolean isSkipEnabled() {
        return !mQuestionAnswer.isCompulsory();
    }


}
