
package com.comp.iitb.vialogue.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnPhoneNumberValidityChanged;
import com.comp.iitb.vialogue.dialogs.VerifyOtp;
import com.comp.iitb.vialogue.listeners.PhoneNumberEditTextValidityListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class CreateYourAccount extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = CreateYourAccount.class.getSimpleName();

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
    private static final int RC_SIGN_IN = 007;

    //others
    String PHONE_NUMBER = "User_mobile_number";
    private PhoneNumberEditTextValidityListener PhoneNumberEditTextValidityListener;
    public static GoogleApiClient mGoogleApiClient;
    private static ProgressDialog mProgressDialog;
    boolean mSilentSignIn =false;
    private Bundle info;

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

        info = new Bundle();

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
            // GOOGLE SIGN IN
            case R.id.google_sign_in:
                signIn();
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
                info.putString(getResources().getString(R.string.registrationData), mPhoneNumber);
                info.putString(getResources().getString(R.string.registrationType), getResources().getString(R.string.phoneNo));
                //verifyotp logic to be called here

                VerifyOtp verifyOtpDialog = new VerifyOtp(CreateYourAccount.this, info);
                verifyOtpDialog.show();
                break;

        }
    }

    private void signIn() {
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

    public void onCouldNotSignIn() {
        Toast.makeText(CreateYourAccount.this, R.string.network_signIn, Toast.LENGTH_LONG).show();
        try {
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

    private void handleSignInResult(GoogleSignInResult result) {
        //System.out.print("inside handle sign in result");
        //System.out.print("result:" + result.isSuccess());
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

            info.putString(getResources().getString(R.string.registrationData), mEmail);
            info.putString(getResources().getString(R.string.registrationType), getResources().getString(R.string.email));
            Intent intent = new Intent(CreateYourAccount.this, CreateYourAccount2.class);
            intent.putExtras(info);
            startActivity(intent);
            finish();
        }
        else {
            if(mSilentSignIn || isNetworkAvailable()) {
                mSilentSignIn = false;
            } else {
                onCouldNotSignIn();
            }
        }
    }

    //for silent google sign in
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
