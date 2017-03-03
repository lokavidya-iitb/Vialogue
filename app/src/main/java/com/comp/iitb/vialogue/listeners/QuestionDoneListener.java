package com.comp.iitb.vialogue.listeners;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.comp.iitb.vialogue.activity.QuestionDialogResultActivity;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;

import java.util.ArrayList;

/**
 * Created by ironstein on 03/03/17.
 */

public class QuestionDoneListener {

    Context mContext;
    Fragment mFragment;

    public QuestionDoneListener(Context context, Fragment fragment) {
        mContext = context;
        mFragment = fragment;
    }

    public void onDone(String questionString, ArrayList<String> options, ArrayList<Integer> correctOptions) {
        Intent intent = new Intent(mContext, QuestionDialogResultActivity.class);
        intent.putExtra(Question.Fields.QUESTION_STRING_FIELD, questionString);
        intent.putExtra(Question.Fields.OPTIONS_FIELD, options);
        intent.putExtra(Question.Fields.CORRECT_OPTIONS_FIELD, correctOptions);
        intent.putExtra(Question.Fields.QUESTION_TYPE_FIELD, Question.Type.MCQ);
        intent.putExtra(Question.Fields.SOLUTION_FIELD, "");
        intent.putExtra(Question.Fields.HINTS_FIELD, new ArrayList<String>());
        intent.putExtra(Question.Fields.IS_COMPULSORY_FIELD, true);

        mFragment.startActivityForResult(intent, SharedRuntimeContent.GET_QUESTION);
    }

}
