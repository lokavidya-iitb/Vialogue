package com.comp.iitb.vialogue.Network.LokavidyaSso.Apis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.comp.iitb.vialogue.Network.LokavidyaSso.ApiStrings;
import com.comp.iitb.vialogue.Network.LokavidyaSso.SharedPreferencesDetails;
import com.comp.iitb.vialogue.Network.NetworkCalls;
import com.comp.iitb.vialogue.coordinators.OnDoneLogIn;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by ironstein on 25/05/17.
 */

public class LogIn {

    public static enum RegistrationType {
        PHONE_NUMBER,
        EMAIL_ID
    }

    public static enum LogInResponseType {
        LOGGED_IN,
        PASSWORD_DOES_NOT_MATCH,
        USER_NOT_ACTIVE,
        SOMETHING_WENT_WRONG,
        NETWORK_ERROR
    }

    public static class LogInResponse {
        private Response mResponse;
        private LogInResponseType mResponseType;
        private String mResponseString;
        private String mSessionToken;
        private String mSessionUuid;

        public LogInResponse(Response response) {
            mResponse = response;
            mSessionToken = null;
            mSessionUuid = null;
            if(mResponse == null) {
                mResponseType = LogInResponseType.NETWORK_ERROR;
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
                mResponseType = LogInResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            } catch (IOException e) {
                e.printStackTrace();
                mResponseType = LogInResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            }

            switch(responseCode) {
                case(200): {
                    mResponseType = LogInResponseType.LOGGED_IN;
                    mResponseString = "User Successfully logged in";
                    try {
                        mSessionToken = responseBody.getString(SharedPreferencesDetails.SESSION_TOKEN_KEY);
                        mSessionUuid = responseBody.getString(SharedPreferencesDetails.SESSION_UUID_KEY);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mResponseType = LogInResponseType.SOMETHING_WENT_WRONG;
                        mResponseString = "Something went wrong";
                        return;
                    }
                    break;
                } case(401): {
                    mResponseType = LogInResponseType.PASSWORD_DOES_NOT_MATCH;
                    mResponseString = "Credentials do not match";
                    break;
                } case(422): {
                    mResponseType = LogInResponseType.USER_NOT_ACTIVE;
                    mResponseString = "This account has not been activated yet";
                    break;
                } case(406): {
                    mResponseType = LogInResponseType.SOMETHING_WENT_WRONG;
                    mResponseString = "Something went wrong";
                    break;
                } default: {
                    mResponseType = LogInResponseType.NETWORK_ERROR;
                    mResponseString = "Could not Log In, please check your network connection";
                    break;
                }
            }
        }

        public LogInResponseType getResponseType() {
            return mResponseType;
        }

        public String getResponseString() {
            return mResponseString;
        }

        public String getSessionToken() {
            return mSessionToken;
        }

        public String getSessionUuid() {
            return mSessionUuid;
        }
    }

    public static LogInResponse logIn(Context context, RegistrationType registrationType, String registrationData, String password) {
        JSONObject user;
        JSONObject body;
        Response response;

        // create body JSON object
        try {
            user = new JSONObject();
            body = new JSONObject();
            if(registrationType == RegistrationType.EMAIL_ID) {
                user.put("email", registrationData);
            } else if(registrationType == RegistrationType.PHONE_NUMBER) {
                user.put("phone", registrationData);
            }
            user.put("password", password);
            body.put("user", user);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return new LogInResponse(null);
        }

        // send POST request
        try {
            response = new NetworkCalls().doPostRequest(ApiStrings.getLoginApi(), body.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new LogInResponse(null);
        }

        // handle request response
        LogInResponse logInResponse = new LogInResponse(response);
        saveSessionDetails(context, logInResponse);
        return logInResponse;
    }

    public static void logInInBackground(Context context, RegistrationType registrationType, String registrationData, String password, OnDoneLogIn onDoneLogIn) {
        (new AsyncTask<Void, Void, Void>() {
            private LogInResponse mLogInResponse;
            ProgressDialog asyncDialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage("Please Wait..");
                asyncDialog.show();
                super.onPreExecute();
            }

            @Override
            public Void doInBackground(Void... params) {
                mLogInResponse = logIn(context, registrationType, registrationData, password);
                return null;
            }

            @Override
            public void onPostExecute(Void result) {
                asyncDialog.dismiss();
                //System.out.println(mLogInResponse);
                onDoneLogIn.done(mLogInResponse);
            }

        }).execute();
    }

    public static void saveSessionDetails(Context context, LogInResponse logInResponse) {
        if(logInResponse.getResponseType() == LogInResponseType.LOGGED_IN) {
            System.out.println("saving session details to Shared Preferences");
            SharedPreferences lokavidyaSsoSharedPreferences = context.getSharedPreferences(SharedPreferencesDetails.SHARED_PREFERENCES_NAME, 0);
            SharedPreferences.Editor editor = lokavidyaSsoSharedPreferences.edit();
            editor.putString(SharedPreferencesDetails.SESSION_TOKEN_KEY, logInResponse.getSessionToken());
            editor.putString(SharedPreferencesDetails.SESSION_UUID_KEY, logInResponse.getSessionUuid());
            editor.putBoolean(SharedPreferencesDetails.IS_LOGGED_IN_KEY, true);
            editor.commit();
        }
    }

}
