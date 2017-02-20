package com.comp.iitb.vialogue.models.ParseObjects.models;

import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.comp.iitb.vialogue.models.ParseObjects.models.json.ProjectJson;
import com.comp.iitb.vialogue.models.ParseObjects.models.json.SlideJson;

/**
 * Created by ironstein on 15/02/17.
 */

@ParseClassName("Project")
public class Project extends BaseParseClass {

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

    public Project(String name, String parentId) {
        setName(name);
        setParentId(parentId);
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

    /*
     * Slide related methods
     */

    public ArrayList<Integer> getSlideOrderingSequence() {
        return (ArrayList) getList(Fields.SLIDE_ORDERING_SEQUENCE);
    }

    private void setSlideOrderingSequence(List<Integer> slideOrderingSequence) {
        put(Fields.SLIDE_ORDERING_SEQUENCE, slideOrderingSequence);
    }

    private ArrayList<Slide> getSlides() {
        return (ArrayList) getList(Fields.SLIDES);
    }

    private void setSlides(ArrayList<Slide> slides) {
        put(Fields.SLIDES, slides);
    }

    public void addSlide() {
        int newProjectSlideId;
        ArrayList<Integer> slideOrderingSequence = getSlideOrderingSequence();
        ArrayList<Slide> slides = (ArrayList) getSlides();

        if(slideOrderingSequence == null) {
            // no slides added to the project yet
            slideOrderingSequence = new ArrayList<Integer>();
            slides = new ArrayList<Slide>();
            newProjectSlideId = 0;
        } else {
            newProjectSlideId = Collections.max(getSlideOrderingSequence()) + 1;
        }

        // add new Slide to "slides", and the new ProjectSlideId to "slideOrderingSequence"
        slideOrderingSequence.add(newProjectSlideId);
        Slide slide = new Slide(newProjectSlideId);
        slides.add(slide);

        // save the new "slides" and "slideOrderingSequence" arrays
        setSlideOrderingSequence(slideOrderingSequence);
        setSlides(slides);
    }

    public Slide getSlide(int slideIndex) {
        ArrayList<Integer> slideOrderingSequence = getSlideOrderingSequence();
        if(slideIndex >= slideOrderingSequence.size()) {
            throw new ArrayIndexOutOfBoundsException("slideIndex greater than length of {slideOrderingSequence}");
        }
        int projectSlideId = getSlideOrderingSequence().get(slideIndex);
        for(Slide s : getSlides()) {
            if(s.getProjectSlideId() == projectSlideId) {
                return s;
            }
        }
        throw new Error("projectSlideId does not match any Slide. Something is very wrong here :(");
    }

    public void deleteSlide(int slideIndex) {
        ArrayList<Integer> slideOrderingSequence = getSlideOrderingSequence();
        if(slideIndex >= slideOrderingSequence.size()) {
            throw new ArrayIndexOutOfBoundsException("slideIndex greater than length of {slideOrderingSequence}");
        }

        int projectSlideId = getSlideOrderingSequence().get(slideIndex);
        ArrayList<Slide> slides = getSlides();

        int slideToDeleteIndex = -1;
        for(int i=0; i<slides.size(); i++) {
            if(slides.get(i).getProjectSlideId() == projectSlideId) {
                slideToDeleteIndex = i;
                break;
            }
        }

        if(slideToDeleteIndex == -1) {
            throw new Error("projectSlideId does not match any Slide. Something is very wrong here :(");
        }

        // remove elements from "slides" and "slideOrderingSequence"
        slideOrderingSequence.remove(slideIndex);
        slides.remove(slideToDeleteIndex);

        // save the new "slides" and "slideOrderingSequence" arrays
        setSlideOrderingSequence(slideOrderingSequence);
        setSlides(slides);

    }

    public void moveSlideToPosition(int initialPosition, int finalPosition) {
        ArrayList<Slide> slides = getSlides();
    }

}
