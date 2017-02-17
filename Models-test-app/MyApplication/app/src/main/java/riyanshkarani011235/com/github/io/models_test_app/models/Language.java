package riyanshkarani011235.com.github.io.models_test_app.models;

import com.google.gson.Gson;
import com.parse.ParseClassName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import riyanshkarani011235.com.github.io.models_test_app.models.json.LanguageJson;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Language")
public class Language extends BaseParseClass {

    private LanguageJson mLanguageJson;
    private String mName;

    // default constructor required by Parse
    public Language() {}

//    // from Json String
//    public Language(String json) throws org.json.JSONException {
//        this(new Gson().fromJson(json, LanguageJson.class));
//    }
//
//    public Language(LanguageJson languageJson) throws org.json.JSONException {
//        mLanguageJson = languageJson;
//        mName = languageJson.getName();
//        init();
//    }

    // implement interface
    public LanguageJson getJsonObject() {
        return mLanguageJson;
    }

    @Override
    public String toString() {
        return mLanguageJson.toString();
    }
}
