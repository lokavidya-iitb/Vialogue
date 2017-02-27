package com.comp.iitb.vialogue;

import android.app.Application;

import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Audio;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Video;
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

public class App extends Application {

    @Override
    public void onCreate() {

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

        // setup Parse
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId(appId)
            .server(serverUrl)
            .enableLocalDataStore()
            .build()
        );

    }
}
