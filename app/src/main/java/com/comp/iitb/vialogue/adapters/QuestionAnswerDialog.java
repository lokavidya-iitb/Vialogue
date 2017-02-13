package com.comp.iitb.vialogue.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.comp.iitb.vialogue.R;

/**
 * Created by shubh on 10-02-2017.
 */

public abstract class QuestionAnswerDialog extends Dialog {

    private RelativeLayout mQuestionLayout;
    private View mQuestionView;
    private RelativeLayout mOptionLayout;
    private View mOptionsView;
    private RelativeLayout mHintLayout;
    private View mHintView;
    private RelativeLayout mSolutionLayout;
    private View mSolutionView;
    private Button mDone;
    private LayoutInflater mLayoutInflater;
    private Button mSkip;
    public QuestionAnswerDialog(Context context) {
        super(context);
    }

    public QuestionAnswerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected QuestionAnswerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.question_answer_layout);
        mLayoutInflater = LayoutInflater.from(getContext());
        setCancelable(false);

        mQuestionLayout = (RelativeLayout) findViewById(R.id.question_layer);
        mQuestionView = onCreateQuestionView();
        if (mQuestionView != null)
            mQuestionLayout.addView(mQuestionView);

        mOptionLayout = (RelativeLayout) findViewById(R.id.option_layer);
        mOptionsView = onCreateOptionsView();
        if (mOptionsView != null)
            mOptionLayout.addView(mOptionsView);

        mHintLayout = (RelativeLayout) findViewById(R.id.hint_layer);
        mHintView = onCreateOptionsView();
        if (mHintView != null)
            mHintLayout.addView(mHintView);

        mSolutionLayout = (RelativeLayout) findViewById(R.id.solution_layer);
        mSolutionView = onCreateSolutionView();
        if (mSolutionView != null)
            mSolutionLayout.addView(mSolutionView);

        mDone = (Button) findViewById(R.id.done_button);

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswerCorrect()) {
                    onAnswerCorrect();
                } else {
                    onAnswerIncorrect();
                }
            }
        });
        
        mSkip = (Button)findViewById(R.id.skip_button);
        if(isSkipEnabled()){
            mSkip.setVisibility(View.VISIBLE);
        } else {
            mSkip.setVisibility(View.GONE);
        }
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSkipPressed();
            }
        });
    }

    protected abstract void onSkipPressed();

    public void showSkip(){ mSkip.setVisibility(View.VISIBLE);}

    public void hideSkip() {mSkip.setVisibility(View.GONE);}

    public void enableSkip() {mSkip.setEnabled(true);}

    public void disableSkip(){mSkip.setEnabled(false);}

    protected Button getDoneButton() {
        return mDone;
    }

    public LayoutInflater getLayoutInflator() {
        return mLayoutInflater;
    }

    public void showHints() {
        mHintLayout.setVisibility(View.VISIBLE);
    }

    public void hideHints() {
        mHintLayout.setVisibility(View.GONE);
    }

    public void showSolution() {
        mSolutionLayout.setVisibility(View.VISIBLE);
    }

    public void hideSolution() {
        mSolutionLayout.setVisibility(View.GONE);
    }

    protected abstract View onCreateQuestionView();

    protected abstract View onCreateOptionsView();

    protected abstract View onCreateHintsView();

    protected abstract View onCreateSolutionView();

    protected abstract boolean isAnswerCorrect();

    protected abstract void onAnswerIncorrect();

    protected abstract void onAnswerCorrect();

    protected abstract boolean isSkipEnabled();
}
