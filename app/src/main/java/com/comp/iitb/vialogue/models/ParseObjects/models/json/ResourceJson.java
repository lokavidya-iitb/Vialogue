
package com.comp.iitb.vialogue.models.ParseObjects.models.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResourceJson extends BaseJsonClass {

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
    @SerializedName("type")
    @Expose
    private String type;
    /**
     *
     * (Required)
     *
     */
    @SerializedName("type_id")
    @Expose
    private String typeId;
    /**
     *
     * (Required)
     *
     */
    @SerializedName("url")
    @Expose
    private String url;
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
    @SerializedName("is_edited")
    @Expose
    private Boolean isEdited;

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
    public String getType() {
        return type;
    }

    /**
     *
     * (Required)
     *
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * (Required)
     *
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     *
     * (Required)
     *
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     *
     * (Required)
     *
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * (Required)
     *
     */
    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
