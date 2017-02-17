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

    private static class Fields {
        public static final String

        PROJECT_SLIDE_ID = "project_slide_id",
        HYPERLINKS = "hyperlinks";
    }

    // default constructor required by Parse
    public Slide() {}

    public Integer getProjectSlideId() {
        return getInt(Fields.PROJECT_SLIDE_ID);
    }

    public void setProjectSlideId(Integer projectSlideId) {
        put(Fields.PROJECT_SLIDE_ID, projectSlideId);
    }

    public List<String> getHyperlinks() {
        return getList(Fields.HYPERLINKS);
    }

    public void setHyperlinks(List<String> hyperlinks) {
        put(Fields.HYPERLINKS, hyperlinks);
    }

}
