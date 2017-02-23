package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;

import android.net.Uri;

import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.parse.ParseClassName;
import com.parse.ParseFile;

import java.io.File;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Image")
public class Image extends BaseResourceClass {

    public Image(){}

    private static final String IMAGE_RESOURCE_NAME = "image";

    public Image(Uri uri) {
        setFile(new ParseFile(new File(uri.getPath())));
    }

}
