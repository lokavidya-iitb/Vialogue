package com.comp.iitb.vialogue.models.ParseObjects.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Audio;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Video;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseFieldsClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcking.github.com.giraffeplayer.PlayerModel;

/**
 * Created by ironstein on 16/02/17.
 */

@ParseClassName("Slide")
public class Slide extends BaseParseClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public Slide() {}

    public Slide getNewInstance() {
        return new Slide();
    }

    private static class Fields implements BaseFieldsClass {
        public static final String

        HYPERLINKS = "hyperlinks";

        public ArrayList<String> getAllFields() {
            return new ArrayList<String>(Arrays.asList(new String[] {
                    HYPERLINKS
            }));
        }
    }

    @Override
    public ArrayList<String> getAllFields() {
        ArrayList<String> fields = new Fields().getAllFields();
        fields.addAll(super.getAllFields());
        return fields;
    }

    public static enum ResourceType {
        IMAGE,
        VIDEO,
        QUESTION,
        AUDIO
    }

    public static enum SlideType {
        IMAGE("IMAGE"),
        VIDEO("VIDEO"),
        QUESTION("QUESTION");

        String mSlideType;
        SlideType(String slideType) {
            mSlideType = slideType;
        }

        @Override
        public String toString() {
            return mSlideType;
        }
    }

    private SlideType mSlideType = null;
    private Bitmap mThumbnail;

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Context context, Storage storage) {
        if(mSlideType == null) {
            mSlideType = getSlideType();
        }
        if(mThumbnail == null) {
            if(getSlideType() == SlideType.IMAGE) {
                mThumbnail = storage.getImageThumbnail(getResource().getResourceFile().getAbsolutePath());
            } else if(getSlideType() == SlideType.VIDEO) {
                mThumbnail = storage.getVideoThumbnail(getResource().getResourceFile().getAbsolutePath());
            } else if(getSlideType() == SlideType.QUESTION) {
                mThumbnail = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_question);
            }
        }
    }

    public void setThumbnail(Bitmap thumbnail) {
        mThumbnail = thumbnail;
    }

//    public Slide(String path, String audioPath, Bitmap thumbnail, SlideType slideType) {
//        addResource(new);
//        this.path = path;
//        this.audioPath = audioPath;
//        this.thumbnail = thumbnail;
//        this.slideType = slideType;
//        isSelected = false;
//    }

    public ArrayList<String> getHyperlinks() {
        ArrayList<String> hyperlinks = null;
        try {
            hyperlinks = (ArrayList) getList(Fields.HYPERLINKS);
        } catch (Exception e) {}
        return hyperlinks;
    }

    private void setHyperlinks(List<String> hyperlinks) {
        put(Fields.HYPERLINKS, hyperlinks);
    }

    public void addHyperlink(String slideId) {
        // TODO decide if validation is required here to check
        // if the slideId passed corresponds to a valid slide in the Slide class in the database

        ArrayList<String> hyperlinks = getHyperlinks();
        hyperlinks.add(slideId);
        setHyperlinks(hyperlinks);
    }

    public void deleteHyperlink(int slideIndex) {
        ArrayList<String> hyperlinks = getHyperlinks();
        hyperlinks.remove(slideIndex);
        setHyperlinks(hyperlinks);
    }

    public void addResource(BaseResourceClass resource, ResourceType resourceType) throws Exception {
        if(resourceType == ResourceType.IMAGE) {
            addImage((Image) resource);
        } else if(resourceType == ResourceType.VIDEO) {
            addVideo((Video) resource);
        } else if(resourceType == ResourceType.QUESTION) {
            addQuestion((Question) resource);
        } else if(resourceType == ResourceType.AUDIO) {
            addAudio((Audio) resource);
        }
    }

    public void addImage(Image image) {
        ParseObjectsCollection<BaseResourceClass> childrenResources = new ParseObjectsCollection<>();
        childrenResources.addObject(image);
        setChildrenResources(childrenResources);
        mSlideType = SlideType.IMAGE;
    }

    public void addVideo(Video video) {
        ParseObjectsCollection<BaseResourceClass> childrenResources = new ParseObjectsCollection<>();
        childrenResources.addObject(video);
        setChildrenResources(childrenResources);
        mSlideType = SlideType.VIDEO;
    }

    public void addQuestion(Question question) {
        ParseObjectsCollection<BaseResourceClass> childrenResources = new ParseObjectsCollection<>();
        childrenResources.addObject(question);
        setChildrenResources(childrenResources);
        mSlideType = SlideType.QUESTION;
    }

    private void addAudio(Audio audio) throws Exception {
        throw new Exception("cannot add Audio directly to a slide");
    }

    public BaseResourceClass getResource() {
        return getChildrenResources().get(0);
    }

    public SlideType getSlideType() {
        if(mSlideType == null) {
            if(getResource() instanceof Image) {
                mSlideType = SlideType.IMAGE;
            } else if(getResource() instanceof Video) {
                mSlideType = SlideType.VIDEO;
            } else if(getResource() instanceof Question) {
                mSlideType = SlideType.QUESTION;
            }
        }
        return mSlideType;
    }

    public Audio getAudio() {
        Audio audio = null;
        if(getSlideType()==SlideType.IMAGE)

        try {
            audio = (Audio) getChildrenResources().get(0);
        } catch (Exception e) {}

        return audio;
    }

    public PlayerModel toPlayerModel() {
        PlayerModel playerModel = null;
        if(mSlideType == SlideType.IMAGE) {
            if(!(((Image) getResource()).hasAudio())) {
                return null;
            }
            playerModel = new PlayerModel(
                    getResource().getResourceFile().getAbsolutePath(),
                    ((Image) getResource()).getAudio().getResourceFile().getAbsolutePath()
            );
            playerModel.setType(PlayerModel.MediaType.IMAGE_AUDIO);
        } else if(mSlideType == SlideType.VIDEO) {
            playerModel = new PlayerModel(
                    getResource().getResourceFile().getAbsolutePath(),
                    null
            );
            playerModel.setType(PlayerModel.MediaType.VIDEO);
        } else if(mSlideType == SlideType.QUESTION) {
            // TODO add implementation
        }
        return playerModel;
    }

    public void setSlideType(SlideType slideType) {
        mSlideType = slideType;
    }

    @Override
    public Slide deepCopy() throws Exception {
        Slide copiedSlide = (Slide) super.deepCopy();
        copiedSlide.setThumbnail(getThumbnail());
        copiedSlide.setSlideType(getSlideType());
        return copiedSlide;
    }


}
