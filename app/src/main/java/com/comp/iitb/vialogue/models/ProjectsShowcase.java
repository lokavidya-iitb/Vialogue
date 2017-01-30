package com.comp.iitb.vialogue.models;

/**
 * Created by jeffrey on 17/1/17.
 */
public class ProjectsShowcase {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImagesCount() {
        return ImagesCount;
    }

    public void setImagesCount(int imagesCount) {
        ImagesCount = imagesCount;
    }

    public int getAudioCount() {
        return AudioCount;
    }

    public void setAudioCount(int audioCount) {
        AudioCount = audioCount;
    }

    public int getVideoCount() {
        return VideoCount;
    }

    public void setVideoCount(int videoCount) {
        VideoCount = videoCount;
    }

    public int getQuestionCount() {
        return QuestionCount;
    }

    public void setQuestionCount(int questionCount) {
        QuestionCount = questionCount;
    }

    private int ImagesCount;
    private int AudioCount;
    private int VideoCount;
    private int QuestionCount;

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    private String imageFile;

    public ProjectsShowcase() {
    }

    public ProjectsShowcase(String name, String imageFile, int AudioCount, int ImagesCount, int QuestionCount, int VideoCount) {
        this.name = name;
        this.imageFile=imageFile;
        this.AudioCount=AudioCount;
        this.ImagesCount=ImagesCount;
        this.QuestionCount=QuestionCount;
        this.VideoCount=VideoCount;
    }

}