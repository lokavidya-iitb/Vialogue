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
        "type",
        "url",
        "children_resources",
        "is_edited"
})
public class Resource {

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("url")
    private String url;
    @JsonProperty("children_resources")
    private List<Resource> childrenResources = null;
    @JsonProperty("is_edited")
    private Boolean isEdited;

    /**
     * No args constructor for use in serialization
     *
     */
    public Resource() {
    }

    /**
     *
     * @param id
     * @param isEdited
     * @param childrenResources
     * @param type
     * @param url
     */
    public Resource(String id, String type, String url, List<Resource> childrenResources, Boolean isEdited) {
        super();
        this.id = id;
        this.type = type;
        this.url = url;
        this.childrenResources = childrenResources;
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

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("children_resources")
    public List<Resource> getChildrenResources() {
        return childrenResources;
    }

    @JsonProperty("children_resources")
    public void setChildrenResources(List<Resource> childrenResources) {
        this.childrenResources = childrenResources;
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

