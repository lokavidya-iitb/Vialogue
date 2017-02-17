package riyanshkarani011235.com.github.io.models_test_app.models;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.parse.ParseClassName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import riyanshkarani011235.com.github.io.models_test_app.LoadFromJson;
import riyanshkarani011235.com.github.io.models_test_app.models.json.ProjectJson;
import riyanshkarani011235.com.github.io.models_test_app.models.json.SlideJson;

/**
 * Created by ironstein on 15/02/17.
 */

@ParseClassName("Project")
public class Project extends BaseParseClass {

    private ProjectJson mProjectJson;
    private String mParentId;
    private String mName;
    private String mDescription;
    private Author mAuthor;
    private Category mCategory;
    private Language mLanguage;
    private List<String> mTags;
    private List<Integer> mResolution;
    private List<Integer> mSlideOrderingSequence;
    private List<Slide> mSlides;

    private final String PARENT_ID_FIELD = "parent_id";
    private final String NAME_FIELD = "name";
    private final String DESCRIPTION_FIELD = "description";

    private static final class Fields {
        public static String

        PARENT_ID =                     "parent_id",
        NAME =                          "name",
        DESCRIPTION =                   "description",
        AUTHOR =                        "author",
        CATEGORY =                      "category",
        LANGUAGE =                      "language",
        TAGS =                          "tags",
        RESOLUTION =                    "resolution",
        SLIDE_ORDERING_SEQUENCE =       "slide_ordering_sequence",
        SLIDES =                        "slides";
    }

    // default constructor required by Parse
    public Project() {}

//    // from URI
//    public Project(Uri uri) throws org.json.JSONException {
//        // TODO
//        // get json file from uri
//        this("");
//    }
//
//    // from JSON String
//    public Project(String json) throws org.json.JSONException {
//        this(new Gson().fromJson(json, ProjectJson.class));
//    }
//
//    // from ProjectJson Instance
//    public Project(ProjectJson projectJson) throws org.json.JSONException {
//        mProjectJson = projectJson;
//        setParentId(projectJson.getParentId());
//        setName(projectJson.getName());
//        setDescription(projectJson.getDescription());
//        setAuthor(new Author(projectJson.getAuthor()));
//        setCategory(new Category(projectJson.getCategory()));
//        setLanguage(new Language(projectJson.getLanguage()));
//        setTags(projectJson.getTags());
//        setResolution(projectJson.getResolution());
//        setSlideOrderingSequence(projectJson.getSlideOrderingSequence());
//        List<Slide> slides = new ArrayList<Slide>();
//        for(SlideJson s : projectJson.getSlides()) {
//            slides.add(new Slide(s));
//        }
//        setSlides(slides);
//        init();
//    }

    // implement interface
    public ProjectJson getJsonObject() {
        return mProjectJson;
    }

    // getters and setters
    public String getParentId() {
        return getString(Fields.PARENT_ID);
    }

    public void setParentId(String parentId) {
        put(Fields.PARENT_ID, parentId);
    }

    public String getName() {
        return getString(Fields.NAME);
    }

    public void setName(String name) {
        put(Fields.NAME, name);
    }

    public String getDescription() {
        return getString(Fields.DESCRIPTION);
    }

    public void setDescription(String description) {
        put(Fields.DESCRIPTION, description);
    }

    public Author getAuthor() {
        return (Author) getParseObject(Fields.AUTHOR);
    }

    public void setAuthor(Author author) {
        put(Fields.AUTHOR, author);
    }

    public Category getCategory() {
        return (Category) getParseObject(Fields.CATEGORY);
    }

    public void setCategory(Category category) {
        put(Fields.CATEGORY, category);
    }

    public Language getLanguage() {
        return (Language) getParseObject(Fields.LANGUAGE);
    }

    public void setLanguage(Language language) {
        put(Fields.LANGUAGE, language);
    }

    public List<String> getTags() {
        return getList(Fields.TAGS);
    }

    public void setTags(List<String> tags) {
        put(Fields.TAGS, tags);
    }

    public List<Integer> getResolution() {
        return getList(Fields.RESOLUTION);
    }

    public void setResolution(List<Integer> resolution) {
        put(Fields.RESOLUTION, resolution);
    }

    public List<Integer> getSlideOrderingSequence() {
        return getList(Fields.SLIDE_ORDERING_SEQUENCE);
    }

    public void setSlideOrderingSequence(List<Integer> slideOrderingSequence) {
        put(Fields.SLIDE_ORDERING_SEQUENCE, slideOrderingSequence);
    }

    public List<Slide> getSlides() {
        return getList(Fields.SLIDES);
    }

    public void setSlides(List<Slide> slides) {
        put(Fields.SLIDES, slides);
    }

    // additional methods

    // slide related
    public void getSlide(int slideIndex) {}
    public void addSlide() {}
    public void deleteSlide(int slideIndex) {}

}