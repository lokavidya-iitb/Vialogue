package com.comp.iitb.vialogue.Network.LokavidyaSso.Apis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.comp.iitb.vialogue.Network.LokavidyaSso.ApiStrings;
import com.comp.iitb.vialogue.Network.NetworkCalls;
import com.comp.iitb.vialogue.coordinators.OnDoneResetPassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by ironstein on 26/05/17.
 */

public class ResetPassword {

    public static enum RegistrationType {
        PHONE_NUMBER,
        EMAIL_ID
    }

    public static enum ResetPasswordResponseType {
        PASSWORD_RESET,
        INVALID_OTP,
        COULD_NOT_RESET_PASSWORD,
        NETWORK_ERROR,
        SOMETHING_WENT_WRONG
    }

    public static class ResetPasswordResponse {

        private Response mResponse;
        private ResetPasswordResponseType mResponseType;
        private String mResponseString;

        public ResetPasswordResponse(Response response) {
            mResponse = response;
            if(mResponse == null) {
                mResponseType = ResetPasswordResponseType.NETWORK_ERROR;
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
                mResponseType = ResetPasswordResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            } catch (IOException e) {
                e.printStackTrace();
                mResponseType = ResetPasswordResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            }

            switch (responseCode) {
                case (200): {
                    mResponseType = ResetPasswordResponseType.PASSWORD_RESET;
                    mResponseString = "Password has been reset Successfully";
                    return;
                } case(404): {
                    mResponseType = ResetPasswordResponseType.INVALID_OTP;
                    mResponseString = "Invalid OTP";
                    return;
                } default: {
                    mResponseType = ResetPasswordResponseType.NETWORK_ERROR;
                    mResponseString = "Could not Connect to the server, please check your network connection";
                    return;
                }
            }
        }

        public Response getResponse() {
            return mResponse;
        }

        public ResetPasswordResponseType getResponseType() {
            return mResponseType;
        }

        public String getResponseString() {
            return mResponseString;
        }

        public void setResponseType(ResetPasswordResponseType responseType) {
            mResponseType = responseType;
        }

        public void setResponseString(String responseString) {
            mResponseString = responseString;
        }

        public static ResetPasswordResponse getNewNullResponse() {
            ResetPasswordResponse resetPasswordResponse = new ResetPasswordResponse(null);
            resetPasswordResponse.setResponseType(ResetPasswordResponseType.SOMETHING_WENT_WRONG);
            resetPasswordResponse.setResponseString("Something went wrong");
            return resetPasswordResponse;
        }
    }

    public static ResetPasswordResponse resetPassword(Context context, String uniqueId, String newPassword) {
        String apiString = "";
        JSONObject body;
        JSONObject user;

        try {
            /*if(registrationType == RegistrationType.EMAIL_ID) {
                apiString = ApiStrings.getResetPasswordApi(ApiStrings.RegistrationType.EMAIL_ID, registrationData, otp);
            } else {
                apiString = ApiStrings.getResetPasswordApi(ApiStrings.RegistrationType.PHONE_NUMBER, registrationData, otp);
            }*/
            apiString = ApiStrings.getResetPasswordApi();
        } catch (java.io.UnsupportedEncodingException e) {
            return ResetPasswordResponse.getNewNullResponse();
        }

        // create body JSON object
        try {
            body = new JSONObject();
            user = new JSONObject();
            user.put("uuid", uniqueId);
            user.put("password", newPassword);
            body.put("user", user);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return new ResetPasswordResponse(null);
        }

        System.out.println(body.toString());
        System.out.println(apiString);

        try {
            return new ResetPasswordResponse(new NetworkCalls().doPostRequest(apiString, body.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResetPasswordResponse(null);
        }
    }

    public static void resetPasswordInBackground(Context context, String uniqueId, String newPassword, OnDoneResetPassword onDoneResetPassword) {
        (new AsyncTask<Void, Void, Void>() {
            private ResetPasswordResponse mResetPasswordResponse;
            ProgressDialog asyncDialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage("Please Wait..");
                asyncDialog.show();
                super.onPreExecute();
            }

            @Override
            public Void doInBackground(Void... params) {
                mResetPasswordResponse = resetPassword(context, uniqueId, newPassword);
                return null;
            }

            @Override
            public void onPostExecute(Void result) {
                asyncDialog.dismiss();
                //System.out.println(mLogInResponse);
                onDoneResetPassword.done(mResetPasswordResponse);
            }

        }).execute();
    }

}
