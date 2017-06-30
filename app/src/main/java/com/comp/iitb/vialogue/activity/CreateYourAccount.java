package com.comp.iitb.vialogue.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnOtpReceived;
import com.comp.iitb.vialogue.coordinators.OnOtpSent;
import com.comp.iitb.vialogue.coordinators.OnPhoneNumberValidityChanged;
import com.comp.iitb.vialogue.dialogs.VerifyOtp;
import com.comp.iitb.vialogue.library.SendOtpAsync;
import com.comp.iitb.vialogue.listeners.PhoneNumberEditTextValidityListener;
import com.comp.iitb.vialogue.listeners.SmsOtpListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static android.provider.Telephony.Carriers.PASSWORD;

public class CreateYourAccount extends AppCompatActivity implements View.OnClickListener {

    // constants
    private static final int SMS_READ_PERMISSION = 1235;
    private static final int SMS_RECEIVE_PERMISSION = 1236;

    //variables
    private String mPhoneNumber = null;
    private String mPersonName = null;
    private String mProfilePictureUrl = null;
    private String mEmail = null;
    private Integer mOtp;
    private static final int DELAY_BETWEEN_OTP_REQUESTS_MILLIS = 60000; // 1 minute


    //others
    String PHONE_NUMBER = "User_mobile_number";
    private PhoneNumberEditTextValidityListener PhoneNumberEditTextValidityListener;
    public static GoogleApiClient mGoogleApiClient;
    private static ProgressDialog mProgressDialog;



    //UI elements
    Button googleSignInButton;
    EditText enterPhoneNoEditText;
    Button generateOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_your_account);

        // initialise UI elements.
        googleSignInButton = (Button) findViewById(R.id.google_sign_in);

        enterPhoneNoEditText = (EditText) findViewById(R.id.enter_phone_edit_text);

        generateOtp = (Button) findViewById(R.id.next);


        // adding listeners

        googleSignInButton.setOnClickListener(this);
        generateOtp.setOnClickListener(this);

        PhoneNumberEditTextValidityListener = new PhoneNumberEditTextValidityListener(
                enterPhoneNoEditText,
                new OnPhoneNumberValidityChanged() {
                    @Override
                    public void onValidityChanged(boolean isValid) {
                        generateOtp.setEnabled(isValid);
                        Log.d("CreateYourAccount", enterPhoneNoEditText.getText().toString());
                    }
                }
        );


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            // GOOGLE SIGN IN
            case R.id.google_sign_in:
                //signIn();
                break;

            // GENERATE OTP
            case R.id.next:
                Log.d("CreateYourAccount", "next button clicked");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    // RECEIVE SMS permission
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                        // permission granted
                    } else {
                        // permission not granted, ask for permission
                        ActivityCompat.requestPermissions(CreateYourAccount.this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSION);
                    }

                    // READ SMS permission
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                        // permission granted
                    } else {
                        // permission not granted, ask for permission
                        ActivityCompat.requestPermissions(CreateYourAccount.this, new String[]{Manifest.permission.READ_SMS}, SMS_READ_PERMISSION);
                    }
                }

                mPhoneNumber = "+91" + enterPhoneNoEditText.getText().toString();
                Bundle dialogInfo = new Bundle();
                dialogInfo.putString(PHONE_NUMBER, mPhoneNumber);
                //verifyOtp(mPhoneNumber);

                VerifyOtp verifyOtpDialog = new VerifyOtp(CreateYourAccount.this, dialogInfo);
                verifyOtpDialog.show();
                break;

        }
    }




    private boolean mCanSendOtp = true;
    public void verifyOtp(String phoneNumber) {
        if(mCanSendOtp) {

            mCanSendOtp = false;
            new SendOtpAsync(CreateYourAccount.this, new OnOtpSent() {
                @Override
                public void onDone(Object object, ParseException e) {

                    // otp generated successfully
                    mOtp = (Integer) object;

                    Toast.makeText(getBaseContext(), R.string.otpVerification, Toast.LENGTH_SHORT).show();

                    // add sms listener
                    SmsOtpListener.bindListener(new OnOtpReceived() {
                        @Override
                        public void onDone(Integer otp) {
                            if(otp.equals(mOtp)) {
                                onOtpVerified();
                            }
                        }
                    });

                    // wait until new otp can be generated
                    (new AsyncTask<Void, Void, Void>() {
                        @Override
                        public void onPreExecute() {
                            mCanSendOtp = false;
                        }

                        @Override
                        public Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(DELAY_BETWEEN_OTP_REQUESTS_MILLIS);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public void onPostExecute(Void result) {
                            System.out.println("onPostExecute : called");
                            mCanSendOtp = true;
                        }
                    }).execute();

                }

                @Override
                public void onCouldNotSend() {
                    // otp could not be generated
                    mCanSendOtp = true;
                    Toast.makeText(CreateYourAccount.this, R.string.cannotGenOTP, Toast.LENGTH_LONG).show();
                    SmsOtpListener.unbindListener();
                    finish();
                }
            }).execute(phoneNumber);

        } else {
            // do nothing, but fool the user
            // make him think that he is at least doing something
            Toast.makeText(CreateYourAccount.this, "OTP Has already been sent to your mobile number", Toast.LENGTH_LONG).show();
        }
    }

    public void onOtpVerified() {
        Toast.makeText(CreateYourAccount.this, R.string.otpVerified, Toast.LENGTH_SHORT).show();
        SmsOtpListener.unbindListener();
        signInParseUser();
    }

    private void signInParseUser() {
        mProgressDialog = ProgressDialog.show(CreateYourAccount.this, "Signing In", "Please Wait ...");
        String userName = null;
        if(mEmail == null) {
            // SIGN IN WITH PHONE NUMBER
            userName = mPhoneNumber;
        } else {
            // SIGN IN WITH EMAIL ID
            userName = mEmail;
        }

        ParseUser user = new ParseUser();
        user.setUsername(userName);
        if(mEmail != null) {
            user.setEmail(mEmail);
        }
        user.setPassword(PASSWORD);
        if(mPersonName != null) {
            user.put("name", mPersonName);
        }
        if(mProfilePictureUrl != null) {
            user.put("profile_picture_uri", mProfilePictureUrl);
        }
        if(mPhoneNumber != null) {
            user.put("phone_number", mPhoneNumber);
        }

        // Try signing up th user
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // ERROR SIGNING UP
                    switch(e.getCode()) {
                        case ParseException.USERNAME_TAKEN :
                            // User already exists in the database, so just log him in
                            logInParseUser();
                            break;
                        default:
                            // some other error
                            onCouldNotSignIn();
                            break;
                    }
                } else {
                    onSignedIn();
                }

            }
        });
    }

    public void logInParseUser() {
        String userName = null;
        if(mEmail == null) {
            // SIGN IN WITH PHONE NUMBER
            userName = mPhoneNumber;
        } else {
            // SIGN IN WITH EMAIL ID
            userName = mEmail;
        }
        ParseUser user = new ParseUser();
        user.logInInBackground(userName, PASSWORD, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                onSignedIn();
            }
        });
    }

    public void onSignedIn() {
        String userName = null;
        if(mEmail == null) {
            // SIGN IN WITH PHONE NUMBER
            Toast.makeText(CreateYourAccount.this, getResources().getString(R.string.mobileSignedSuccess) + mPhoneNumber, Toast.LENGTH_LONG).show();
        } else {
            // SIGN IN WITH EMAIL ID
            Toast.makeText(CreateYourAccount.this, getResources().getString(R.string.emailSignedSuccess) + mEmail, Toast.LENGTH_LONG).show();
        }
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {}
        finish();
    }

    public void onCouldNotSignIn() {
        Toast.makeText(CreateYourAccount.this, R.string.network_signIn, Toast.LENGTH_LONG).show();
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {}
    }


}
