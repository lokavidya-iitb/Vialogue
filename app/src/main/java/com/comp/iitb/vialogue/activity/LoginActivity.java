package com.comp.iitb.vialogue.activity;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.LogIn;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnDoneLogIn;
import com.comp.iitb.vialogue.dialogs.ForgotPasswordDialog;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String PHONE_NUMBER_REGEX = "(^)([\\d]){10}$";

    //ui elements
    private TextView mRegisteredNumberIdTextView;
    private EditText mRegisteredNumberIdEditText;
    private TextView mEnterLoginPasswordTextView;
    private EditText mEnterLoginPasswordEditText;
    private TextView mForgotPasswordTextView;
    private Button mLoginButton;
    private String mLoginId;
    private String mPassword;
    private Context mContext;

    private static String mResponseString;
    private String mRegistrationData;

    LogIn.RegistrationType mRegistrationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = LoginActivity.this;

        //initialize ui elements
        mRegisteredNumberIdTextView = (TextView) findViewById(R.id.tv_registered_number_id);
        mRegisteredNumberIdEditText = (EditText) findViewById(R.id.et_registered_number_id);
        mEnterLoginPasswordTextView = (TextView) findViewById(R.id.tv_enter_login_password);
        mEnterLoginPasswordEditText = (EditText) findViewById(R.id.et_enter_login_password);
        mForgotPasswordTextView = (TextView) findViewById(R.id.tv_forgot_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginId = mRegisteredNumberIdEditText.getText().toString();
                if(mLoginId.length() == 0) {
                    mRegisteredNumberIdEditText.requestFocus();
                    mRegisteredNumberIdTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
                    return;
                } else {
                    mRegisteredNumberIdTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.tab_indicator_text));
                }
                mPassword = mEnterLoginPasswordEditText.getText().toString();
                if(mPassword.length() == 0) {
                    mEnterLoginPasswordEditText.requestFocus();
                    mEnterLoginPasswordTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
                    return;
                } else {
                    mEnterLoginPasswordTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.tab_indicator_text));
                }

                if(mLoginId.matches(PHONE_NUMBER_REGEX)) {
                    mRegistrationType = LogIn.RegistrationType.PHONE_NUMBER;
                } else {
                    mRegistrationType = LogIn.RegistrationType.EMAIL_ID;
                }

                System.out.println("mRegistrationType: " + mRegistrationType);
                mRegistrationData = mLoginId;

                login(mContext, mRegistrationType, mRegistrationData, mPassword);
            }
        });

        mForgotPasswordTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        mForgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog(mContext);
                forgotPasswordDialog.show();

                /*Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.activity_forgot_password);
                dialog.show();*/
            }
        });

    }

    public static void login(Context context, LogIn.RegistrationType registrationType, String registrationData, String password) {

        LogIn.logInInBackground(context, registrationType, registrationData, password, new OnDoneLogIn() {
            @Override
            public void done(LogIn.LogInResponse logInResponse) {
                switch (logInResponse.getResponseType()) {
                    case LOGGED_IN:
                        mResponseString = logInResponse.getResponseString();
                    case PASSWORD_DOES_NOT_MATCH:
                        mResponseString = logInResponse.getResponseString();
                    case USER_NOT_ACTIVE:
                        mResponseString = logInResponse.getResponseString();
                    case SOMETHING_WENT_WRONG:
                        mResponseString = logInResponse.getResponseString();
                    case NETWORK_ERROR:
                        mResponseString = logInResponse.getResponseString();
                }
                Toast.makeText(context, mResponseString, Toast.LENGTH_SHORT).show();
            }
        });
    }
}