package com.comp.iitb.vialogue.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.ForgotPassword;
import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.LogIn;
import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.SignUp;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.LoginActivity;
import com.comp.iitb.vialogue.coordinators.OnDoneForgotPassword;
import com.comp.iitb.vialogue.coordinators.OnDoneLogIn;
import com.comp.iitb.vialogue.coordinators.OnDoneSignIn;
import com.comp.iitb.vialogue.coordinators.OnDoneCallingSsoApiResult;
import com.comp.iitb.vialogue.dialogs.VerifyOtpDialogue;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by omkar on 22/7/17.
 */

public class SsoMethods {

    String mUniqueId = null;
    OnDoneCallingSsoApiResult mOnDoneCallingSsoApiResult;
    String mResponseString;
    Bundle info;
    boolean user_exists = false;
    boolean otp_sent = false;
    private static final String PHONE_NUMBER_REGEX = "(^)(?:\\+91)([\\d]){10}$";


    public SsoMethods(OnDoneCallingSsoApiResult onDoneCallingSsoApiResult){
        mOnDoneCallingSsoApiResult = onDoneCallingSsoApiResult;
    }

    public SsoMethods() {}

    public void signUp(Context context, String userName, SignUp.RegistrationType registrationType, String registrationData, String password, String uniqueId) {
        SignUp.signUpInBackground(
                context,
                userName,
                registrationType,
                registrationData,
                password,
                uniqueId,
                new OnDoneSignIn() {
                    @Override
                    public void done(SignUp.SignUpResponse signUpResponse) {
                        switch (signUpResponse.getResponseType()) {
                            case USER_SIGNED_UP:
                                mResponseString = signUpResponse.getResponseString();
                                mUniqueId = signUpResponse.getUniqueId();
                            case USER_ALREADY_EXISTS:
                                mResponseString = signUpResponse.getResponseString();
                                user_exists = true;
                            case COULD_NOT_SIGN_UP:
                                mResponseString = signUpResponse.getResponseString();
                            case NETWORK_ERROR:
                                mResponseString = signUpResponse.getResponseString();
                        }
                        Toast.makeText(context, mResponseString, Toast.LENGTH_LONG).show();
                        System.out.println("signUpResponse:" + signUpResponse);
                        info = null;
                        info  = new Bundle();
                        info.putString(context.getResources().getString(R.string.uniqueId), mUniqueId);
                        if(registrationData.matches(PHONE_NUMBER_REGEX))
                            info.putString(context.getResources().getString(R.string.registrationType), context.getResources().getString(R.string.phoneNo));
                        else
                            info.putString(context.getResources().getString(R.string.registrationType), context.getResources().getString(R.string.email));
                        info.putString("responseString", mResponseString);
                        info.putBoolean("user_exists", user_exists);
                        mOnDoneCallingSsoApiResult.onDone(info);
                    }
                });
    }

    public void login(Context context, LogIn.RegistrationType registrationType, String registrationData, String password) {

        LogIn.logInInBackground(context, registrationType, registrationData, password, new OnDoneLogIn() {
            @Override
            public void done(LogIn.LogInResponse logInResponse) {
                switch (logInResponse.getResponseType()) {
                    case LOGGED_IN:
                        mResponseString = logInResponse.getResponseString();
                        info = null;
                        mOnDoneCallingSsoApiResult.onDone(info);
                    case PASSWORD_DOES_NOT_MATCH:
                        mResponseString = logInResponse.getResponseString();
                    case USER_NOT_ACTIVE:
                        mResponseString = logInResponse.getResponseString();
                    case SOMETHING_WENT_WRONG:
                        mResponseString = logInResponse.getResponseString();
                    case NETWORK_ERROR:
                        mResponseString = logInResponse.getResponseString();
                    case USER_DOES_NOT_EXIST:
                        mResponseString = logInResponse.getResponseString();
                }
                Toast.makeText(context, mResponseString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void forgotPassword(Context context, ForgotPassword.RegistrationType registrationType, String registrationData) {
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
                                otp_sent = true;
                                //new VerifyOtpDialogue(context, args, 1).show();
                            case USER_NOT_REGISTERED:
                                mResponseString = response.getResponseString();
                            case NETWORK_ERROR:
                                mResponseString = response.getResponseString();
                            case SOMETHING_WENT_WRONG:
                                mResponseString = response.getResponseString();
                        }
                        Toast.makeText(context, mResponseString, Toast.LENGTH_SHORT).show();
                        info = null;
                        info  = new Bundle();
                        info.putBoolean("otp_sent", otp_sent);
                        mOnDoneCallingSsoApiResult.onDone(info);
                    }
                }
        );
    }

    public void signUpUsingParse(String registrationData, String userName, String password) {

        ParseUser user = new ParseUser();
        if(registrationData.matches(PHONE_NUMBER_REGEX)) {
            user.put("phone_number", registrationData);
        } else {
            user.setEmail(registrationData);
        }
        user.setUsername(userName);
        user.setPassword(password);
        // Try signing up th user
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // ERROR SIGNING UP
                    switch(e.getCode()) {
                        case ParseException.USERNAME_TAKEN :
                            // User already exists in the database, so just log him in
                            user.logInInBackground(userName, password, new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {

                                }
                            });

                            System.out.println("parse login");
                            break;
                        default:
                            // some other error
                            System.out.println("getmess" +e.getMessage());
                            System.out.println("getcode" +e.getCode());
                            //onCouldNotSignIn();
                            System.out.println("default");
                            break;
                    }
                } else {
                    //onSignedIn();
                    System.out.println("parse signup");
                }

            }
        });
    }

}
