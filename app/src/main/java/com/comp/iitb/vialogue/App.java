package com.comp.iitb.vialogue;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

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
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by ironstein on 13/02/17.
 */

public class App extends Application {

//    private RefWatcher refWatcher;

//    public static RefWatcher getRefWatcher(Context context) {
//        App application = (App) context.getApplicationContext();
//        return application.refWatcher;
//    }

    @Override
    public void onCreate() {

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        refWatcher = LeakCanary.install(this);
//        // Normal app init code...
//
//        // start service that will save the current project
//        // whenever the app is stopped, no matter in what fashion
//        startService(new Intent(getApplicationContext(), ClosingService.class));

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

        String appId = "wpFAVTgYHZSrmGRFXzPwXZrBjE4btFgNYzOV";
        String serverUrl = "https://lokavidya-heroku-server.herokuapp.com/parse";

        Parse.enableLocalDatastore(getBaseContext());

        // setup Parse
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId(appId)
            .server(serverUrl)
            .enableLocalDataStore()
            .build()
        );

    }
}
