
package com.comp.iitb.vialogue.models.ParseObjects.models.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SlideJson extends BaseJsonClass {

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
    @SerializedName("project_slide_id")
    @Expose
    private Integer projectSlideId;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("hyperlinks")
    @Expose
    private List<String> hyperlinks = null;

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
    public Integer getProjectSlideId() {
        return projectSlideId;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setProjectSlideId(Integer projectSlideId) {
        this.projectSlideId = projectSlideId;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<String> getHyperlinks() {
        return hyperlinks;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setHyperlinks(List<String> hyperlinks) {
        this.hyperlinks = hyperlinks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
