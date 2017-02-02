package com.comp.iitb.vialogue.listeners;

import android.content.Context;
import android.view.View;

import com.comp.iitb.vialogue.fragments.QuestionAnswerDialog;

/**
 * Created by shubh on 01-02-2017.
 */

public class QuestionPickerClick implements View.OnClickListener {

    Context mContext;
    public QuestionPickerClick(Context context){
        mContext = context;
    }
    @Override
    public void onClick(View v) {
        QuestionAnswerDialog qaDialog = new QuestionAnswerDialog(mContext);
        qaDialog.show();
    }
}
