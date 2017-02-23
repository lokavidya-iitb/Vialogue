package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;


import android.net.Uri;

import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.parse.ParseClassName;
import com.parse.ParseFile;

import java.io.File;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Audio")
public class Audio extends BaseResourceClass {

    private static final String AUDIO_RESOURCE_NAME = "audio";

    public Audio(Uri uri) {
        setFile(new ParseFile(new File(uri.getPath())));
    }

}
