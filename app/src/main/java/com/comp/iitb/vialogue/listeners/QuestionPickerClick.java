package com.comp.iitb.vialogue.listeners;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.comp.iitb.vialogue.fragments.QuestionAnswerDialog;

/**
 * Created by shubh on 01-02-2017.
 */

public class QuestionPickerClick implements View.OnClickListener {

    Context mContext;
    Fragment mFragment;
    public QuestionPickerClick(Context context, Fragment fragment){
        mContext = context;
        mFragment = fragment;
    }
    @Override
    public void onClick(View v) {
        QuestionAnswerDialog qaDialog = new QuestionAnswerDialog(mContext, new QuestionDoneListener(mContext, mFragment));
        qaDialog.show();
    }
}
