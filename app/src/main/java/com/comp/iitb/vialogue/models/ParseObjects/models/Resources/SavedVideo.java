package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;

import android.content.Context;
import android.net.Uri;

import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.parse.ParseClassName;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("SavedVideo")
public class SavedVideo extends BaseResourceClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public SavedVideo() {}

    public SavedVideo getNewInstance() {
        return new SavedVideo();
    }

    private static final String VIDEO_RESOURCE_NAME = "video";

    public SavedVideo(Context context) {
        this(Uri.fromFile(BaseResourceClass.makeTempResourceFile(Slide.ResourceType.VIDEO, context)));
    }

    public SavedVideo(Uri uri) {
        super(uri);
    }

    public boolean doesStoreFile() {
        return true;
    }
}
