package com.comp.iitb.vialogue.models.Json;

/**
 * Created by ironstein on 17/01/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Urls {

    @SerializedName("audio_url")
    @Expose
    private String audioUrl;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("question_url")
    @Expose
    private String questionUrl;

    // constructor
    public Urls(String audioUrl, String imageUrl, String videoUrl, String questionUrl) {
        this.audioUrl = audioUrl;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.questionUrl = questionUrl;
    }

    // getters and setters
    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
    }

}

