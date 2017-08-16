package com.comp.iitb.vialogue.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.LogIn;
import com.comp.iitb.vialogue.Network.LokavidyaSso.SharedPreferencesDetails;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnDoneCallingSsoApiResult;
import com.comp.iitb.vialogue.coordinators.OnDoneLogIn;
import com.comp.iitb.vialogue.dialogs.ForgotPasswordDialog;
import com.comp.iitb.vialogue.library.SsoMethods;

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
    SharedPreferences mLokavidyaSsoSharedPreferences;

    private static String mResponseString;
    private String mRegistrationData;

    LogIn.RegistrationType mRegistrationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = LoginActivity.this;
        mLokavidyaSsoSharedPreferences = mContext.getSharedPreferences(SharedPreferencesDetails.SHARED_PREFERENCES_NAME, 0);

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
                    mRegistrationData = "+91" + mLoginId;
                } else {
                    mRegistrationType = LogIn.RegistrationType.EMAIL_ID;
                    mRegistrationData = mLoginId;
                }

                System.out.println("mRegistrationType: " + mRegistrationType);

                new SsoMethods(new OnDoneCallingSsoApiResult() {
                    @Override
                    public void onDone(Bundle info) {
                        new SsoMethods().signUpUsingParse(mRegistrationData, mLokavidyaSsoSharedPreferences.getString(SharedPreferencesDetails.SESSION_NAME, ""), mPassword);
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).login(mContext, mRegistrationType, mRegistrationData, mPassword);
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
}