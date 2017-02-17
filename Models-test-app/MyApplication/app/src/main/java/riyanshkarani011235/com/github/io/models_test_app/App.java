package riyanshkarani011235.com.github.io.models_test_app;

import android.app.Application;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseObject;

import riyanshkarani011235.com.github.io.models_test_app.models.Author;
import riyanshkarani011235.com.github.io.models_test_app.models.BaseParseClass;
import riyanshkarani011235.com.github.io.models_test_app.models.Category;
import riyanshkarani011235.com.github.io.models_test_app.models.Language;
import riyanshkarani011235.com.github.io.models_test_app.models.Project;
import riyanshkarani011235.com.github.io.models_test_app.models.Resource;
import riyanshkarani011235.com.github.io.models_test_app.models.Slide;

/**
 * Created by ironstein on 13/02/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {

        ParseObject.registerSubclass(Project.class);
        ParseObject.registerSubclass(Slide.class);
        ParseObject.registerSubclass(Resource.class);
        ParseObject.registerSubclass(Author.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Language.class);

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
