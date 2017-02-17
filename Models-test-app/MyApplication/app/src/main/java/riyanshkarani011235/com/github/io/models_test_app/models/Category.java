package riyanshkarani011235.com.github.io.models_test_app.models;

import com.google.gson.Gson;
import com.parse.ParseClassName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import riyanshkarani011235.com.github.io.models_test_app.models.json.CategoryJson;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Category")
public class Category extends BaseParseClass {

    private static final class Fields {
        public static final String

        NAME = "name";
    }

    // default constructor required by Parse
    public Category() {}

    public String getName() {
        return getString(Fields.NAME);
    }

    public void setName(String name) {
        put(Fields.NAME, name);
    }

}
