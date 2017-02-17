
package com.comp.iitb.vialogue.models.ParseObjects.models.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProjectJson extends BaseJsonClass {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("id")
    @Expose
    private String id;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("is_edited")
    @Expose
    private Boolean isEdited;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("children_resources")
    @Expose
    private List<ResourceJson> childrenResources = null;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("author")
    @Expose
    private AuthorJson author;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("category")
    @Expose
    private CategoryJson category;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("language")
    @Expose
    private LanguageJson language;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("resolution")
    @Expose
    private List<Integer> resolution = null;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("slide_ordering_sequence")
    @Expose
    private List<Integer> slideOrderingSequence = null;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("slides")
    @Expose
    private List<SlideJson> slides = null;

    /**
     * 
     * (Required)
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getIsEdited() {
        return isEdited;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<ResourceJson> getChildrenResources() {
        return childrenResources;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setChildrenResources(List<ResourceJson> childrenResources) {
        this.childrenResources = childrenResources;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * (Required)
     * 
     */
    public AuthorJson getAuthor() {
        return author;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setAuthor(AuthorJson author) {
        this.author = author;
    }

    /**
     * 
     * (Required)
     * 
     */
    public CategoryJson getCategory() {
        return category;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setCategory(CategoryJson category) {
        this.category = category;
    }

    /**
     * 
     * (Required)
     * 
     */
    public LanguageJson getLanguage() {
        return language;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLanguage(LanguageJson language) {
        this.language = language;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Integer> getResolution() {
        return resolution;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setResolution(List<Integer> resolution) {
        this.resolution = resolution;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Integer> getSlideOrderingSequence() {
        return slideOrderingSequence;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSlideOrderingSequence(List<Integer> slideOrderingSequence) {
        this.slideOrderingSequence = slideOrderingSequence;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<SlideJson> getSlides() {
        return slides;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSlides(List<SlideJson> slides) {
        this.slides = slides;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
