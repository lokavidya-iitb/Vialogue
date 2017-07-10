package com.comp.iitb.vialogue.Network.LokavidyaSso.Apis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.comp.iitb.vialogue.Network.LokavidyaSso.ApiStrings;
import com.comp.iitb.vialogue.Network.NetworkCalls;
import com.comp.iitb.vialogue.coordinators.OnDoneSignIn;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by ironstein on 25/05/17.
 */

public class SignUp {

    public static enum RegistrationType {
        PHONE_NUMBER,
        EMAIL_ID
    }

    public static enum SignUpResponseType {
        USER_ALREADY_EXISTS,
        USER_SIGNED_UP,
        NETWORK_ERROR,
        COULD_NOT_SIGN_UP
    }

    public static class SignUpResponse {

        private Response mResponse;
        private SignUpResponseType mResponseType;
        private String mResponseString;

        public SignUpResponse(Response response) {
            mResponse = response;
            if(mResponse == null) {
                mResponseType = SignUpResponseType.NETWORK_ERROR;
                mResponseString = "Could not Sign Up, please check your network connection";
                return;
            }

            switch(mResponse.code()) {
                case(200): {
                    // successful
                    mResponseType = SignUpResponseType.USER_SIGNED_UP;
                    mResponseString = "User Successfully Signed Up";
                    break;
                } default: {
                    // user already exists
                    mResponseType = SignUpResponseType.USER_ALREADY_EXISTS;
                    mResponseString = "A User with these credentials already exists";
                    break;
                }
            }
        }

        public SignUpResponseType getResponseType() {
            return mResponseType;
        }

        public String getResponseString() {
            return mResponseString;
        }
    }

    public static SignUpResponse signUp(Context context, String userName, RegistrationType registrationType, String registrationData, String password) {
        JSONObject user;
        JSONObject body;
        Response response;

        // create body JSON object
        try {
            user = new JSONObject();
            body = new JSONObject();
            user.put("name", userName);
            if(registrationType == RegistrationType.EMAIL_ID) {
                user.put("phone", null);
                user.put("email", registrationData);
            } else if(registrationType == RegistrationType.PHONE_NUMBER) {
                user.put("email", null);
                user.put("phone", registrationData);
            }
            user.put("password", password);
            user.put("password_confirmation", password);
            body.put("user", user);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return new SignUpResponse(null);
        }

        // send POST request
        try {
            return new SignUpResponse(new NetworkCalls().doPostRequest(ApiStrings.getSignUpApi(), body.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return new SignUpResponse(null);
        }
    }

    public static void signUpInBackground(Context context, String userName, RegistrationType registrationType, String registrationData, String password, OnDoneSignIn onDoneSignIn) {
        (new AsyncTask<Void, Void, Void>() {
            private SignUpResponse mSignUpResponse;
            ProgressDialog asyncDialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage("Please Wait..");
                asyncDialog.show();
                super.onPreExecute();
            }

            @Override
            public Void doInBackground(Void... params) {
                mSignUpResponse = signUp(context, userName, registrationType, registrationData, password);
                return null;
            }

            @Override
            public void onPostExecute(Void result) {
                asyncDialog.dismiss();
                System.out.println(mSignUpResponse);
                onDoneSignIn.done(mSignUpResponse);
            }

        }).execute();
    }

}
