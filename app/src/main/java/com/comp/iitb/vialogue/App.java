package com.comp.iitb.vialogue;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.IGoogleClientBilling;
import com.adobe.creativesdk.aviary.utils.AdobeImageEditorIntentConfigurationValidator;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.comp.iitb.vialogue.activity.CameraActivity;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Audio;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Video;
import com.comp.iitb.vialogue.service.ClosingService;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import com.comp.iitb.vialogue.models.ParseObjects.models.Author;
import com.comp.iitb.vialogue.models.ParseObjects.models.Category;
import com.comp.iitb.vialogue.models.ParseObjects.models.Language;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.parse.SaveCallback;


/**
 * Created by ironstein on 13/02/17.
 */

public class App extends Application implements IAdobeAuthClientCredentials, IGoogleClientBilling {

    // constants
    private static final String CREATIVE_SDK_CLIENT_ID      = "2898ebd674af4c39884a33e2167ce989";
    private static final String CREATIVE_SDK_CLIENT_SECRET  = "6004eafa-73dd-49fb-b5b4-0f40d55927f8";
    private static final String CREATIVE_SDK_REDIRECT_URI   = "ams+6e78a51ceeacace4863810e2770b3855f74a403a://adobeid/2898ebd674af4c39884a33e2167ce989";
    private static final String[] CREATIVE_SDK_SCOPES       = {"email", "profile", "address"};
    private static final String appId = "wpFAVTgYHZSrmGRFXzPwXZrBjE4btFgNYzOV";
//    private static final String serverUrl = "https://lokavidya-heroku-server.herokuapp.com/parse";
//    private static final String serverUrl = "https://10.196.2.173:5000/parse";
//    private static final String serverUrl = "http://best-erp.com/lokavidya/parse";
//    private static final String serverUrl = "http://192.168.1.100:27017/parse";
    private static final String serverUrl = "http://54.218.78.174:5000/parse"; // EC2 instance
    //private static final String serverUrl = "http://192.168.43.83:5000/parse";

    public static String deviceToken;

    @Override
    public void onCreate() {
        super.onCreate();

        // start service that will save the current project
        // whenever the app is stopped, no matter in what fashion

        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());

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

        //ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                deviceToken = (String) ParseInstallation.getCurrentInstallation().get("deviceToken");
                System.out.println("deviceToken: " +deviceToken);
            }
        });

        Intent cdsIntent = AdobeImageIntent.createCdsInitIntent(getBaseContext(), "CDS");
        startService(cdsIntent);

        startService(new Intent(getApplicationContext(), ClosingService.class));

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

    @Override
    public String getBillingKey() {
        return "";
    }
}
