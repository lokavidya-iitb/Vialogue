package com.comp.iitb.vialogue.models.ParseObjects.models;

import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseFieldsClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Category")
public class Category extends BaseParseClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public Category() {}

    public Category(String name) {
        setName(name);
    }

    public Category getNewInstance() {
        return new Category();
    }

    private static final class Fields implements BaseFieldsClass {
        public static final String

        NAME = "name",
        IMAGE_URL = "image_url",
        DESCRIPTION = "description";

        public ArrayList<String> getAllFields() {
            return new ArrayList<String>(Arrays.asList(new String[] {
                    NAME
            }));
        }
    }

    @Override
    public ArrayList<String> getAllFields() {
        ArrayList<String> fields = new Fields().getAllFields();
        fields.addAll(super.getAllFields());
        return fields;
    }

    public String getName() {
        return getString(Fields.NAME);
    }

    public void setName(String name) {
        put(Fields.NAME, name);
    }

    public String getImageUrl() {
        return getString(Fields.IMAGE_URL);
    }

    public void setImageUrl(String imageUrl) {
        put(Fields.IMAGE_URL, imageUrl);
    }

    public String getDescription() {
        return getString(Fields.DESCRIPTION);
    }

    public void setDescription(String description) {
        put(Fields.DESCRIPTION, description);
    }

}
