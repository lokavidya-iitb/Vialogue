package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import android.net.Uri;

import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Audio;

/**
 * Created by ironstein on 03/03/17.
 */

public abstract class CanSaveAudioResource extends BaseResourceClass {

    public CanSaveAudioResource() {}

    public CanSaveAudioResource(Uri uri) {
        super(uri);
    }

    public Audio getAudio() {
        try {
            return (Audio) getChildrenResources().get(0);
        } catch (Exception e) {
            return null;
        }
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
        if(getChildrenResources().size() == 0) {
            return false;
        } return true;
    }

    public abstract void addAudio();

}
