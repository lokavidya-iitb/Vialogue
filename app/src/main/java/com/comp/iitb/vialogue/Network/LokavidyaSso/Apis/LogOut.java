package com.comp.iitb.vialogue.Network.LokavidyaSso.Apis;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.comp.iitb.vialogue.Network.LokavidyaSso.ApiStrings;
import com.comp.iitb.vialogue.Network.LokavidyaSso.SharedPreferencesDetails;
import com.comp.iitb.vialogue.Network.NetworkCalls;
import com.comp.iitb.vialogue.coordinators.OnDoneLogOut;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by ironstein on 25/05/17.
 */

public class LogOut {

    public static enum LogOutResponseType {
        LOGGED_OUT,
        INCORRECT_SESSION_TOKEN,
        NETWORK_ERROR,
        SOMETHING_WENT_WRONG
    }

    public static class LogOutResponse {

        private Response mResponse;
        private LogOutResponseType mResponseType;
        private String mResponseString;

        public LogOutResponse(Response response) {
            mResponse = response;
            if(response == null) {
                mResponseType = LogOutResponseType.NETWORK_ERROR;
                mResponseString = "Could not Log In, please check your network connection";
                return;
            }

            JSONObject responseBody = null;
            int responseCode;

            try {
                responseBody = new JSONObject(response.body().string());
                responseCode = Integer.parseInt(responseBody.getString("status"));
            } catch (JSONException e) {
                e.printStackTrace();
                mResponseType = LogOutResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            } catch (IOException e) {
                e.printStackTrace();
                mResponseType = LogOutResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            }

            switch(responseCode) {
                case(200): {
                    mResponseType = LogOutResponseType.LOGGED_OUT;
                    mResponseString = "User Successfully logged out";
                    break;
                } case(404): {
                    mResponseType = LogOutResponseType.INCORRECT_SESSION_TOKEN;
                    mResponseString = "IncorrectSessionToken";
                    break;
                } default: {
                    mResponseType = LogOutResponseType.NETWORK_ERROR;
                    mResponseString = "Could not Log In, please check your network connection";
                    break;
                }
            }
        }

        public LogOutResponseType getResponseType() {
            return mResponseType;
        }

        public String getResponseString() {
            return mResponseString;
        }
    }

    public static LogOutResponse logOut(Context context) {
        SharedPreferences lokavidyaSsoSharedPreferences = context.getSharedPreferences(SharedPreferencesDetails.SHARED_PREFERENCES_NAME, 0);
        try {
            LogOutResponse logOutResponse = new LogOutResponse(new NetworkCalls().doGetRequest(ApiStrings.getLogoutApi(lokavidyaSsoSharedPreferences.getString(SharedPreferencesDetails.SESSION_TOKEN_KEY, ""))));
            removeSessionDetails(context, logOutResponse);
            return logOutResponse;
        } catch (IOException e) {
            return new LogOutResponse(null);
        }
    }

    public static void logOutInBackground(Context context, OnDoneLogOut onDoneLogOut) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... params) {
                onDoneLogOut.done(logOut(context));
                return null;
            }
        }).execute();
    }

    public static void removeSessionDetails(Context context, LogOutResponse logOutResponse) {
        if(logOutResponse.getResponseType() == LogOutResponseType.LOGGED_OUT) {
            System.out.println("deleting session details from shared preferences");
            SharedPreferences lokavidyaSsoSharedPreferences = context.getSharedPreferences(SharedPreferencesDetails.SHARED_PREFERENCES_NAME, 0);
            SharedPreferences.Editor editor = lokavidyaSsoSharedPreferences.edit();
            editor.putString(SharedPreferencesDetails.SESSION_TOKEN_KEY, "");
            editor.putString(SharedPreferencesDetails.SESSION_UUID_KEY, "");
            editor.putBoolean(SharedPreferencesDetails.IS_LOGGED_IN_KEY, false);
            editor.commit();
        }
    }

}
