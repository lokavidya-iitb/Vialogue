package com.comp.iitb.vialogue.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import com.comp.iitb.vialogue.coordinators.ConditionListener;

/**
 * Created by shubh on 03-02-2017.
 */

public class MinimumConditionOnTextChangeListener implements TextWatcher{
    private ConditionListener mConditionListener;
    private int mTextLengthLimit;
    public MinimumConditionOnTextChangeListener(ConditionListener conditionListener){
        mConditionListener = conditionListener;
        mTextLengthLimit = 0;
    }

    public int getTextLengthLimit() {
        return mTextLengthLimit;
    }

    public void setTextLengthLimit(int mTextLengthLimit) {
        this.mTextLengthLimit = mTextLengthLimit;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length()<=mTextLengthLimit)
            mConditionListener.conditionFailed(this);
        else
            mConditionListener.conditionSatisfied(this);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
