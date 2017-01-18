package com.comp.iitb.vialogue.models.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ironstein on 17/01/17.
 */

public class Project {

    /**
     * {
     * 		id: ...,
     * 		parent_id: ...,
     * 		original_parent_id: ...,
     * 		name: ...,
     * 		is_dubbed: ...,
     * 		category_id: ...,
     * 		language_id: ...,
     * 		author_id: ...,
     * 		resolution_x: ...,
     * 		resolution_y: ...,
     * 		slide_ordering_sequence: [...],
     * 		slides: [
     * 			{
     * 		        id: ...,
     * 				layering_objects: [...],
     * 				hyperlink: ...,
     * 				type: ...,
     * 				urls: {
     * 					audio_url: ...,
     * 					image_url: ...,
     * 					video_url: ...,
     * 					question_url: ...
     * 				}
     * 			},
     * 			...
     * 		]
     * }
     */

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("parent_id")
    @Expose
    private Integer parentId;

    @SerializedName("original_parent_id")
    @Expose
    private Integer originalParentId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("is_dubbed")
    @Expose
    private Boolean isDubbed;

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;

    @SerializedName("language_id")
    @Expose
    private Integer languageId;

    @SerializedName("author_id")
    @Expose
    private Integer authorId;

    @SerializedName("resolution_x")
    @Expose
    private Integer resolutionX;

    @SerializedName("resolution_y")
    @Expose
    private Integer resolutionY;

    @SerializedName("slide_ordering_sequence")
    @Expose
    private List<Integer> slideOrderingSequence = null;

    @SerializedName("slides")
    @Expose
    private List<Slide> slides = null;

    // constructor
    public Project(
            int id,
            int parentId,
            int originalParentId,
            String name,
            boolean isDubbed,
            int categoryId,
            int languageId,
            int authorId,
            int resolutionX,
            int resolutionY,
            List<Integer> slideOrderingSequence,
            List<Slide> slides
        ) {
        this.id = id;
        this.parentId = parentId;
        this.originalParentId = originalParentId;
        this.name = name;
        this.isDubbed = isDubbed;
        this.categoryId = categoryId;
        this.languageId = languageId;
        this.authorId = authorId;
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.slideOrderingSequence = slideOrderingSequence;
        this.slides = slides;
    }

    // getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOriginalParentId() {
        return originalParentId;
    }

    public void setOriginalParentId(Integer originalParentId) {
        this.originalParentId = originalParentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDubbed() {
        return isDubbed;
    }

    public void setIsDubbed(Boolean isDubbed) {
        this.isDubbed = isDubbed;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getResolutionX() {
        return resolutionX;
    }

    public void setResolutionX(Integer resolutionX) {
        this.resolutionX = resolutionX;
    }

    public Integer getResolutionY() {
        return resolutionY;
    }

    public void setResolutionY(Integer resolutionY) {
        this.resolutionY = resolutionY;
    }

    public List<Integer> getSlideOrderingSequence() {
        return slideOrderingSequence;
    }

    public void setSlideOrderingSequence(List<Integer> slideOrderingSequence) {
        this.slideOrderingSequence = slideOrderingSequence;
    }

    public List<Slide> getSlides() {
        return slides;
    }

    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }

}
