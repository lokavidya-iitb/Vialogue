package com.comp.iitb.vialogue.Network.LokavidyaSso.Apis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.comp.iitb.vialogue.Network.LokavidyaSso.ApiStrings;
import com.comp.iitb.vialogue.Network.NetworkCalls;
import com.comp.iitb.vialogue.coordinators.OnDoneVerifyOtp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by omkar on 22/7/17.
 */

public class VerifyOtp {

    public static enum VerifyOtpResponseType {
        OTP_VERIFIED_SUCCESSFULLY,
        INCORRECT_OTP,
        NETWORK_ERROR,
        SOMETHING_WENT_WRONG
    }

    public static class VerifyOtpResponse {
        private Response mResponse;
        private VerifyOtpResponseType mResponseType;
        private String mResponseString;
        private String mUniqueId;
        JSONObject responseBody = null;
        int responseCode;

        public VerifyOtpResponse(Response response) {
            mResponse = response;
            System.out.println(response);
            if(mResponse == null) {
                mResponseType = VerifyOtpResponseType.NETWORK_ERROR;
                mResponseString = "Could not verify otp, please check your network connection";
                return;
            }

            try {
                responseBody = new JSONObject(response.body().string());
                responseCode = Integer.parseInt(responseBody.getString("status"));

            } catch (JSONException e) {
                e.printStackTrace();
                mResponseType = VerifyOtpResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            } catch (IOException e) {
                e.printStackTrace();
                mResponseType = VerifyOtpResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            }

            switch(responseCode) {
                case(200): {
                    // successfull
                    try {
                        mUniqueId = responseBody.getString("uuid");
                        System.out.println("uuid" + mUniqueId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mResponseType = VerifyOtpResponseType.SOMETHING_WENT_WRONG;
                        mResponseString = "Something went wrong";
                        return;
                    }
                    mResponseType = VerifyOtpResponseType.OTP_VERIFIED_SUCCESSFULLY;
                    mResponseString = "User successfully signed up";
                    break;
                } case(404): {
                    mResponseType = VerifyOtpResponseType.INCORRECT_OTP;
                    mResponseString = "Please enter the otp we have sent";
                    break;
                } default: {
                    mResponseType = VerifyOtpResponseType.NETWORK_ERROR;
                    mResponseString = "Could not verify otp, please check your network connection";
                    break;
                }
            }
        }

        public VerifyOtpResponseType getResponseType() {
            return mResponseType;
        }

        public String getResponseString() {
            return mResponseString;
        }

        public String getUniqueId() { return mUniqueId; }
    }

    public static VerifyOtpResponse verifyOtp(Context context, String otp) {
        JSONObject user;
        JSONObject body;
        Response response;

        // create body JSON object
        try {
            user = new JSONObject();
            body = new JSONObject();
            //user.put("uniqueId", uniqueId);
            user.put("otp", otp);
            body.put("user", user);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return new VerifyOtpResponse(null);
        }

        // send POST request
        try {
            return new VerifyOtpResponse(new NetworkCalls().doPostRequest(ApiStrings.getVerifyOtpApi(), body.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return new VerifyOtpResponse(null);
        }
    }

    public static void verifyOtpInBackground(Context context, String otp, OnDoneVerifyOtp onDoneVerifyOtp) {
        (new AsyncTask<Void, Void, Void>() {
            private VerifyOtpResponse mVerifyOtpResponse;
            ProgressDialog asyncDialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage("Verifying Otp\nPlease Wait..");
                asyncDialog.show();
                super.onPreExecute();
            }

            @Override
            public Void doInBackground(Void... params) {
                mVerifyOtpResponse = verifyOtp(context, otp);
                return null;
            }

            @Override
            public void onPostExecute(Void result) {
                asyncDialog.dismiss();
                System.out.println(mVerifyOtpResponse);
                onDoneVerifyOtp.done(mVerifyOtpResponse);
            }

        }).execute();
    }

}
