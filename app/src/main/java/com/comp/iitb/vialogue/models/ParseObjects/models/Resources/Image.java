package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;

import android.content.Context;
import android.net.Uri;

import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.parse.ParseClassName;
import com.parse.ParseFile;

import java.io.File;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Image")
public class Image extends BaseResourceClass {

    private static final String IMAGE_RESOURCE_NAME = "image";

    public Image() {}

    public Image(Context context) {
        this(Uri.fromFile(BaseResourceClass.makeTempResourceFile(Slide.ResourceType.IMAGE, context)));
    }

    public Image(Uri uri) {
        super(uri);
    }

}
