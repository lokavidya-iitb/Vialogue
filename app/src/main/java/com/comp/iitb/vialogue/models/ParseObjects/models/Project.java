package com.comp.iitb.vialogue.models.ParseObjects.models;

import android.content.Context;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseFieldsClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ironstein on 15/02/17.
 */

@ParseClassName("Project")
public class Project extends BaseParseClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public Project() {}

    public static final class Fields implements BaseFieldsClass {
        public static String

        PARENT_ID =                     "parent_id",
        NAME =                          "name",
        DESCRIPTION =                   "description",
        AUTHOR =                        "author",
        CATEGORY =                      "category",
        LANGUAGE =                      "language",
        TAGS =                          "tags",
        RESOLUTION =                    "resolution",
        SLIDES =                        "slides";

        public ArrayList<String> getAllFields() {
            return new ArrayList<String>(Arrays.asList(new String[] {
                    PARENT_ID,
                    NAME,
                    DESCRIPTION,
                    AUTHOR,
                    CATEGORY,
                    LANGUAGE,
                    TAGS,
                    RESOLUTION,
                    SLIDES
            }));
        }
    }

    @Override
    public ArrayList<String> getAllFields() {
        ArrayList<String> fields = new Fields().getAllFields();
        fields.addAll(super.getAllFields());
        return fields;
    }

    public Project(String name) {
        setAuthor(new Author());
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

    public ParseObjectsCollection<Slide> getSlides() {
        ParseObjectsCollection<Slide> slides = null;
        try {
            slides =  (ParseObjectsCollection) getParseObject(Fields.SLIDES);
        } catch (Exception e) {}
        if(slides == null) {
            System.out.println("creating new slides");
            slides = new ParseObjectsCollection<Slide>();
            setSlides(slides);
        } else {
            System.out.println("using existing slides");
        }
        return slides;
    }

    public void setSlides(ParseObjectsCollection<Slide> slides) {
        put(Fields.SLIDES, slides);
    }

    public void addEmptySlide() {
        ParseObjectsCollection<Slide> slides = getSlides();
        slides.addObject(new Slide());
        setSlides(slides);
    }

    public void addSlide(Slide slide) {
        ParseObjectsCollection<Slide> slides = getSlides();
        slides.addObject(slide);
        setSlides(slides);
    }

    public Slide getSlide(int slideIndex) {
        return getSlides().get(slideIndex);
    }

    public void deleteSlide(int slideIndex) {
        ParseObjectsCollection<Slide> slides = getSlides();
        slides.remove(slideIndex);
        setSlides(slides);
    }

    public void moveSlideToPosition(int initialPosition, int finalPosition) {
        ParseObjectsCollection<Slide> slides = getSlides();
        slides.move(initialPosition, finalPosition);
        setSlides(slides);
    }

}
