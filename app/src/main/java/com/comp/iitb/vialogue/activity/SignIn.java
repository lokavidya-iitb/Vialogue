package com.comp.iitb.vialogue.activity;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnOtpReceived;
import com.comp.iitb.vialogue.coordinators.OnOtpSent;
import com.comp.iitb.vialogue.coordinators.OnPhoneNumberValidityChanged;
import com.comp.iitb.vialogue.coordinators.OnSignedOut;
import com.comp.iitb.vialogue.helpers.SharedPreferenceHelper;
import com.comp.iitb.vialogue.library.SendOtpAsync;
import com.comp.iitb.vialogue.listeners.PhoneNumberEditTextValidityListener;
import com.comp.iitb.vialogue.listeners.SmsOtpListener;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Random;


public class SignIn extends AppCompatActivity implements
        View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static Activity mSignInActivity;

    // constants
    private static final int SMS_READ_PERMISSION = 1235;
    private static final int SMS_RECEIVE_PERMISSION = 1236;
    private static final String TAG = SignIn.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private static final int PASSWORD_NUM_CHARACTERS = 36;
    private static final String PASSWORD = "Gwwla`U1uFJ;x_Khp%Wy>^cK61+[^kqXkX>HO=He";
    private static final int DELAY_BETWEEN_OTP_REQUESTS_MILLIS = 60000; // 1 minute

    // variables
    private Integer mOtp;
    private String mPhoneNumber = null;
    private String mPersonName = null;
    private String mProfilePictureUrl = null;
    private String mEmail = null;
    public static GoogleApiClient mGoogleApiClient;
    private boolean mSilentSignIn = false;

    // UI Elements
    private Button btnSignIn;
    private EditText mPhoneNumberEditText;
    private Button mGenerateOtpButton;
    private EditText mOtpEditText;
    private Button mVerifyOtpButton;
    private TextView mOrTextView;

    // Others
    private PhoneNumberEditTextValidityListener mPhoneNumberEditTextValidityListener;
    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mSignInActivity = this;

        // Initialize UI Components
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mPhoneNumberEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        mGenerateOtpButton = (Button) findViewById(R.id.generate_otp_button);
        mOtpEditText = (EditText) findViewById(R.id.enter_otp_edit_text);
        mVerifyOtpButton = (Button) findViewById(R.id.verify_otp_button);
        mOrTextView = (TextView) findViewById(R.id.or_text_view);

        // Add Listeners
        btnSignIn.setOnClickListener(this);
        mGenerateOtpButton.setOnClickListener(this);
        mVerifyOtpButton.setOnClickListener(this);

        mPhoneNumberEditTextValidityListener = new PhoneNumberEditTextValidityListener(
                mPhoneNumberEditText,
                new OnPhoneNumberValidityChanged() {
                    @Override
                    public void onValidityChanged(boolean isValid) {
                        mGenerateOtpButton.setEnabled(isValid);
                    }
                }
        );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            // SIGN IN
            case R.id.btn_sign_in:
                signIn();
                break;

            // GENERATE OTP
            case R.id.generate_otp_button :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    // RECEIVE SMS permission
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                        // permission granted
                    } else {
                        // permission not granted, ask for permission
                        ActivityCompat.requestPermissions(SignIn.this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSION);
                    }

                    // READ SMS permission
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                        // permission granted
                    } else {
                        // permission not granted, ask for permission
                        ActivityCompat.requestPermissions(SignIn.this, new String[]{Manifest.permission.READ_SMS}, SMS_READ_PERMISSION);
                    }
                }

                mPhoneNumber = "+91" + mPhoneNumberEditText.getText().toString();
                verifyOtp(mPhoneNumber);
                mOtpEditText.setVisibility(View.VISIBLE);
                mVerifyOtpButton.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.GONE);
                mOrTextView.setVisibility(View.GONE);
                break;

            // VERIFY OTP
            case R.id.verify_otp_button :
                try {
                    Integer otp = Integer.parseInt(mOtpEditText.getText().toString());
                    if(otp.equals(mOtp)) {
                        onOtpVerified();
                    }
                } catch (Exception e) {}
                Toast.makeText(SignIn.this, R.string.invalidOTP, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void signIn() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(isNetworkAvailable()) {
            // network available
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            onCouldNotSignIn();
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public static void signOut(Context context, final OnSignedOut onSignedOut) {
        final Context context_ = context;
        mProgressDialog = ProgressDialog.show(context, "Logging Out", "Please wait ...");
//        try {
//            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                    new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(Status status) {
//                        }
//                    });
//            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                    new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(Status status) {}
//                    });
//        } catch (Exception e) {}

        ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    onLoggedOut(context_);
                } else {
                    onCouldNotLogOut(context_);
                }
                onSignedOut.done(e);
            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            mPersonName = acct.getDisplayName();
            mEmail = acct.getEmail();
            try {
                mProfilePictureUrl = acct.getPhotoUrl().toString();
            } catch (NullPointerException e) {mProfilePictureUrl = "";}

            // sign out of Google
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                        }
                    });
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {}
                    });

            // sign into Parse
            signInParseUser();
        } else {
            if(mSilentSignIn || isNetworkAvailable()) {
                mSilentSignIn = false;
            } else {
                onCouldNotSignIn();
            }
        }
    }

    private boolean mCanSendOtp = true;
    public void verifyOtp(String phoneNumber) {
        if(mCanSendOtp) {

            mCanSendOtp = false;
            new SendOtpAsync(SignIn.this, new OnOtpSent() {
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
                    Toast.makeText(SignIn.this, R.string.cannotGenOTP, Toast.LENGTH_LONG).show();
                    SmsOtpListener.unbindListener();
                    finish();
                }
            }).execute(phoneNumber);

        } else {
            // do nothing, but fool the user
            // make him think that he is at least doing something
            Toast.makeText(SignIn.this, "OTP Has already been sent to your mobile number", Toast.LENGTH_LONG).show();
        }
    }

    public void onOtpVerified() {
        Toast.makeText(SignIn.this, R.string.otpVerified, Toast.LENGTH_SHORT).show();
        SmsOtpListener.unbindListener();
        signInParseUser();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void signInParseUser() {
        mProgressDialog = ProgressDialog.show(SignIn.this, "Signing In", "Please Wait ...");
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
            Toast.makeText(SignIn.this, getResources().getString(R.string.mobileSignedSuccess) + mPhoneNumber, Toast.LENGTH_LONG).show();
        } else {
            // SIGN IN WITH EMAIL ID
            Toast.makeText(SignIn.this, getResources().getString(R.string.emailSignedSuccess) + mEmail, Toast.LENGTH_LONG).show();
        }
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {}
        finish();
    }

    public void onCouldNotSignIn() {
        Toast.makeText(SignIn.this, R.string.network_signIn, Toast.LENGTH_LONG).show();
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {}
    }

    private static void onLoggedOut(Context context) {
        Toast.makeText(context, R.string.loggedOut, Toast.LENGTH_LONG).show();
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {}
    }

    private static void onCouldNotLogOut(Context context) {
        Toast.makeText(context, R.string.cannotSignIn, Toast.LENGTH_LONG).show();
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length != 0) {
            switch(requestCode) {
                case SMS_READ_PERMISSION :
                    if(!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(SignIn.this, R.string.gimmeSMS, Toast.LENGTH_LONG).show();
                    }
                    break;
                case SMS_RECEIVE_PERMISSION :
                    if(!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(SignIn.this, R.string.gimmeSMS, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        } else {
            // TODO handle this (first understand when this happens exactly)
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mSilentSignIn = true;

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            try {
                showProgressDialog();
            } catch (Exception e) {}
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    try {
                        hideProgressDialog();
                    } catch (Exception e) {}
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
