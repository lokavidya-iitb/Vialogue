package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.parse.ParseFile;

/**
 * Created by ironstein on 16/02/17.
 */

public abstract class BaseResourceClass extends BaseParseClass {

    private static final class Fields {
        public static final String

        FILE = "file";
    }

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public BaseResourceClass() {}

    public BaseResourceClass(ParseFile file) {
        setFile(file);
    }

    public ParseFile getFile() {
        return getParseFile(Fields.FILE);
    }

    public void setFile(ParseFile file) {
        put(Fields.FILE, file);
    }
}
