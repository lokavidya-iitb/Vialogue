package com.comp.iitb.vialogue.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.comp.iitb.vialogue.coordinators.ConditionListener;

/**
 * Created by shubh on 03-02-2017.
 */

public class MinimumConditionOnTextChangeListener implements TextWatcher{
    private ConditionListener mConditionListener;
    private int mTextLengthLimit;
    private EditText mEditText;
    public MinimumConditionOnTextChangeListener(ConditionListener conditionListener, EditText editText){
        mConditionListener = conditionListener;
        mTextLengthLimit = 0;
        mEditText = editText;
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
            mConditionListener.conditionFailed(mEditText);
        else
            mConditionListener.conditionSatisfied(mEditText);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
