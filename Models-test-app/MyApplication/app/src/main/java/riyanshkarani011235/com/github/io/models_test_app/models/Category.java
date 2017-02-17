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

    private CategoryJson mCategoryJson;
    private String mName;

    // default constructor required by Parse
    public Category() {}

//    // from JSON String
//    public Category(String json) throws org.json.JSONException {
//        this(new Gson().fromJson(json, CategoryJson.class));
//    }
//
//    // from CategoryJson instance
//    public Category(CategoryJson categoryJson) throws org.json.JSONException {
//        mCategoryJson = categoryJson;
//        mName = categoryJson.getName();
//        init();
//    }

    // implementing interface
    public CategoryJson getJsonObject() {
        return mCategoryJson;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return mCategoryJson.toString();
    }

}
