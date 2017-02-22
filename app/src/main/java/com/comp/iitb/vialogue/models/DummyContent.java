package com.comp.iitb.vialogue.models;

import android.graphics.Bitmap;

import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {


    /**
     * A dummy item representing a piece of content.
     */
    public static class Slide {
        public final Bitmap thumbnail;
        public SlideType slideType;
        public final String path;
        private String audioPath;
        private boolean isSelected;

        public Slide(String path,String audioPath,Bitmap thumbnail, SlideType slideType) {
            this.path = path;
            this.audioPath = audioPath;
            this.thumbnail = thumbnail;
            this.slideType = slideType;
            isSelected = false;
        }

        public void setAudioPath(String audioPath){
            this.audioPath = audioPath;
            if(this.slideType == SlideType.IMAGE)
                this.slideType = SlideType.IMAGE_AUDIO;
            if(this.audioPath!=null){
                SharedRuntimeContent.onSlideChanged(this);
            }
        }
        public void setSelected(boolean isSelected){
            this.isSelected = isSelected;
            if(isSelected){
                SharedRuntimeContent.onSlideChanged(this);
            }
        }

        @Override
        public String toString() {
            return slideType.toString();
        }

        public String getAudioPath() {
            return audioPath;
        }
    }

    public enum SlideType {
        IMAGE_AUDIO("IA"),
        VIDEO("V"),
        IMAGE("I");
        private String mType;
        private SlideType(String type){
            mType = type;
        }

        @Override
        public String toString() {
            return mType;
        }
    }
}
