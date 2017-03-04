package com.comp.iitb.vialogue.models.ParseObjects.models;

import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.parse.ParseClassName;

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

    private static final class Fields {
        public static final String

        NAME = "name";
    }

    public String getName() {
        return getString(Fields.NAME);
    }

    public void setName(String name) {
        put(Fields.NAME, name);
    }

}
