package com.comp.iitb.vialogue.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.listeners.MinimumConditionOnTextChangeListener;
import com.comp.iitb.vialogue.listeners.QuestionDoneListener;
import com.comp.iitb.vialogue.listeners.SingleChoiceQuestionConditionListener;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;

import java.util.ArrayList;

public class MultiChoiceQuestionDialog extends Dialog implements SingleChoiceQuestionConditionListener{

    private static final int MAX_OPTIONS_COUNT = 4;
    private static final int SHOULD_ALLOW_DELETE_MIN = 2;

    private Context mContext;

    private EditText mQuestionStringEditText;
    private LinearLayout mQuestionOptionsCheckBox;
    private ImageButton mAddOptionImageButton;
    private Button mDoneButton;

    private QuestionDoneListener mQuestionDoneListener;

    private Question mQuestion;
    private int mSlideNumber;
    public static ArrayList<Boolean> mConditionSatisfiesStackList = new ArrayList<Boolean>();

    public MultiChoiceQuestionDialog(Context context, QuestionDoneListener questionDoneListener) {
        super(context);
        mContext = context;
        mQuestionDoneListener = questionDoneListener;
    }

    public MultiChoiceQuestionDialog(Context context, QuestionDoneListener questionDoneListener, Question question, int slideNumber) {
        this(context, questionDoneListener);
        mQuestion = question;
        mSlideNumber = slideNumber;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup UI
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_multi_choice_question_dialog);

        // Initialize UI Components
        mQuestionStringEditText = (EditText) findViewById(R.id.question_string_edit_text_mc);
        mQuestionOptionsCheckBox = (LinearLayout) (findViewById(R.id.options_mc).findViewById(R.id.checkbox_group));
        mAddOptionImageButton = (ImageButton) findViewById(R.id.add_option_image_button_mc);
        mDoneButton = (Button) findViewById(R.id.done_button_mc);

        // Initialize state
        mConditionSatisfiesStackList = new ArrayList<Boolean>();
        if(mQuestion != null) {
            // load state from the provided question
            String questionString = mQuestion.getQuestionString();
            ArrayList<String> options = mQuestion.getOptions();
            ArrayList<Integer> correctOptions = mQuestion.getCorrectOptions();

            mQuestionStringEditText.setText(questionString);

            for(int i=0; i<options.size(); i++) {
                String option = options.get(i);
                if(i < 2) {
                    boolean isChecked = false;
                    for(int j=0; j<correctOptions.size(); j++) {
                        if(correctOptions.get(j) == i) {
                            isChecked = true;
                            break;
                        }
                    }
                    addOption(false, isChecked, option);
                } else {
                    boolean isChecked = false;
                    for(int j=0; j<correctOptions.size(); j++) {
                        if(correctOptions.get(j) == i) {
                            isChecked = true;
                            break;
                        }
                    }
                    addOption(true, isChecked, option);
                }
            }
            mConditionSatisfiesStackList.add(true);
        } else {
            // load default state
            addOption(false, true);
            addOption(false);
            mConditionSatisfiesStackList.add(false);
        }
        final int questionStringConditionIndex = mConditionSatisfiesStackList.size() - 1;

        // Setup Listeners
        mQuestionStringEditText.addTextChangedListener(new MinimumConditionOnTextChangeListener(this, mQuestionStringEditText, questionStringConditionIndex));
//        setupRadioButtonsGroup();
        mAddOptionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOption();
            }
        });
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> options = new ArrayList<String>();
                for(int i=0; i<mQuestionOptionsCheckBox.getChildCount(); i++) {
                    options.add(((EditText) mQuestionOptionsCheckBox.getChildAt(i).findViewById(R.id.edit_text_mc)).getText().toString());
                }

                ArrayList<Integer> correctOptions = new ArrayList<Integer>();
                for(int i=0; i<mQuestionOptionsCheckBox.getChildCount(); i++) {
                    if(((CheckBox) mQuestionOptionsCheckBox.getChildAt(i).findViewById(R.id.checkbox_group)).isChecked()) {
                        correctOptions.add(i);
                    }
                }
                mQuestionDoneListener.onDone(
                        mQuestionStringEditText.getText().toString(),
                        options,
                        correctOptions,
                        mSlideNumber
                );
                dismiss();
            }
        });
        calculateDoneButtonEnabled();
    }

    private void setupRadioButtonsGroup() {
        for(int i=0; i<mQuestionOptionsCheckBox.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mQuestionOptionsCheckBox.getChildAt(i).findViewById(R.id.radio_button);
            setupRadioButton(radioButton);
        }
    }

    private void setupRadioButton(final RadioButton radioButton) {
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllRadioButtonsButOneUnchecked(radioButton);
            }
        });
    }

    private void setAllRadioButtonsButOneUnchecked(RadioButton checkedRadioButton) {
        for(int i=0; i<mQuestionOptionsCheckBox.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mQuestionOptionsCheckBox.getChildAt(i).findViewById(R.id.radio_button);
            if(checkedRadioButton.equals(radioButton)) {
                radioButton.setChecked(true);
            } else {
                radioButton.setChecked(false);
            }
        }
    }

    public void addOption(boolean shouldAllowDelete, boolean isChecked, String optionString) {

        // check if adding option is allowed
        if(mQuestionOptionsCheckBox.getChildCount() >= MAX_OPTIONS_COUNT) {
            Toast.makeText(getContext(), "Cannot add more than " + MAX_OPTIONS_COUNT + " options", Toast.LENGTH_LONG).show();
            return;
        }

        mConditionSatisfiesStackList.add(!optionString.equals(""));
        final int index = mConditionSatisfiesStackList.size() - 1;

        final View singleOptionView = getLayoutInflater().inflate(R.layout.content_multi_choice_question_option, null);

        // Instantiate UI Components
        final EditText editText = (EditText) singleOptionView.findViewById(R.id.edit_text);
        final RadioButton radioButton = (RadioButton) singleOptionView.findViewById(R.id.radio_button);
        final Button deleteButton = (Button) singleOptionView.findViewById(R.id.delete_button);

        radioButton.setChecked(isChecked);
        editText.setText(optionString);

        // Set listeners
        editText.addTextChangedListener(new MinimumConditionOnTextChangeListener(this, editText, index));
        if(shouldAllowDelete) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mQuestionOptionsCheckBox.removeView(singleOptionView);
                    mAddOptionImageButton.setVisibility(View.VISIBLE);

                    if(!MinimumConditionOnTextChangeListener.doesItReallyHaveChars(editText.getText().toString())) {
                        mConditionSatisfiesStackList.set(index, true);
                    }
                    calculateDoneButtonEnabled();
                }
            });
        } else {
            deleteButton.setVisibility(View.GONE);
        }
        setupRadioButton(radioButton);

        // add view to RadioGroup
        mQuestionOptionsCheckBox.addView(singleOptionView);

        // if addOption button has to be disabled
        if(mQuestionOptionsCheckBox.getChildCount() == MAX_OPTIONS_COUNT) {
            mAddOptionImageButton.setVisibility(View.GONE);
        }

        mDoneButton.setEnabled(false);
    }

    public void addOption(boolean shouldAllowDelete, boolean isChecked) {
        addOption(shouldAllowDelete, isChecked, "");
    }

    private void addOption(boolean shouldAllowDelete) {
        addOption(shouldAllowDelete, false);
    }

    private void addOption() {
        addOption(true);
    }

    private void loadStateFromQuestion() {
        // TODO Implement
    }

    @Override
    public void conditionSatisfied(int index) {
        mConditionSatisfiesStackList.set(index, true);
        calculateDoneButtonEnabled();
    }

    @Override
    public void conditionFailed(int index) {
        mConditionSatisfiesStackList.set(index, false);
        calculateDoneButtonEnabled();
    }

    public void calculateDoneButtonEnabled() {
        System.out.println(mConditionSatisfiesStackList);
        for(boolean b : mConditionSatisfiesStackList) {
            if(!b) {
                mDoneButton.setEnabled(false);
                return;
            }
        } mDoneButton.setEnabled(true);
    }

}









