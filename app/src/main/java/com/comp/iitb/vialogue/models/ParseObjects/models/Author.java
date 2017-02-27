package com.comp.iitb.vialogue.models.ParseObjects.models;

import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.parse.ParseClassName;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Author")
public class Author extends BaseParseClass {

    private static final class Fields {
        public static final String

        FIRST_NAME = "first_name",
        LAST_NAME = "last_name",
        EMAIL = "email";
    }

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public Author() {}

    public String getFirstName() {
        return getString(Fields.FIRST_NAME);
    }

    public void setFirstName(String firstName) {
        put(Fields.FIRST_NAME, firstName);
    }

    public String getLastName() {
        return getString(Fields.LAST_NAME);
    }

    public void setLastName(String lastName) {
        put(Fields.LAST_NAME, lastName);
    }

    public String getEmail() {
        return getString(Fields.EMAIL);
    }

    public void setEmail(String email) {
        put(Fields.EMAIL, email);
    }

}
