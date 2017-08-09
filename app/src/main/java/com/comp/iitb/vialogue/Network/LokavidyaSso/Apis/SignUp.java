package com.comp.iitb.vialogue.Network.LokavidyaSso.Apis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.comp.iitb.vialogue.Network.LokavidyaSso.ApiStrings;
import com.comp.iitb.vialogue.Network.NetworkCalls;
import com.comp.iitb.vialogue.coordinators.OnDoneSignIn;

import org.json.JSONException;
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
        COULD_NOT_SIGN_UP,
        SOMETHING_WENT_WRONG
    }

    public static class SignUpResponse {

        private Response mResponse;
        private SignUpResponseType mResponseType;
        private String mResponseString;

        JSONObject responseBody = null;
        private String mUniqueId;
        int responseCode;

        public SignUpResponse(Response response, RegistrationType registrationType) {
            mResponse = response;
            System.out.println("response:" +mResponse);
            if(mResponse == null) {
                System.out.println("response is null");
                mResponseType = SignUpResponseType.NETWORK_ERROR;
                mResponseString = "Could not Sign Up, please check your network connection";
                return;
            }

            switch(mResponse.code()) {
                case(200): {
                    // successful
                    try {
                        responseBody = new JSONObject(response.body().string());
                        //responseCode = Integer.parseInt(responseBody.getString("status"));
                        //System.out.println("rcode:" +responseCode);
                        //System.out.println("rcode1:" + mResponse.code());
                        mUniqueId = responseBody.getString("uuid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mResponseType = SignUpResponseType.SOMETHING_WENT_WRONG;
                        mResponseString = "Something went wrong";
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        mResponseType = SignUpResponseType.SOMETHING_WENT_WRONG;
                        mResponseString = "Something went wrong";
                        return;
                    }
                    System.out.println("success:");
                    mResponseType = SignUpResponseType.USER_SIGNED_UP;
                    mResponseString = registrationType == RegistrationType.PHONE_NUMBER ? "Please enter the otp we have sent" : "Please check your email and activate your email address";
                    break;
                } default: {
                    // user already exists
                    System.out.println("default:");
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

        public String getUniqueId() { return mUniqueId; }
    }

    public static SignUpResponse signUp(Context context, String userName, RegistrationType registrationType, String registrationData, String password, String uniqueId) {
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
            if(uniqueId != null) {
                user.put("uuid", uniqueId);
            }
            body.put("user", user);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return new SignUpResponse(null, null);
        }

        // send POST request
        try {
            return new SignUpResponse(new NetworkCalls().doPostRequest(ApiStrings.getSignUpApi(), body.toString()), registrationType);
        } catch (IOException e) {
            e.printStackTrace();
            return new SignUpResponse(null, null);
        }
    }

    public static void signUpInBackground(Context context, String userName, SignUp.RegistrationType registrationType, String registrationData, String password, String uniqueId, OnDoneSignIn onDoneSignIn) {
        (new AsyncTask<Void, Void, Void>() {
            private SignUp.SignUpResponse mSignUpResponse;
            ProgressDialog asyncDialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage("Please Wait..");
                asyncDialog.show();
                super.onPreExecute();
            }

            @Override
            public Void doInBackground(Void... params) {
                mSignUpResponse = signUp(context, userName, registrationType, registrationData, password, uniqueId);
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
