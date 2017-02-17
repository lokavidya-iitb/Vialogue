package com.comp.iitb.vialogue.models.ParseObjects.models;

import com.parse.ParseClassName;

import com.comp.iitb.vialogue.models.ParseObjects.models.json.LanguageJson;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Language")
public class Language extends BaseParseClass {

    private static final class Fields {
        public static final String

        NAME = "name";
    }

    // default constructor required by Parse
    public Language() {}

    public String getName() {
        return getString(Fields.NAME);
    }

    public void setName(String name) {
        put(Fields.NAME, name);
    }
}
