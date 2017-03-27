package com.comp.iitb.vialogue.ExperimentingParse;

/**
 * Created by jeffrey on 16/2/17.
 */
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import android.app.Application;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("wpFAVTgYHZSrmGRFXzPwXZrBjE4btFgNYzOV")
                .clientKey("knkn;")
                .server("https://lokavidya-heroku-server.herokuapp.com/parse")
                .build()
        );

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);

       ParseACL.setDefaultACL(defaultACL, true);
    }

}