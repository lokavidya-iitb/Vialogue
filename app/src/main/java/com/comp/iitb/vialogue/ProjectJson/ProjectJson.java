package com.comp.iitb.vialogue.ProjectJson;

import android.util.Log;

import com.comp.iitb.vialogue.models.Json.Project;
import com.comp.iitb.vialogue.models.Json.Slide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ironstein on 17/01/17.
 */

public class ProjectJson {
    public enum SlideType {
        IMAGE("image"),
        VIDEO("video"),
        QUESTION("question");

        private final String text;
        /**
         * @param text
         */
        private SlideType(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }

    private Project project;

    public ProjectJson(String jsonString) {
        // JSON already exists

        // TODO
        // I have passed jsonString directly just for the time being
        // Have to read JSON from file,
        // the parameter to this function should be the URL of the JSON file
        // String jsonString = doSomething()

        Gson gson = new Gson();
        project = gson.fromJson(jsonString, Project.class);

    }

    public ProjectJson(
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

        // JSON does not exist. Create a new JSON
        this.project = new Project(
                id,
                parentId,
                originalParentId,
                name,
                isDubbed,
                categoryId,
                languageId,
                authorId,
                resolutionX,
                resolutionY,
                slideOrderingSequence,
                slides
        );

    }

    public void saveJson() {
        String jsonString = new Gson().toJson(project);

        // TODO :implement logic to write the "jsonString" String to the JSON file
        Log.i("jsonString", jsonString);
    }

    // getters and setters
    public Integer getId() {
        return this.project.getId();
    }

    public void setId(Integer id) {
        this.project.setId(id);
    }

    public Integer getParentId() {
        return this.project.getParentId();
    }

    public void setParentId(Integer parentId) {
        this.project.setParentId(parentId);
    }

    public Integer getOriginalParentId() {
        return this.project.getOriginalParentId();
    }

    public void setOriginalParentId(Integer originalParentId) {
        this.project.setOriginalParentId(originalParentId);
    }

    public String getName() {
        return this.project.getName();
    }

    public void setName(String name) {
        this.project.setName(name);
    }

    public Boolean getIsDubbed() {
        return this.project.getIsDubbed();
    }

    public void setIsDubbed(Boolean isDubbed) {
        this.project.setIsDubbed(isDubbed);
    }

    public Integer getCategoryId() {
        return this.project.getCategoryId();
    }

    public void setCategoryId(Integer categoryId) {
        this.project.setCategoryId(categoryId);
    }

    public Integer getLanguageId() {
        return this.project.getLanguageId();
    }

    public void setLanguageId(Integer languageId) {
        this.project.setLanguageId(languageId);
    }

    public Integer getAuthorId() {
        return this.project.getAuthorId();
    }

    public void setAuthorId(Integer authorId) {
        this.project.setAuthorId(authorId);
    }

    public Integer getResolutionX() {
        return this.project.getResolutionX();
    }

    public void setResolutionX(Integer resolutionX) {
        this.project.setResolutionX(resolutionX);
    }

    public Integer getResolutionY() {
        return this.project.getResolutionY();
    }

    public void setResolutionY(Integer resolutionY) {
        this.project.setResolutionY(resolutionY);
    }

    public List<Integer> getSlideOrderingSequence() {
        return this.project.getSlideOrderingSequence();
    }

    public void setSlideOrderingSequence(List<Integer> slideOrderingSequence) {
        this.project.setSlideOrderingSequence(slideOrderingSequence);
    }

    public List<Slide> getSlides() {
        return this.project.getSlides();
    }

    public void setSlides(List<Slide> slides) {
        this.project.setSlides(slides);
    }

    // additional methods
    public boolean addSlide(SlideType type, String resourceUrl) {
        List<Slide> slides = getSlides();
        int newSlideId = slides.get(slides.size() - 1).getId();

        Slide newSlide;
        if (type == SlideType.IMAGE) {
            newSlide = new Slide(newSlideId, new ArrayList<Integer>(), -1, type.toString(), null, resourceUrl, null, null);
        } else if (type == SlideType.VIDEO) {
            newSlide = new Slide(newSlideId, new ArrayList<Integer>(), -1, type.toString(), null, null, resourceUrl, null);
        } else if (type == SlideType.QUESTION) {
            newSlide = new Slide(newSlideId, new ArrayList<Integer>(), -1, type.toString(), null, null, null, resourceUrl);
        } else {
            return false;
        }

        slides.add(newSlide);
        return true;
    }

    public Slide getSlide(int slideNumber) {
        return project.getSlides().get(slideNumber);
    }

    public void deleteSlide(int slideNumber) {
        getSlides().remove(slideNumber);
    }
}
