package com.comp.iitb.vialogue.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.ForgotPassword;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.ResetPasswordActivity;
import com.comp.iitb.vialogue.coordinators.OnDoneForgotPassword;

public class ForgotPasswordDialog extends Dialog {

    private static final String PHONE_NUMBER_REGEX = "(^)([\\d]){10}$";

    TextView mEnterEmailOrPhoneNoTextView;
    EditText mEnterEmailOrPhoneNoEditText;
    Button mGenerateOtpButton;

    //temperory for testing purpose
    EditText mEnterOtp;
    Button mVerifyOtp;

    String mEmailOrPhone;
    String mRegistrationData;
    String mResponseString;

    ForgotPassword.RegistrationType mRegistrationType;

    Context mContext;

    Bundle args;

    public ForgotPasswordDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEnterEmailOrPhoneNoTextView = (TextView) findViewById(R.id.tv_enter_email_phone_no);
        mEnterEmailOrPhoneNoEditText = (EditText) findViewById(R.id.et_enter_email_phone_no);
        mGenerateOtpButton = (Button) findViewById(R.id.btn_generate_otp);

        //temperory for testing purpose
        mEnterOtp = (EditText) findViewById(R.id.et_enter_otp);
        mVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);

        args = new Bundle();

        mGenerateOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailOrPhone = mEnterEmailOrPhoneNoEditText.getText().toString();
                if (mEnterEmailOrPhoneNoEditText.length() == 0) {
                    mEnterEmailOrPhoneNoEditText.requestFocus();
                    mEnterEmailOrPhoneNoTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_light));
                    return;
                } else {
                    mEnterEmailOrPhoneNoTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.tab_indicator_text));

                    args.putString(mContext.getResources().getString(R.string.registrationData), mEmailOrPhone);
                    if (mEmailOrPhone.matches(PHONE_NUMBER_REGEX)) {
                        mRegistrationType = ForgotPassword.RegistrationType.PHONE_NUMBER;
                        args.putString(mContext.getResources().getString(R.string.registrationType), mContext.getResources().getString(R.string.phoneNo));
                    } else {
                        mRegistrationType = ForgotPassword.RegistrationType.EMAIL_ID;
                        args.putString(mContext.getResources().getString(R.string.registrationType), mContext.getResources().getString(R.string.email));
                    }

                    mRegistrationData = mEmailOrPhone;

                    forgotPassword(getContext(), mRegistrationType, mRegistrationData);

                    ForgotPasswordDialog.this.dismiss();
                }
            }
        });

        mVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEnterOtp.getText().toString().length() == 0) return;
                Bundle args = new Bundle();
                args.putString("otp", mEnterOtp.getText().toString());
                Intent intent = new Intent(getContext(), ResetPasswordActivity.class);
                intent.putExtras(args);
                mContext.startActivity(intent);
            }
        });

    }

    /*public void startResetPasswordActivity(String otp) {
        args.putString(mContext.getResources().getString(R.string.otp), otp);
        Intent intent = new Intent(mContext, ResetPasswordActivity.class);
        intent.putExtras(args);
        mContext.startActivity(intent);
    }*/

    private void forgotPassword(Context context, ForgotPassword.RegistrationType registrationType, String registrationData) {
        ForgotPassword.forgotPasswordInBackground(
                context,
                registrationType,
                registrationData,
                new OnDoneForgotPassword() {
                    @Override
                    public void done(ForgotPassword.ForgotPasswordResponse response) {
                        switch (response.getResponseType()) {
                            case OTP_SENT:
                                mResponseString = response.getResponseString();
                                VerifyOtp verifyOtpDialog = new VerifyOtp(mContext, args, 1);
                                verifyOtpDialog.show();
                            case USER_NOT_REGISTERED:
                                mResponseString = response.getResponseString();
                            case NETWORK_ERROR:
                                mResponseString = response.getResponseString();
                            case SOMETHING_WENT_WRONG:
                                mResponseString = response.getResponseString();
                        }
                        Toast.makeText(context, mResponseString, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
