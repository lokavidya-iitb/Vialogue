package com.comp.iitb.vialogue.models.ParseObjects;

/**
 * Created by ironstein on 11/02/17.
 */



import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "parent",
        "original_parent",
        "name",
        "description",
        "tags",
        "is_dubbed",
        "category",
        "language",
        "author",
        "resolution",
        "slide_ordering_sequence",
        "slides"
})
public class Project {

    @JsonProperty("id")
    private String id;
    @JsonProperty("parent")
    private String parent;
    @JsonProperty("original_parent")
    private String originalParent;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("tags")
    private List<String> tags = null;
    @JsonProperty("is_dubbed")
    private Boolean isDubbed;
    @JsonProperty("category")
    private String category;
    @JsonProperty("language")
    private String language;
    @JsonProperty("author")
    private String author;
    @JsonProperty("resolution")
    private List<Object> resolution = null;
    @JsonProperty("slide_ordering_sequence")
    private List<Integer> slideOrderingSequence = null;
    @JsonProperty("slides")
    private List<Slide> slides = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Project() {
    }

    /**
     *
     * @param slideOrderingSequence
     * @param tags
     * @param id
     * @param author
     * @param category
     * @param isDubbed
     * @param slides
     * @param description
     * @param name
     * @param parent
     * @param originalParent
     * @param resolution
     * @param language
     */
    public Project(String id, String parent, String originalParent, String name, String description, List<String> tags, Boolean isDubbed, String category, String language, String author, List<Object> resolution, List<Integer> slideOrderingSequence, List<Slide> slides) {
        super();
        this.id = id;
        this.parent = parent;
        this.originalParent = originalParent;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.isDubbed = isDubbed;
        this.category = category;
        this.language = language;
        this.author = author;
        this.resolution = resolution;
        this.slideOrderingSequence = slideOrderingSequence;
        this.slides = slides;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("parent")
    public String getParent() {
        return parent;
    }

    @JsonProperty("parent")
    public void setParent(String parent) {
        this.parent = parent;
    }

    @JsonProperty("original_parent")
    public String getOriginalParent() {
        return originalParent;
    }

    @JsonProperty("original_parent")
    public void setOriginalParent(String originalParent) {
        this.originalParent = originalParent;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @JsonProperty("is_dubbed")
    public Boolean getIsDubbed() {
        return isDubbed;
    }

    @JsonProperty("is_dubbed")
    public void setIsDubbed(Boolean isDubbed) {
        this.isDubbed = isDubbed;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty("resolution")
    public List<Object> getResolution() {
        return resolution;
    }

    @JsonProperty("resolution")
    public void setResolution(List<Object> resolution) {
        this.resolution = resolution;
    }

    @JsonProperty("slide_ordering_sequence")
    public List<Integer> getSlideOrderingSequence() {
        return slideOrderingSequence;
    }

    @JsonProperty("slide_ordering_sequence")
    public void setSlideOrderingSequence(List<Integer> slideOrderingSequence) {
        this.slideOrderingSequence = slideOrderingSequence;
    }

    @JsonProperty("slides")
    public List<Slide> getSlides() {
        return slides;
    }

    @JsonProperty("slides")
    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

