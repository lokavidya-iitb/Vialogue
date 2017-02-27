package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;

import android.net.Uri;

import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.parse.ParseClassName;
import com.parse.ParseFile;

import java.io.File;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Video")
public class Video extends BaseResourceClass {

    private static final String VIDEO_RESOURCE_NAME = "video";

    public Video() {}

    public Video(Uri uri) {
        setFile(new ParseFile(new File(uri.getPath())));
    }

}
