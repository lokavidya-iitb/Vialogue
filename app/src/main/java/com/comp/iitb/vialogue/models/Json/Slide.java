package com.comp.iitb.vialogue.models.Json;

/**
 * Created by ironstein on 17/01/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slide {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("layering_objects")
    @Expose
    private List<Integer> layeringObjects = null;
    @SerializedName("hyperlink")
    @Expose
    private Integer hyperlink;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("urls")
    @Expose
    private Urls urls;

    // constructor
    public Slide(
            int id,
            List<Integer> layeringObjects,
            int hyperlink,
            String type,
            String audioUrl,
            String imageUrl,
            String videoUrl,
            String questionUrl
        ) {
        this.id = id;
        this.layeringObjects = layeringObjects;
        this.hyperlink = hyperlink;
        this.type = type;
        this.urls = new Urls(audioUrl, imageUrl, videoUrl, questionUrl);
    }

    // getter and setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getLayeringObjects() {
        return layeringObjects;
    }

    public void setLayeringObjects(List<Integer> layeringObjects) {
        this.layeringObjects = layeringObjects;
    }

    public Integer getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(Integer hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    // Urls methods
    public String getAudioUrl() {
        return this.urls.getAudioUrl();
    }

    public void setAudioUrl(String audioUrl) {
        this.urls.setAudioUrl(audioUrl);
    }

    public String getImageUrl() {
        return this.urls.getImageUrl();
    }

    public void setImageUrl(String imageUrl) {
        this.urls.setImageUrl(imageUrl);
    }

    public String getVideoUrl() {
        return this.urls.getVideoUrl();
    }

    public void setVideoUrl(String videoUrl) {
        this.urls.setVideoUrl(videoUrl);
    }

    public String getQuestionUrl() {
        return this.urls.getQuestionUrl();
    }

    public void setQuestionUrl(String questionUrl) {
        this.urls.setQuestionUrl(questionUrl);
    }

}

