package com.comp.iitb.vialogue;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.comp.iitb.vialogue.activity.CameraActivity;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Audio;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Video;
import com.comp.iitb.vialogue.service.ClosingService;
import com.parse.Parse;
import com.parse.ParseObject;

import com.comp.iitb.vialogue.models.ParseObjects.models.Author;
import com.comp.iitb.vialogue.models.ParseObjects.models.Category;
import com.comp.iitb.vialogue.models.ParseObjects.models.Language;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;


/**
 * Created by ironstein on 13/02/17.
 */

public class App extends Application implements IAdobeAuthClientCredentials {

    // constants
    private static final String CREATIVE_SDK_CLIENT_ID      = "f71339a291b841acbc45c18cf50ab10c";
    private static final String CREATIVE_SDK_CLIENT_SECRET  = "5ef66814-7fd7-4063-ab3e-8fadefd46ab3";
    private static final String CREATIVE_SDK_REDIRECT_URI   = "ams+2c0aaf828a578c32b47110b21fdd7300961250cd://adobeid/f71339a291b841acbc45c18cf50ab10c";
    private static final String[] CREATIVE_SDK_SCOPES       = {"email", "profile", "address"};
    private static final String appId = "wpFAVTgYHZSrmGRFXzPwXZrBjE4btFgNYzOV";
//    private static final String serverUrl = "https://lokavidya-heroku-server.herokuapp.com/parse";
//    private static final String serverUrl = "https://10.196.31.255:5000/parse";
//    private static final String serverUrl = "http://best-erp.com/lokavidya/parse";
//    private static final String serverUrl = "http://192.168.1.100:27017/parse";
    private static final String serverUrl = "http://54.218.78.174:5000/parse";

    @Override
    public void onCreate() {
        super.onCreate();

        // start service that will save the current project
        // whenever the app is stopped, no matter in what fashion
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
        startService(new Intent(getApplicationContext(), ClosingService.class));

        // register parse Subclasses
        ParseObject.registerSubclass(Project.class);
        ParseObject.registerSubclass(Slide.class);
        ParseObject.registerSubclass(ParseObjectsCollection.class);
        ParseObject.registerSubclass(Author.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Language.class);
        ParseObject.registerSubclass(Question.class);
        ParseObject.registerSubclass(Question.QuestionType.class);
        ParseObject.registerSubclass(Image.class);
        ParseObject.registerSubclass(Audio.class);
        ParseObject.registerSubclass(Video.class);

        Parse.enableLocalDatastore(getBaseContext());

        // setup Parse
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId(appId)
            .server(serverUrl)
            .enableLocalDataStore()
            .build()
        );
    }



    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String[] getAdditionalScopesList() {
        return CREATIVE_SDK_SCOPES;
    }

    @Override
    public String getRedirectURI() {
        return CREATIVE_SDK_REDIRECT_URI;
    }
}
