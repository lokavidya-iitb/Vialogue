package com.comp.iitb.vialogue.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.comp.iitb.vialogue.R;

public class CreateNewAccountScreen1 extends AppCompatActivity {

    private EditText mEmailIdEditText;
    private EditText mPhoneNumberEditText;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account_screen1);

        // instantiate UI Variables
        mEmailIdEditText = (EditText) findViewById(R.id.email_id_edit_text);
        mPhoneNumberEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        mNextButton = (Button) findViewById(R.id.next_button);

        // Add listeners
        mEmailIdEditText.addTextChangedListener(new TextWatcher() {

            private boolean mAllowEditing = false;
            private String mStringBeforeTextChanged = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStringBeforeTextChanged = charSequence.toString();
                System.out.println("string before text changed : " + mStringBeforeTextChanged);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Patterns.EMAIL_ADDRESS.matcher(charSequence.toString()).matches()) {
                    mAllowEditing = true;
                } else {
                    mAllowEditing = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!mAllowEditing) {
                    mEmailIdEditText.setText(mStringBeforeTextChanged);
                }
            }
        });


        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
