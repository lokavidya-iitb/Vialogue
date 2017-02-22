package riyanshkarani011235.com.github.io.models_test_app.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;

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
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public Resource() {}

    public Resource(ParseFile file) {
        setFile(file);
    }

    public Resource(String type) {
        setType(type);
    }

    public ParseFile getFile() {
        return getParseFile(Fields.FILE);
    }

    public void setFile(ParseFile file) {
        put(Fields.FILE, file);
    }

    public String getType() {
        return getString(Fields.TYPE);
    }

    private void setType(String type) {
        put(Fields.TYPE, type);
    }

}
