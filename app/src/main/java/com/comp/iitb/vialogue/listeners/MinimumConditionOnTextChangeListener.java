package com.comp.iitb.vialogue.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.comp.iitb.vialogue.coordinators.ConditionListener;
import com.comp.iitb.vialogue.fragments.SingleChoiceQuestionDialog;

import java.util.ArrayList;

/**
 * Created by shubh on 03-02-2017.
 */

public class MinimumConditionOnTextChangeListener implements TextWatcher{
    private SingleChoiceQuestionConditionListener mConditionListener;
    private int mMinimumTextLength;
    private int mMaximumTextLength;
    private EditText mEditText;
    private boolean mIsConditionSatisfied = false;
    private int mIndex;

    public MinimumConditionOnTextChangeListener(SingleChoiceQuestionConditionListener conditionListener, EditText editText, int index){
        mConditionListener = conditionListener;
        mEditText = editText;
        mMinimumTextLength = 1;
        mMaximumTextLength = 200;
        mIndex = index;
    }

    public int getMinimumTextLength() {
        return mMinimumTextLength;
    }

    public void setMinimumTextLength(int minimumTextLength) {
        this.mMinimumTextLength = minimumTextLength;
    }

    public int getMaximumTextLength() {
        return mMaximumTextLength;
    }

    public void setMaximumTextLength(int maximumTextLength) {
        mMaximumTextLength = maximumTextLength;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(satisfiesMinimumCondition(s.toString(), mMinimumTextLength, mMaximumTextLength)) {
            if(!mIsConditionSatisfied) {
                mIsConditionSatisfied = !mIsConditionSatisfied;
                mConditionListener.conditionSatisfied(mIndex);
            }
        } else {
            if(mIsConditionSatisfied) {
                mIsConditionSatisfied = !mIsConditionSatisfied;
                mConditionListener.conditionFailed(mIndex);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!doesItReallyHaveChars(s)) {
            mEditText.setError("Cannot be Blank");
        }
    }

    public static boolean satisfiesMinimumCondition(String string, int min, int max) {
        int length = string.length();
        return((length >= min) && (length <= max) && (doesItReallyHaveChars(string)));
    }

    public static boolean satisfiesMinimumCondition(EditText editText, int min, int max) {
        return satisfiesMinimumCondition(editText.getText().toString(), min, max);
    }

    public static boolean doesItReallyHaveChars(CharSequence s) {
        return (s.toString().trim().length() != 0);
    }
}
