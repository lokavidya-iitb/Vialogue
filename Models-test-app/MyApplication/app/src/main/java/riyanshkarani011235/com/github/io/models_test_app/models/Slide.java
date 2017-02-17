package riyanshkarani011235.com.github.io.models_test_app.models;

import com.google.gson.Gson;
import com.parse.ParseClassName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import riyanshkarani011235.com.github.io.models_test_app.models.json.BaseJsonClass;
import riyanshkarani011235.com.github.io.models_test_app.models.json.SlideJson;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Slide")
public class Slide extends BaseParseClass {

    public SlideJson mSlideJson;
    public Integer mProjectSlideId;
    public List<String> mHyperlinks;

    // default constructor required by Parse
    public Slide() {}

//    // from JSON String
//    public Slide(String json) throws org.json.JSONException {
//        this(new Gson().fromJson(json, SlideJson.class));
//    }
//
//    // from SlideJson instance
//    public Slide(SlideJson slideJson) throws org.json.JSONException {
//        mSlideJson = slideJson;
//        mProjectSlideId = slideJson.getProjectSlideId();
//        mHyperlinks = slideJson.getHyperlinks();
//        init();
//    }

    // implementing interface
    public SlideJson getJsonObject() {
        return mSlideJson;
    }

    public Integer getProjectSlideId() {
        return mProjectSlideId;
    }

    public void setProjectSlideId(Integer projectSlideId) {
        mProjectSlideId = projectSlideId;
    }

    public List<String> getHyperlinks() {
        return mHyperlinks;
    }

    public void setHyperlinks(List<String> hyperlinks) {
        mHyperlinks = hyperlinks;
    }

    @Override
    public String toString() {
        return mSlideJson.toString();
    }

}
