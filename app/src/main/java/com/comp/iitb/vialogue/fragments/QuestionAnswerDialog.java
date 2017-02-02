package com.comp.iitb.vialogue.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.listeners.ChangeVisibilityClick;

/**
 * Created by shubh on 31-01-2017.
 */

public class QuestionAnswerDialog extends Dialog {

    private static final int MAX_OPTIONS = 4;
    private static final int OPTIONAL_LAYOUT_COUNT = 2;

    private EditText mQuestion;
    private EditText[] mAnswerOptions = new EditText[MAX_OPTIONS];
    private AppCompatRadioButton[] mIsAnswerButtons = new AppCompatRadioButton[MAX_OPTIONS];
    private RelativeLayout[] mOptionalLayout = new RelativeLayout[OPTIONAL_LAYOUT_COUNT];
    private Button[] mRemoveOption = new Button[OPTIONAL_LAYOUT_COUNT];
    private ImageButton mAddOptionButton;
    private AppCompatRadioButton mSelectedAnswer;
    private Button mDoneButton;

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
        setContentView(R.layout.question_answer);

        mQuestion = (EditText) findViewById(R.id.question_text);
        mAddOptionButton = (ImageButton) findViewById(R.id.add_option);

        mAnswerOptions[0] = (EditText) findViewById(R.id.answer_option_0);
        mAnswerOptions[1] = (EditText) findViewById(R.id.answer_option_1);
        mAnswerOptions[2] = (EditText) findViewById(R.id.answer_option_2);
        mAnswerOptions[3] = (EditText) findViewById(R.id.answer_option_3);

        mIsAnswerButtons[0] = (AppCompatRadioButton) findViewById(R.id.is_answer_0);
        mIsAnswerButtons[1] = (AppCompatRadioButton) findViewById(R.id.is_answer_1);
        mIsAnswerButtons[2] = (AppCompatRadioButton) findViewById(R.id.is_answer_2);
        mIsAnswerButtons[3] = (AppCompatRadioButton) findViewById(R.id.is_answer_3);
        setUpRadioButtonsGroup();

        mOptionalLayout[0] = (RelativeLayout) findViewById(R.id.answer_option_view_2);
        mOptionalLayout[1] = (RelativeLayout) findViewById(R.id.answer_option_view_3);

        mRemoveOption[0] = (Button) findViewById(R.id.delete_button_2);
        mRemoveOption[1] = (Button) findViewById(R.id.delete_button_3);
        setUpRemoveButtons();

        setUpAddButton();
        mDoneButton = (Button) findViewById(R.id.done_button);

    }

    private void setUpAddButton() {
        mAddOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < OPTIONAL_LAYOUT_COUNT; i++) {
                    if (mOptionalLayout[i].getVisibility() != View.VISIBLE) {
                        mOptionalLayout[i].setVisibility(View.VISIBLE);
                        mAnswerOptions[i+2].setText("");
                        for (int j = i + 3; j < mAnswerOptions.length; j++) {
                            mAnswerOptions[j - 1].setText(mAnswerOptions[j].getText());
                            mAnswerOptions[j].setText("");
                        }
                        break;
                    }
                }
            }
        });
    }

    private void setUpRadioButtonsGroup() {
        for (RadioButton radioButton:mIsAnswerButtons) {
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedAnswer != null) {
                        mSelectedAnswer.setChecked(false);
                    }
                    if (v instanceof AppCompatRadioButton) {
                        mSelectedAnswer = (AppCompatRadioButton) v;
                    }
                }
            });
        }
    }


    private void setUpRemoveButtons() {
        mRemoveOption[0].setOnClickListener(new ChangeVisibilityClick(mOptionalLayout[0], View.GONE));
        mRemoveOption[1].setOnClickListener(new ChangeVisibilityClick(mOptionalLayout[1], View.GONE));
    }
}
