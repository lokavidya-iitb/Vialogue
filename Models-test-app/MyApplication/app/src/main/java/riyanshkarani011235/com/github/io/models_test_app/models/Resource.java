package riyanshkarani011235.com.github.io.models_test_app.models;

import com.google.gson.Gson;
import com.parse.ParseClassName;
import com.parse.ParseFile;

import org.apache.commons.lang3.builder.ToStringBuilder;

import riyanshkarani011235.com.github.io.models_test_app.models.json.ProjectJson;
import riyanshkarani011235.com.github.io.models_test_app.models.json.ResourceJson;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Resource")
public class Resource extends BaseParseClass {

    private static final class Fields {
        public static final String

        TYPE = "type",
        FILE = "file";
    }

    // default constructor required by Parse
    public Resource() {}

    public ParseFile getFile() {
        return getParseFile(Fields.FILE);
    }

    public void setFile(ParseFile file) {
        put(Fields.FILE, file);
    }

    public String getType() {
        return getString(Fields.TYPE);
    }

    public void setType(String type) {
        put(Fields.TYPE, type);
    }

}
