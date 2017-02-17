package riyanshkarani011235.com.github.io.models_test_app.models;

import com.google.gson.Gson;
import com.parse.ParseClassName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import riyanshkarani011235.com.github.io.models_test_app.models.json.ProjectJson;
import riyanshkarani011235.com.github.io.models_test_app.models.json.ResourceJson;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Resource")
public class Resource extends BaseParseClass {

    private ResourceJson mResourceJson;
    private String mType;
    private String mUrl;

    // default constructor required by Parse
    public Resource() {}

//    // from JSON String
//    public Resource(String json) throws org.json.JSONException {
//        this(new Gson().fromJson(json, ResourceJson.class));
//    }
//
//    // from ResourceJson instance
//    public Resource(ResourceJson resourceJson) throws org.json.JSONException {
//        mResourceJson = resourceJson;
//        mType = resourceJson.getType();
//        mUrl = resourceJson.getUrl();
//        init();
//    }

    // implement interface
    public ResourceJson getJsonObject() {
        return mResourceJson;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return mResourceJson.toString();
    }

}
