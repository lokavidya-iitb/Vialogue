package com.comp.iitb.vialogue.coordinators;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.Toast;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.adapters.SlideRecyclerViewAdapter;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer.PlayerModel;

/**
 * Created by shubh on 11-01-2017.
 */

public class SharedRuntimeContent {

    public static String untitledProjectNameRegex = "(^)Untitled Project ([0-9].*$)";

    public static List<String> videoPathList = new ArrayList<>();
    public static List<String> imagePathList = new ArrayList<>();
    public static List<Bitmap> imageThumbnails = new ArrayList<>();
    public static final int GET_IMAGE = 541;
    public static final int GET_VIDEO = 542;
    public static final int GET_CAMERA_IMAGE = 543;
    public static final int GET_QUESTION = 544;
//    public static final String IMAGE_FOLDER_NAME = "images";
//    public static final String VIDEO_FOLDER_NAME = "videos";
//    public static final String AUDIO_FOLDER_NAME = "audio";
//    public static final String VIALOGUE_FOLDER_NAME = "vialogue";
//    public static final String TEMP_FOLDER = "temp_folder";
//    public static final String TEMP_IMAGE_NAME = "image_temp";
    /**
     * An array of sample (dummy) items.
     */
//    public static File projectFolder;
    public static SlideRecyclerViewAdapter projectAdapter;
    public static MainActivity mainActivity;
    public static FloatingActionButton previewFab;
    // TODO change implementation for isSelected
    public static boolean isSelected = false;
    public static int selectedPosition;

    public static Project project = new Project();

    public static void addSlide(Slide slide) {
        project.addSlide(slide);
        projectAdapter.notifyItemInserted(project.getSlides().size() - 1);
        previewFab.show();
        calculatePreviewFabVisibility();
    }

    public static void changeSlideAtPosition(int position, Slide slide) {
        deleteSlide(position);
        addSlideAtPosition(position, slide);
    }

    public static void addSlideAtPosition(int position, Slide slide) {
        addSlide(slide);
        changeSlidePosition(getNumberOfSlides()-1, position);
    }

    public static void changeSlidePosition(int current, int destination) {
        project.moveSlideToPosition(current, destination);
        projectAdapter.notifyDataSetChanged();
        calculatePreviewFabVisibility();
    }

    public static void deleteSlide(int position) {
        project.deleteSlide(position);
        projectAdapter.notifyItemRemoved(position);
        updateAdapterView(position);
        calculatePreviewFabVisibility();
    }

    public static void calculatePreviewFabVisibility() {

        if(previewFab == null) {
            return;
        }

        if(getNumberOfSlides() == 0) {} else {
            for(Slide s : project.getSlides().getAll()) {
                if(s.getSlideType() == Slide.SlideType.VIDEO) {
                    previewFab.show();
                    return;
                } else if(s.getSlideType() == Slide.SlideType.IMAGE) {
                    if(((Image) s.getResource()).hasAudio()) {
                        previewFab.show();
                        return;
                    }
                }
            }
        }

        previewFab.hide();
    }

    public static void setProjectName(String name) {
        project.setName(name);
    }

    public static String getProjectName() {
        return project.getName();
    }

    public static void pinProject(Context context) {
        // save project with a temporary name
        if((getProjectName() == null) || (getProjectName() == "")) {
            String newProjectName = getNewUndefinedProjectName();
            System.out.println("setting project name : " + newProjectName);
            setProjectName(newProjectName);
        } else {
            System.out.println("projectName : " + getProjectName());
        }

        if(getNumberOfSlides() != 0) {
            System.out.println("pinning project");
            try {
                project.pinParseObject();
//                project.save();
//                Toast.makeText(context, "Project saved succcessfully :)", Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                Toast.makeText(context, "Something went wrong while saving project :(", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public static String getNewUndefinedProjectName() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.fromLocalDatastore();
        int maxNum = 0;
        String returnString = "Untitled Project 0";
        try {
            List<ParseObject> localProjects = query.find();
            System.out.println("localProjects : " + localProjects);
            for(ParseObject object : localProjects) {
                Project project = (Project) object;
                if(project.getName().matches(untitledProjectNameRegex)) {
                    System.out.println("got a match");
                    System.out.println(project.getName());
                    try {
                        int number = Integer.parseInt(project.getName().substring(17));
                        System.out.println(number);
                        if(number >= maxNum) {
                            maxNum = number + 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            returnString = "Untitled Project " + maxNum;
            System.out.println(returnString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public static int getNumberOfSlides() {
        ParseObjectsCollection<Slide> slides = project.getSlides();
        if(slides == null) {
            return 0;
        }
        return slides.size();
    }

    public static Slide getSlideAt(int position) {
        return project.getSlide(position);
    }

    public static void updateAdapterView(int position) {
        projectAdapter.notifyItemChanged(position);
    }

    public static void onSlideChanged(Slide slide) {
        projectAdapter.notifyItemChanged(getSlidePosition(slide));
    }

    // TODO change
//    public List<PlayerModel> getPreviewList() {
//        ArrayList<PlayerModel> list = new ArrayList<>();
//        for (DummyContent.Slide item : ITEMS) {
//            PlayerModel model = convertSlideToPlayerModel(item);
//            if (model != null) {
//                list.add(model);
//            }
//        }
//        return list;
//    }

    public static List<PlayerModel> getPreviewList() {
        ArrayList<PlayerModel> list = new ArrayList<>();
        if(project.getSlides().getAll() != null) {
            for(Slide slide : project.getSlides().getAll()) {
                PlayerModel playerModel = slide.toPlayerModel();
                if(playerModel != null) {
                    list.add(playerModel);
                }
            }
        }
        return list;
    }

    // TODO change
//    public PlayerModel convertSlideToPlayerModel(DummyContent.Slide slide) {
//        PlayerModel model = new PlayerModel(slide.path, slide.getAudioPath());
//        if (slide.slideType == DummyContent.SlideType.IMAGE)
//            return null;
//        switch (slide.slideType.toString()) {
//            case "IA"://Case Image and Audio
//                model.setType(PlayerModel.MediaType.IMAGE_AUDIO);
//                break;
//            case "V"://Case is Video
//                model.setType(PlayerModel.MediaType.VIDEO);
//                break;
//        }
//        return model;
//    }

    // TODO change
    public static int getSlidePosition(Slide item) {
        return project.getSlides().getObjectPosition(item);
    }

    public static void updateAdapterView() {
        projectAdapter.notifyDataSetChanged();
    }

    public static void hidePreviewFab() {
        previewFab.hide();
    }
}
