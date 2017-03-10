package com.comp.iitb.vialogue.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.ConditionListener;
import com.comp.iitb.vialogue.listeners.MinimumConditionOnTextChangeListener;
import com.comp.iitb.vialogue.listeners.QuestionDoneListener;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;

/**
 * Created by ironstein on 10/03/17.
 */

public class SingleChoiceQuestionDialog extends Dialog implements View.OnClickListener {

    private static final int MAX_OPTIONS_COUNT = 4;

    private Context mContext;

    private EditText mQuestionStringEditText;
    private RadioGroup mQuestionOptionsRadioGroup;
    private ImageButton mAddOptionImageButton;
    private Button mDoneButton;

    private QuestionDoneListener mQuestionDoneListener;

    private Question mQuestion;
    private int mSlideNumber;

    public SingleChoiceQuestionDialog(Context context, QuestionDoneListener questionDoneListener) {
        super(context);
        mContext = context;
        mQuestionDoneListener = questionDoneListener;
    }

    public SingleChoiceQuestionDialog(Context context, QuestionDoneListener questionDoneListener, Question question, int slideNumber) {
        this(context, questionDoneListener);
        mQuestion = question;
        mSlideNumber = slideNumber;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup UI
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.single_choice_question_layout);

        // Initialize UI Components
        mQuestionStringEditText = (EditText) findViewById(R.id.question_string_edit_text);
        mQuestionOptionsRadioGroup = (RadioGroup) findViewById(R.id.question_options_radio_group);
        mAddOptionImageButton = (ImageButton) findViewById(R.id.add_option_image_button);
        mDoneButton = (Button) findViewById(R.id.done_button);

        mQuestionOptionsRadioGroup.addView(getLayoutInflater().inflate(R.layout.content_single_choice_question_option, null));

        ((RadioButton) mQuestionOptionsRadioGroup
                .findViewById(R.id.options)
                .findViewById(R.id.first_option)
                .findViewById(R.id.radio_button))
                .setChecked(true);

        // Setup Listeners
        mQuestionOptionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                System.out.println(i);
                for(int j=0; i<radioGroup.getChildCount(); j++) {
                    boolean setChecked = false;
                    if(j == i) {
                        setChecked = true;
                    } ((RadioButton) radioGroup.getChildAt(j).findViewById(R.id.radio_button)).setChecked(setChecked);
                }
            }
        });

        // Initialize state
        if(mQuestion != null) {
            loadStateFromQuestion();
        }
    }

    public void addOption() {
        addOption(true);
    }

    public void addOption(boolean shouldAllowDelete) {

        // check if adding option is allowed
        if(mQuestionOptionsRadioGroup.getChildCount() >= MAX_OPTIONS_COUNT) {
            Toast.makeText(getContext(), "Cannot add more than " + MAX_OPTIONS_COUNT + " options", Toast.LENGTH_LONG).show();
            return;
        }

        final View singleOptionView = getLayoutInflater().inflate(R.layout.content_single_choice_question_option, null);

        // Instantiate UI Components
        final EditText editText = (EditText) singleOptionView.findViewById(R.id.edit_text);
        final RadioButton radioButton = (RadioButton) singleOptionView.findViewById(R.id.radio_button);
        final Button deleteButton = (Button) singleOptionView.findViewById(R.id.delete_button);

        // Set listeners
//        (editText).addTextChangedListener(new MinimumConditionOnTextChangeListener(this, editText));
        if(shouldAllowDelete) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // delete existing text
                    editText.setText("");
                    singleOptionView.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

        }
    }

    public void loadStateFromQuestion() {
        // TODO Implement
    }

}
