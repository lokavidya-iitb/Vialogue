package com.comp.iitb.vialogue.Network.LokavidyaSso.Apis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.comp.iitb.vialogue.MainActivity;
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
        NETWORK_ERROR,
        USER_DOES_NOT_EXIST
    }

    public static class LogInResponse {
        private Response mResponse;
        private LogInResponseType mResponseType;
        private String mResponseString;
        private String mSessionToken;
        private String mSessionUuid;
        private String mSessionName;

        public LogInResponse(Response response) {
            mResponse = response;
            System.out.println(response);
            mSessionToken = null;
            mSessionUuid = null;
            mSessionName = null;
            if(mResponse == null) {
                System.out.println("could not login 1");
                mResponseType = LogInResponseType.NETWORK_ERROR;
                mResponseString = "Could not Log In, please check your network connection";
                return;
            }

            JSONObject responseBody = null;
            int responseCodey = 0;
            try {
                responseBody = new JSONObject(response.body().string());
                responseCodey = Integer.parseInt(responseBody.getString("status"));
                System.out.println("resbody: " +responseBody);
                System.out.println("res: " +responseCodey);
            } catch (JSONException e) {
                System.out.println("catch1");
                e.printStackTrace();
                mResponseType = LogInResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            } catch (IOException e) {
                System.out.println("catch2");
                e.printStackTrace();
                mResponseType = LogInResponseType.SOMETHING_WENT_WRONG;
                mResponseString = "Something went wrong";
                return;
            }

            switch(responseCodey) {
                case(200): {
                    mResponseType = LogInResponseType.LOGGED_IN;
                    mResponseString = "User Successfully logged in";
                    try {
                        mSessionToken = responseBody.getString(SharedPreferencesDetails.SESSION_TOKEN_KEY);
                        mSessionUuid = responseBody.getString(SharedPreferencesDetails.SESSION_UUID_KEY);
                        mSessionName = responseBody.getString(SharedPreferencesDetails.SESSION_NAME);
                        System.out.println("session_name:" +mSessionName);
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
                } case(404) : {
                    mResponseType = LogInResponseType.USER_DOES_NOT_EXIST;
                    mResponseString = "User does not exist, please register to login";
                    break;
                } default: {
                    System.out.println("res:" + responseCodey);
                    System.out.println("could not login 2");
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

        public String getSessionName() { return mSessionName; }
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
            System.out.println("registrationdata: " +registrationData);
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
            editor.putString(SharedPreferencesDetails.SESSION_NAME, logInResponse.getSessionName());
            editor.putBoolean(SharedPreferencesDetails.IS_LOGGED_IN_KEY, true);
            editor.apply();
            MainActivity.mIsLoggedIn = true;
        }
    }

}
