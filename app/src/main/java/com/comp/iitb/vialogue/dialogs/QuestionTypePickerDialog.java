package com.comp.iitb.vialogue.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.fragments.SingleChoiceQuestionDialog;
import com.comp.iitb.vialogue.listeners.QuestionDoneListener;

public class QuestionTypePickerDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Fragment mFragment;
    private TextView QT1;
    private TextView QT2;
    private TextView QT3;

    public QuestionTypePickerDialog(Context context, Fragment fragment)
    {
        super(context);
        mContext= context;
        mFragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_question_type_picker_dialog);
        // initialise UI components
        QT1 = (TextView)findViewById(R.id.QT1);
        QT2= (TextView)findViewById(R.id.QT2);
        QT3= (TextView)findViewById(R.id.QT3);

        // setting click listners
        QT1.setOnClickListener(this);
        QT1.setOnClickListener(this);
        QT1.setOnClickListener(this);
    }

    // add as many question types as you want.
    @Override
    public void onClick(View v) {
        int ClickedViewId = v.getId();
        switch(ClickedViewId)
        {
            case R.id.QT1:
                new SingleChoiceQuestionDialog(mContext, new QuestionDoneListener(mContext, mFragment)).show();
                break;
            case R.id.QT2:
             new MultiChoiceQuestionDialog(mContext, new QuestionDoneListener(mContext,mFragment)).show();
                break;
            case R.id.QT3:
                break;
            case R.id.QT4:
                break;
        }
        QuestionTypePickerDialog.this.dismiss();

    }
}
