package com.comp.iitb.vialogue.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.helpers.SharedPreferenceHelper;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.List;


public class SignIn extends AppCompatActivity implements
        View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,
        GoogleApiClient.OnConnectionFailedListener {
    private String personName;
    private String personPhotoUrl;
    private String email;
    private static final String TAG = SignIn.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    public static GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;
    private Button skip;
    private SliderLayout mDemoSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                /*for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
                    Toast.makeText(getApplicationContext(),"It is required",Toast.LENGTH_LONG).show();
                    Intent loop = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(loop);
                }*/

                }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Dramatic farmer #1",R.drawable.image2);
        file_maps.put("Dramatic farmer #2",R.drawable.image1);
        file_maps.put("Dramatic farmer #3",R.drawable.image3);
        file_maps.put("Dramatic farmer #4", R.drawable.image4);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        skip = (Button) findViewById(R.id.skip);
        btnSignIn.setOnClickListener(this);
        skip.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public static void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            personName = acct.getDisplayName();
            try {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }
            catch (NullPointerException e)
            {
                personPhotoUrl="";
                e.printStackTrace();
            }
            email = acct.getEmail();
            Log.e(TAG, "Name: " + personName + ", email: " + email+ ", Image: " + personPhotoUrl);
            savetoParse(personName,email);
            updateUI(true);

        } else {

            updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.skip:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                Toast.makeText(SignIn.this,
                        "Sign In to upload your projects.", Toast.LENGTH_SHORT).show();
                break;
        }
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
    public void onStart() {
        super.onStart();

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
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
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

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            SharedPreferenceHelper help = new SharedPreferenceHelper(getApplicationContext());
            try {
                help.saveToSharedPref(Master.personName,personName);
                help.saveToSharedPref(Master.email,email);
                help.saveToSharedPref(Master.personPhotoUrl,personPhotoUrl);
                help.saveToSharedPref(Master.signedOrNot,true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(SignIn.this,
                    "Signed in as "+ personName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferenceHelper help = new SharedPreferenceHelper(getApplicationContext());
            try {
                help.saveToSharedPref(Master.personName,"");
                help.saveToSharedPref(Master.email,"");
                help.saveToSharedPref(Master.personPhotoUrl,"");
                help.saveToSharedPref(Master.signedOrNot,false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(SignIn.this,
                    "Signed in as "+ personName, Toast.LENGTH_SHORT).show();
            btnSignIn.setVisibility(View.VISIBLE);

            /*Toast.makeText(SignIn.this,
                    "Howzaat! Caught an exception from Google", Toast.LENGTH_SHORT).show();*/

        }
    }


    private void savetoParse(String name, String email) {


        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword("some");
        user.put("name", name);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {

                if (e != null) {
/*
                    Toast.makeText(SignIn.this,
                            "Saving user failed.", Toast.LENGTH_SHORT).show();*/
                    Log.w(TAG,
                            "Error : " + e.getMessage() + ":::" + e.getCode());

                    if (e.getCode() == 202) {
                       /* Snackbar.make(get, "Signed in as "+ Master.personName, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();*/

                       /* Toast.makeText(
                                SignIn.this,
                                "Username already taken. \n Please choose another username.",
                                Toast.LENGTH_LONG).show();*/

                    }

                } else {

                    Toast.makeText(SignIn.this, "You're new!",
                            Toast.LENGTH_SHORT).show();
                   /* Snackbar.make(getCurrentFocus(), "Signed in as "+ Master.personName, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/



                }

            }
        });


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
}
