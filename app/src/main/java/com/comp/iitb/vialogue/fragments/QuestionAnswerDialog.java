package com.comp.iitb.vialogue.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.ConditionListener;
import com.comp.iitb.vialogue.listeners.ChangeVisibilityClick;
import com.comp.iitb.vialogue.listeners.MinimumConditionOnTextChangeListener;
import com.comp.iitb.vialogue.listeners.QuestionDoneListener;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.parse.ParseFile;

import java.util.ArrayList;

/**
 * Created by shubh on 31-01-2017.
 */

public class QuestionAnswerDialog extends Dialog implements ConditionListener {

    private static final int MAX_OPTIONS = 4;
    private static final int OPTIONAL_LAYOUT_COUNT = 2;

    private EditText mQuestion;
    private EditText[] mAnswerOptions = new EditText[MAX_OPTIONS];
    private AppCompatRadioButton[] mIsAnswerButtons = new AppCompatRadioButton[MAX_OPTIONS];
    private RelativeLayout[] mOptionalLayout = new RelativeLayout[OPTIONAL_LAYOUT_COUNT];
    private Button[] mRemoveOption = new Button[OPTIONAL_LAYOUT_COUNT];
    private ImageButton mAddOptionButton;
    private CompoundButton mSelectedAnswer;
    private static Button mDoneButton;
    private Context mContext;
    private Question mQuestionObject = null;
    private int mSlideNumber = -1;

    private QuestionDoneListener mQuestionDoneListener;

    public QuestionAnswerDialog(Context context, QuestionDoneListener questionDoneListener) {
        super(context);
        mQuestionDoneListener = questionDoneListener;
        mContext = context;
    }

    public QuestionAnswerDialog(Context context, QuestionDoneListener questionDoneListener, Question question, int slideNumber) {
        super(context);
        mQuestionDoneListener = questionDoneListener;
        mQuestionObject = question;
        mSlideNumber = slideNumber;
    }

    public QuestionAnswerDialog(Context context, int themeResId, QuestionDoneListener questionDoneListener) {
        super(context, themeResId);
        mQuestionDoneListener = questionDoneListener;
        mContext = context;
    }

    protected QuestionAnswerDialog(Context context, boolean cancelable, OnCancelListener cancelListener, QuestionDoneListener questionDoneListener) {
        super(context, cancelable, cancelListener);
        mQuestionDoneListener = questionDoneListener;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.question_answer);

        // initialization
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
        mOptionalLayout[0] = (RelativeLayout) findViewById(R.id.answer_option_view_2);
        mOptionalLayout[1] = (RelativeLayout) findViewById(R.id.answer_option_view_3);
        mRemoveOption[0] = (Button) findViewById(R.id.delete_button_2);
        mRemoveOption[1] = (Button) findViewById(R.id.delete_button_3);
        mDoneButton = (Button) findViewById(R.id.done_button);

        // load state
        if(mQuestionObject != null) {
            loadFromQuestionObject(mQuestionObject);
        }

        // add listeners
        mQuestion.addTextChangedListener(new MinimumConditionOnTextChangeListener(this, mQuestion));
        mAnswerOptions[0].addTextChangedListener(new MinimumConditionOnTextChangeListener(this, mAnswerOptions[0]));
        mAnswerOptions[1].addTextChangedListener(new MinimumConditionOnTextChangeListener(this, mAnswerOptions[1]));
        mAnswerOptions[2].addTextChangedListener(new MinimumConditionOnTextChangeListener(this, mAnswerOptions[2]));
        mAnswerOptions[3].addTextChangedListener(new MinimumConditionOnTextChangeListener(this, mAnswerOptions[3]));
        setUpRadioButtonsGroup();
        mIsAnswerButtons[0].setChecked(true);
        setUpRemoveButtons();
        setUpAddButton();
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

    }

    private void setUpAddButton() {

        mAddOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOption();
            }
        });
    }

    public void addOption() {
        for (int i = 0; i < OPTIONAL_LAYOUT_COUNT; i++) {
            if (mOptionalLayout[i].getVisibility() != View.VISIBLE) {
                mOptionalLayout[i].setVisibility(View.VISIBLE);
                mAnswerOptions[i + 2].setText("");
                for (int j = i + 3; j < mAnswerOptions.length; j++) {
                    mAnswerOptions[j - 1].setText(mAnswerOptions[j].getText());
                    mAnswerOptions[j].setText("");
                }
                break;
            }
        }
        mDoneButton.setEnabled(true);
    }

    public void addOptionWithText(String text) {
        for (int i = 0; i < OPTIONAL_LAYOUT_COUNT; i++) {
            if (mOptionalLayout[i].getVisibility() != View.VISIBLE) {
                mOptionalLayout[i].setVisibility(View.VISIBLE);
                mAnswerOptions[i + 2].setText(text);
                for (int j = i + 3; j < mAnswerOptions.length; j++) {
                    mAnswerOptions[j - 1].setText(mAnswerOptions[j].getText());
                    mAnswerOptions[j].setText("");
                }
                break;
            }
        }
    }

    public void done() {
        ArrayList<String> options = new ArrayList<String>();
        for(int i=0; i<mAnswerOptions.length; i++) {
            String text = mAnswerOptions[i].getText().toString();
            options.add(text);
        }
        System.out.println("options : " + options);
        ArrayList<Integer> correctOptions = new ArrayList<Integer>();
        for(int i=0; i<mIsAnswerButtons.length; i++) {
            if(mIsAnswerButtons[i].isChecked()) {
                correctOptions.add(i);
            }
        }
        mQuestionDoneListener.onDone(
                mQuestion.getText().toString(),
                options,
                correctOptions,
                mSlideNumber
        );
        dismiss();
    }


    private void setUpRadioButtonsGroup() {
        for (RadioButton radioButton : mIsAnswerButtons) {
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mSelectedAnswer != null) {
                        mSelectedAnswer.setChecked(false);
                    }
                    mSelectedAnswer = buttonView;
                }
            });
        }
    }


    private void setUpRemoveButtons() {
        mRemoveOption[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnswerOptions[2].setText("");
                ArrayList<String> newOptions = mQuestionObject.getOptions();
                newOptions.set(2, "");
                mQuestionObject.setOptions(newOptions);
                mOptionalLayout[0].setVisibility(View.GONE);

                mDoneButton.setEnabled(true);

//                boolean flag = true;
//                for(int i=0; i<2; i++) {
//                    if(mAnswerOptions[i].getText().length() == 0) {
//                        flag = false;
//                        break;
//                    }
//                }
//                if(flag) {
//                    if((mAnswerOptions[3].getText().length() == 0) && (mAnswerOptions[3].getVisibility() != View.GONE)) {
//                        flag = false;
//                    }
//                }
//                mDoneButton.setEnabled(flag);

            }
        });
        mRemoveOption[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnswerOptions[3].setText("");
                ArrayList<String> newOptions = mQuestionObject.getOptions();
                newOptions.set(3, "");
                mQuestionObject.setOptions(newOptions);
                mOptionalLayout[1].setVisibility(View.GONE);

                mDoneButton.setEnabled(true);


//                boolean flag = true;
//                for(int i=0; i<2; i++) {
//                    if(mAnswerOptions[i].getText().length() == 0) {
//                        flag = false;
//                        break;
//                    }
//                }
//                if(flag) {
//                    if((mAnswerOptions[2].getText().length() == 0) && (mAnswerOptions[2].getVisibility() != View.GONE)) {
//                        flag = false;
//                    }
//                }
//                mDoneButton.setEnabled(flag);
            }
        });

//        mRemoveOption[0].setOnClickListener(new ChangeVisibilityClick(mOptionalLayout[0], View.GONE));
//        mRemoveOption[1].setOnClickListener(new ChangeVisibilityClick(mOptionalLayout[1], View.GONE));
    }

    private boolean[] mFlag = new boolean[MAX_OPTIONS + 1];

    @Override
    public void conditionSatisfied(EditText sender) {
        switch (sender.getId()) {
            case R.id.question_text:
                mFlag[0] = true;
                break;
            case R.id.answer_option_0:
                mFlag[1] = true;
                break;
            case R.id.answer_option_1:
                mFlag[2] = true;
                break;
            case R.id.answer_option_2:
                mFlag[3] = true;
                break;
            case R.id.answer_option_3:
                mFlag[4] = true;
                break;
        }
        int j=0;
        for (int i = 0; i<3;i++){
            if(mFlag[i])
                j++;
        }
        if(j==3){
            mDoneButton.setEnabled(true);
        }
    }

    @Override
    public void conditionFailed(EditText sender) {
        switch (sender.getId()) {
            case R.id.question_text:
                mFlag[0] = false;
                break;
            case R.id.answer_option_0:
                mFlag[1] = false;
                break;
            case R.id.answer_option_1:
                mFlag[2] = false;
                break;
            case R.id.answer_option_2:
                mFlag[3] = false;
                break;
            case R.id.answer_option_3:
                mFlag[4] = false;
                break;
        }
        for (int i=0;i<3;i++ ) {
            if(!mFlag[i]){
//                mDoneButton.setEnabled(false);
            }
        }
    }

    public void loadFromQuestionObject(Question question) {
        System.out.println("loadFromQuestionObject : called");
        System.out.println(question.toString());
        mQuestion.setText(question.getQuestionString());
        ArrayList<String> options = question.getOptions();
        System.out.println("Options : " + options);
        for(int i=0; i<4; i++) {
            String option;
            try {
                option = options.get(i);
            } catch (Exception e) {
                e.printStackTrace();
                option = "";
            }

            if(i >= 2) {
                if(!option.equals("") && !question.equals(null)) {
                    mOptionalLayout[i-2].setVisibility(View.VISIBLE);
                } else {
                    mOptionalLayout[i-2].setVisibility(View.GONE);
                }
            }
            mAnswerOptions[i].setText(option);

        }
        mDoneButton.setEnabled(true);
    }
}
