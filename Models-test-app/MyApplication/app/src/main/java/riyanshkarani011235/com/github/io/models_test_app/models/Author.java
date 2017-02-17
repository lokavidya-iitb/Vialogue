package riyanshkarani011235.com.github.io.models_test_app.models;

import android.util.Log;

import com.google.gson.Gson;
import com.parse.ParseClassName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import riyanshkarani011235.com.github.io.models_test_app.models.json.AuthorJson;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Author")
public class Author extends BaseParseClass {

    private AuthorJson mAuthorJson;
    private String mFirstName;
    private String mLastName;
    private String mEmail;

    // default constructor required by Parse
    public Author() {}

//    // from JSON string
//    public Author(String json) throws org.json.JSONException {
//        this(new Gson().fromJson(json, AuthorJson.class));
//    }
//
//    // from AuthorJson Instance
//    public Author(AuthorJson authorJson) throws org.json.JSONException {
//        mAuthorJson = authorJson;
//        mFirstName = authorJson.getFirstName();
//        mLastName = authorJson.getLastName();
//        mEmail = authorJson.getEmail();
//        init();
//    }

    // implementing the interface
    public AuthorJson getJsonObject() {
        return mAuthorJson;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    @Override
    public String toString() {
        return mAuthorJson.toString();
    }
}
