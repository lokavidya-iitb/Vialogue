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

@ParseClassName("Video")
public class Video extends BaseResourceClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public Video() {}

    private static final String VIDEO_RESOURCE_NAME = "video";

    public Video(Context context) {
        this(Uri.fromFile(BaseResourceClass.makeTempResourceFile(Slide.ResourceType.VIDEO, context)));
    }

    public Video(Uri uri) {
        super(uri);
    }
}
