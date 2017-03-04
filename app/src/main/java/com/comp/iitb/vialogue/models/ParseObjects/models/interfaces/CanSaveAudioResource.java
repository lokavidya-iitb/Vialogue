package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import android.net.Uri;

import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Audio;

/**
 * Created by ironstein on 03/03/17.
 */

public abstract class CanSaveAudioResource extends BaseResourceClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public CanSaveAudioResource() {}

    public CanSaveAudioResource(Uri uri) {
        super(uri);
    }

    public Audio getAudio() {
        Audio audio = null;
        try {
            audio = (Audio) getChildrenResources().get(0);
        } catch (Exception e) {}

        return audio;
    }

    public void addAudio(Audio audio) {
        ParseObjectsCollection<BaseResourceClass> childrenResources = new ParseObjectsCollection<>();
        childrenResources.add(audio);
        setChildrenResources(childrenResources);
    }

    public void removeAudio() {
        setChildrenResources(new ParseObjectsCollection<BaseResourceClass>());
    }

    public boolean hasAudio() {
        if(getAudio() == null) {
            return false;
        } return true;
    }

    public abstract void addAudio();

}
