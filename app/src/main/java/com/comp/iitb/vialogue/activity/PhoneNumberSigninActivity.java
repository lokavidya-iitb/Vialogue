package com.comp.iitb.vialogue.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnOtpReceived;
import com.comp.iitb.vialogue.coordinators.OnOtpSent;
import com.comp.iitb.vialogue.coordinators.OnPhoneNumberValidityChanged;
import com.comp.iitb.vialogue.helpers.SharedPreferenceHelper;
import com.comp.iitb.vialogue.library.SendOtpAsync;
import com.comp.iitb.vialogue.listeners.PhoneNumberEditTextValidityListener;
import com.comp.iitb.vialogue.listeners.SmsOtpListener;
import com.parse.ParseException;

import java.util.ArrayList;

public class PhoneNumberSigninActivity extends AppCompatActivity {

    // constants
    private static final int SMS_READ_PERMISSION = 1235;
    private static final int SMS_RECEIVE_PERMISSION = 1236;
    private ArrayList<Integer> mOtp;

    private EditText mPhoneNumberEditText;
    private Button mGenerateOtpButton;
    private EditText mOtpEditText;
    private Button mVerifyOtpButton;
    private PhoneNumberEditTextValidityListener mPhoneNumberEditTextValidityListener;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_signin);

        mPhoneNumberEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        mGenerateOtpButton = (Button) findViewById(R.id.generate_otp_button);
        mOtpEditText = (EditText) findViewById(R.id.enter_otp_edit_text);
        mVerifyOtpButton = (Button) findViewById(R.id.verify_otp_button);
        mOtp = new ArrayList<Integer>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // RECEIVE SMS permission
            if (getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                // permission granted
            } else {
                // permission not granted, ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSION);
            }

            // READ SMS permission
            if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                // permission granted
            } else {
                // permission not granted, ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_READ_PERMISSION);
            }
        }

        mGenerateOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), R.string.otpVerification, Toast.LENGTH_SHORT).show();
                String phoneNumber = "+91" + mPhoneNumberEditText.getText().toString();
                verifyOtp(phoneNumber);
                mOtpEditText.setVisibility(View.VISIBLE);
                mVerifyOtpButton.setVisibility(View.VISIBLE);
            }
        });

        mPhoneNumberEditTextValidityListener = new PhoneNumberEditTextValidityListener(
                mPhoneNumberEditText,
                new OnPhoneNumberValidityChanged() {
                    @Override
                    public void onValidityChanged(boolean isValid) {
                        mGenerateOtpButton.setEnabled(isValid);
                    }
                }
        );

        mVerifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Integer otp = Integer.parseInt(mOtpEditText.getText().toString());
                    for(Integer generatedOtp: mOtp) {
                        if(otp.equals(generatedOtp)) {
                            onOtpVerified();
                            return;
                        }
                    }
                } catch (Exception e) {}
                Toast.makeText(PhoneNumberSigninActivity.this, R.string.invalidOTP, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void verifyOtp(String phoneNumber) {

        new SendOtpAsync(PhoneNumberSigninActivity.this, new OnOtpSent() {
            @Override
            public void onDone(Object object, ParseException e) {
                if(e == null) {
                    // otp generated successfully
                    mOtp.add((Integer) object);
                } else {
                    // otp could not be generated
                    Toast.makeText(PhoneNumberSigninActivity.this, R.string.cannotGenOTP, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }).execute(phoneNumber);

        SmsOtpListener.bindListener(new OnOtpReceived() {
            @Override
            public void onDone(Integer otp) {
                for(Integer generatedOtp: mOtp) {
                    if(otp.equals(generatedOtp)) {
                        onOtpVerified();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case SMS_READ_PERMISSION :
                if(!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(PhoneNumberSigninActivity.this, R.string.gimmeSMS, Toast.LENGTH_LONG).show();
                }
            case SMS_RECEIVE_PERMISSION :
                if(!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(PhoneNumberSigninActivity.this, R.string.gimmeSMS, Toast.LENGTH_LONG).show();
                }
        }
    }

    public void onOtpVerified() {
        Toast.makeText(PhoneNumberSigninActivity.this, R.string.otpVerified, Toast.LENGTH_SHORT).show();
        finish();
    }

}
