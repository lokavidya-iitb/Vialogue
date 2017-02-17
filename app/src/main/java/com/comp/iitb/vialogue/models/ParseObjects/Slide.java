package com.comp.iitb.vialogue.models.ParseObjects;

/**
 * Created by ironstein on 12/02/17.
 */

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "project_slide_id",
        "hyperlinks",
        "type",
        "resource",
        "is_edited"
})
public class Slide {

    @JsonProperty("id")
    private String id;
    @JsonProperty("project_slide_id")
    private Integer projectSlideId;
    @JsonProperty("hyperlinks")
    private List<String> hyperlinks = null;
    @JsonProperty("type")
    private String type;
    @JsonProperty("resource")
    private Resource resource;
    @JsonProperty("is_edited")
    private Boolean isEdited;

    /**
     * No args constructor for use in serialization
     *
     */
    public Slide() {
    }

    /**
     *
     * @param id
     * @param isEdited
     * @param projectSlideId
     * @param resource
     * @param type
     * @param hyperlinks
     */
    public Slide(String id, Integer projectSlideId, List<String> hyperlinks, String type, Resource resource, Boolean isEdited) {
        super();
        this.id = id;
        this.projectSlideId = projectSlideId;
        this.hyperlinks = hyperlinks;
        this.type = type;
        this.resource = resource;
        this.isEdited = isEdited;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("project_slide_id")
    public Integer getProjectSlideId() {
        return projectSlideId;
    }

    @JsonProperty("project_slide_id")
    public void setProjectSlideId(Integer projectSlideId) {
        this.projectSlideId = projectSlideId;
    }

    @JsonProperty("hyperlinks")
    public List<String> getHyperlinks() {
        return hyperlinks;
    }

    @JsonProperty("hyperlinks")
    public void setHyperlinks(List<String> hyperlinks) {
        this.hyperlinks = hyperlinks;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("resource")
    public Resource getResource() {
        return resource;
    }

    @JsonProperty("resource")
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @JsonProperty("is_edited")
    public Boolean getIsEdited() {
        return isEdited;
    }

    @JsonProperty("is_edited")
    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

