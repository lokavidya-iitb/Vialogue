package com.comp.iitb.vialogue.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.ForgotPassword;
import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.SignUp;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.LoginActivity;
import com.comp.iitb.vialogue.coordinators.OnDoneCallingSsoApiResult;
import com.comp.iitb.vialogue.library.SsoMethods;

/**
 * Created by shubham on 27/6/17.
 */

public class RegenerateOtpDialogue extends Dialog implements View.OnClickListener {

    private Bundle info;
    private Context mContext;
    private EditText TimeoutPhoneNoEditText;
    private Button TimeoutRegenerateOtpButton;


    private String mPhoneNumber = null;
    String PHONE_NUMBER = "User_mobile_number";
    private boolean mCanSendOtp = true;

    private int resetActivityCode = 0;

    VerifyOtpDialogue verifyOtpOnTimeout;
    SignUp.RegistrationType mSignUpRegistrationType = SignUp.RegistrationType.PHONE_NUMBER;
    ForgotPassword.RegistrationType mForgotPasswordRegistrationType;
    String mUniqueId = null;

    RegenerateOtpDialogue(Context context, Bundle info) {
        super(context);
        mContext = context;
        this.info = info;
    }

    RegenerateOtpDialogue(Context context, Bundle info, int resetActivityCode) {
        super(context);
        mContext = context;
        this.info = info;
        this.resetActivityCode = resetActivityCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regenerate_otp_on_timeout);


        mPhoneNumber = info.getString(mContext.getResources().getString(R.string.registrationData));
        Log.d("RegenerateOtpDialogue", mContext.getResources().getString(R.string.registrationData));

        TimeoutPhoneNoEditText = (EditText) findViewById(R.id.timeout_phone_no_edit_text);
        TimeoutRegenerateOtpButton = (Button) findViewById(R.id.timeout_generate_otp);

        TimeoutPhoneNoEditText.setText(mPhoneNumber);
        TimeoutRegenerateOtpButton.setOnClickListener(this);

        RegenerateOtpDialogue.this.setCanceledOnTouchOutside(false);
        RegenerateOtpDialogue.this.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("RegenerateOtpDialogue", "inside on cancel");
            }
        });
        RegenerateOtpDialogue.this.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("RegenerateOtpDialogue", "inside on dismiss");
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (resetActivityCode == 1) {
            //verifyOtpOnTimeout = new VerifyOtpDialogue(mContext, info, 1);
            //verifyOtpOnTimeout.show();
            if(TimeoutPhoneNoEditText.getText().toString().equals("")) return;
            String contents = TimeoutPhoneNoEditText.getText().toString();

            if(info.getString(mContext.getResources().getString(R.string.registrationType)).equals(mContext.getResources().getString(R.string.phoneNo))) {
                mForgotPasswordRegistrationType = ForgotPassword.RegistrationType.PHONE_NUMBER;
                contents = "+91" + contents;
            } else {
                mForgotPasswordRegistrationType = ForgotPassword.RegistrationType.EMAIL_ID;
            }

            new SsoMethods(new OnDoneCallingSsoApiResult() {
                @Override
                public void onDone(Bundle bundleInfo) {
                    boolean otp_sent = bundleInfo.getBoolean("otp_sent");
                    RegenerateOtpDialogue.this.dismiss();
                    if(otp_sent) new VerifyOtpDialogue(mContext, info, 1).show();
                }
            }).forgotPassword(getContext(), mForgotPasswordRegistrationType, contents);
        } else {
            if(TimeoutPhoneNoEditText.getText().toString().equals("")) return;
            String phoneNo = TimeoutPhoneNoEditText.getText().toString();
            RegenerateOtpDialogue.this.dismiss();
            new SsoMethods(new OnDoneCallingSsoApiResult() {
                @Override
                public void onDone(Bundle bundleInfo) {
                    mUniqueId = bundleInfo.getString(mContext.getResources().getString(R.string.uniqueId));
                    //Intent intent = new Intent(mContext, LoginActivity.class);
                    if(mUniqueId != null) {
                        if(bundleInfo.getString(mContext.getResources().getString(R.string.registrationType)).equals(mContext.getResources().getString(R.string.phoneNo))) {
                            Bundle regenInfo = new Bundle();
                            regenInfo.putString(mContext.getResources().getString(R.string.uniqueId), mUniqueId);
                            regenInfo.putString(mContext.getResources().getString(R.string.userName), info.getString(mContext.getResources().getString(R.string.userName)));
                            regenInfo.putString(mContext.getResources().getString(R.string.password), info.getString(mContext.getResources().getString(R.string.password)));
                            regenInfo.putString(mContext.getResources().getString(R.string.registrationType), mContext.getResources().getString(R.string.phoneNo));
                            regenInfo.putString(mContext.getResources().getString(R.string.registrationData), phoneNo);
                            new VerifyOtpDialogue(mContext, regenInfo).show();
                        } else {
                            //mContext.startActivity(intent);
                        }
                    } else {
                        //mContext.startActivity(intent);
                    }
                }
            }).signUp(mContext, info.getString(mContext.getResources().getString(R.string.userName)), mSignUpRegistrationType, phoneNo, info.getString(mContext.getResources().getString(R.string.password)), info.getString(mContext.getResources().getString(R.string.uniqueId)));
        }
        resetActivityCode = 0;
    }
}