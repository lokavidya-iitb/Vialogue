package com.comp.iitb.vialogue.Network.LokavidyaSso.Apis;

import android.content.Context;
import android.net.Network;
import android.os.AsyncTask;

import com.comp.iitb.vialogue.Network.LokavidyaSso.ApiStrings;
import com.comp.iitb.vialogue.Network.NetworkCalls;
import com.comp.iitb.vialogue.coordinators.OnDoneForgotPassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Response;

/**
 * Created by ironstein on 26/05/17.
 */

public class ForgotPassword {

    public static enum RegistrationType {
        PHONE_NUMBER,
        EMAIL_ID
    }

    public static enum ForgotPasswordResponseType {
        OTP_SENT,
        USER_NOT_REGISTERED,
        NETWORK_ERROR,
        SOMETHING_WENT_WRONG
    }

    public static class ForgotPasswordResponse {

        private Response mResponse;
        private ForgotPasswordResponseType mResponseType;
        private String mResponseString;

        public ForgotPasswordResponse(Response response) {
            mResponse = response;
            if(mResponse == null) {
                mResponseType = ForgotPasswordResponseType.NETWORK_ERROR;
                mResponseString = "Could not Connect to the server, please check your network connection";
                return;
            }

            JSONObject responseBody = null;
            int responseCode;
            try {
                responseBody = new JSONObject(response.body().string());
                responseCode = Integer.parseInt(responseBody.getString("status"));
            } catch (JSONException e) {
                e.printStackTrace();
                mResponseType = ForgotPasswordResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            } catch (IOException e) {
                e.printStackTrace();
                mResponseType = ForgotPasswordResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            }

            System.out.println("forgot password response code : " + responseCode);
            switch (responseCode) {
                case(200): {
                    mResponseType = ForgotPasswordResponseType.OTP_SENT;
                    mResponseString = "An OTP has been sent to your registered mobile number and email address";
                    break;
                } case(304): {
                    mResponseType = ForgotPasswordResponseType.USER_NOT_REGISTERED;
                    mResponseString = "The email ID / phone number entered have not been registered";
                    break;
                } default: {
                    mResponseType = ForgotPasswordResponseType.NETWORK_ERROR;
                    mResponseString = "Could not Connect to the server, please check your network connection";
                    break;
                }
            }
        }

        public Response getResponse() {
            return mResponse;
        }

        public ForgotPasswordResponseType getResponseType() {
            return mResponseType;
        }

        public String getResponseString() {
            return mResponseString;
        }

        public void setResponseType(ForgotPasswordResponseType responseType) {
            mResponseType = responseType;
        }

        public void setResponseString(String responseString) {
            mResponseString = responseString;
        }

        public static ForgotPasswordResponse getNewNullResponse() {
            ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse(null);
            forgotPasswordResponse.setResponseType(ForgotPasswordResponseType.SOMETHING_WENT_WRONG);
            forgotPasswordResponse.setResponseString("Something went wrong");
            return forgotPasswordResponse;
        }
    }

    public static ForgotPasswordResponse forgotPassword(Context context, RegistrationType registrationType, String registrationData) {
        Response response = null;
        String apiString = "";

        try {
            if(registrationType == RegistrationType.EMAIL_ID) {
                apiString = ApiStrings.getForgotPasswordApi(ApiStrings.RegistrationType.EMAIL_ID, registrationData);
            } else if(registrationType == RegistrationType.PHONE_NUMBER) {
                apiString = ApiStrings.getForgotPasswordApi(ApiStrings.RegistrationType.PHONE_NUMBER, registrationData);
            }
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            return ForgotPasswordResponse.getNewNullResponse();
        }
        System.out.println(apiString);

        // send GET request
        try {
            response = new NetworkCalls().doGetRequest(apiString);
            return new ForgotPasswordResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return new ForgotPasswordResponse(null);
        }
    }

    public static void forgotPasswordInBackground(Context context, RegistrationType registrationType, String registrationData, OnDoneForgotPassword onDoneForgotPassword) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... params) {
                onDoneForgotPassword.done(forgotPassword(context, registrationType, registrationData));
                return null;
            }
        }).execute();
    }

}
