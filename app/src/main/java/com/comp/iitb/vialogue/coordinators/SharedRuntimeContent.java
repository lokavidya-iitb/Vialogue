package com.comp.iitb.vialogue.coordinators;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.design.widget.FloatingActionButton;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.SlideRecyclerViewAdapter;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.comp.iitb.vialogue.models.QuestionAnswer;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer.PlayerModel;

/**
 * Created by shubh on 11-01-2017.
 */

public class SharedRuntimeContent {

    // Constants
    public static final int GET_IMAGE = 541;
    public static final int GET_VIDEO = 542;
    public static final int GET_CAMERA_IMAGE = 543;
    public static final int GET_QUESTION = 544;
    public static final String blockCharacterSet = "~#^|$%&*!/><.,;:{}[]+=-*|()@#%\n";
    public static String untitledProjectNameRegex = "(^)Untitled Project ([0-9].*$)";

    // Variables
    public static SlideRecyclerViewAdapter projectAdapter;
    public static FloatingActionButton previewFab;
    // TODO change implementation for isSelected
    public static boolean isSelected = false;
    public static int selectedPosition;
    public static List<QuestionAnswer> questionsList = new ArrayList<>();

    /*
     * All the Project related methods
     */
    public static Project project = new Project();

    public static boolean addSlide(Slide slide) throws Exception {
        project.addSlide(slide);
//        project.addSlide(slide.deepCopy());
        projectAdapter.notifyItemInserted(project.getSlides().size() - 1);
        calculatePreviewFabVisibility();
        return true;
    }

    public static void changeSlideAtPosition(int position, Slide slide) {
        deleteSlide(position);
        addSlideAtPosition(position, slide);
    }

    public static void addSlideAtPosition(int position, Slide slide) {
        try {
            addSlide(slide);
        } catch (Exception e) {
            Log.e("addSlideAtPosition", "failed");
            e.printStackTrace();
        }
        changeSlidePosition(getNumberOfSlides() - 1, position);
    }

    public static ArrayList<Slide> getAllSlides() {
        return project.getSlides().getAll();
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

    public static void setProjectName(String name) {
        project.setName(name);
    }

    public static String getProjectName() {
        return project.getName();
    }

    public static void pinProject(Context context) {
        // save project with a temporary name
        if ((getProjectName() == null) || (getProjectName() == "")) {
            String newProjectName = getNewUndefinedProjectName();
            setProjectName(newProjectName);
        } else {
        }

        if (getNumberOfSlides() != 0) {
            try {
                project.pinParseObject();
                System.out.println("project pinned");
            } catch (ParseException e) {
                Toast.makeText(context, R.string.wrongWhileSaving, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public static void pinProjectInBackground(Context context) {
        // save project with a temporary name
        if ((getProjectName() == null) || (getProjectName() == "")) {
            String newProjectName = getNewUndefinedProjectName();
            setProjectName(newProjectName);
        } else {}

        if (getNumberOfSlides() != 0) {
            try {
                project.pinParseObjectInBackground();
                System.out.println("project pinned");
            } catch (ParseException e) {
                Toast.makeText(context, R.string.wrongWhileSaving, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Project> getLocalProjects() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.fromLocalDatastore();

        ArrayList<Project> localProjects = new ArrayList<Project>();
        try {
            List<ParseObject> localObjects = query.find();
            for (ParseObject localObject : localObjects) {
                Project project = (Project) localObject;
                project.fetchChildrenObjects();
                localProjects.add(project);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localProjects;
    }

    public static Project addThumbnailsToProject(Project project, Context context, Storage storage) {
        ParseObjectsCollection<Slide> slides = new ParseObjectsCollection<>();
        for (Slide s : project.getSlides().getAll()) {
            s.setThumbnail(context, storage);
            slides.addObject(s);
        }
        project.setSlides(slides);
        return project;
    }

    public static String getNewUndefinedProjectName() {

        ArrayList<Project> localProjects = getLocalProjects();
        int maxNum = 0;
        for (Project project : localProjects) {
            if (project.getName().matches(untitledProjectNameRegex)) {
                try {
                    int number = Integer.parseInt(project.getName().substring(17));
                    if (number >= maxNum) {
                        maxNum = number + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "Untitled Project " + maxNum;
    }

    public static int getNumberOfSlides() {
        ParseObjectsCollection<Slide> slides = project.getSlides();
        if (slides == null) {
            return 0;
        }
        System.out.println("slides.size : " + slides.size());
        System.out.println("slides.size : " + slides.size());
        return slides.size();
    }

    public static Slide getSlideAt(int position) {
        return project.getSlide(position);
    }

    public static int getSlidePosition(Slide item) {
        return project.getSlides().getObjectPosition(item);
    }

    public static void createEmptyProject(Context context) {
        pinProjectInBackground(context);
        project = new Project();
        updateAdapterView();
    }

    /*
     * Preview FAB related methods
     */
    public static void calculatePreviewFabVisibility() {

        if (previewFab == null) {
            return;
        }

        if (getNumberOfSlides() == 0) {
        } else {
            for (Slide s : project.getSlides().getAll()) {
                if (s.getSlideType() == Slide.SlideType.VIDEO) {
                    previewFab.show();
                    return;
                } else if (s.getSlideType() == Slide.SlideType.IMAGE) {
                    if (((Image) s.getResource()).hasAudio()) {
                        previewFab.show();
                        return;
                    }
                }
            }
        }
        previewFab.hide();
    }

    public static void hidePreviewFab() {
        previewFab.hide();
    }

    /*
     * ProjectAdapter related methods
     */
    public static void updateAdapterView(int position) {
        if(projectAdapter != null) {
            projectAdapter.notifyItemChanged(position);
        }
    }

    public static void updateAdapterView() {
        if(projectAdapter != null) {
            projectAdapter.notifyDataSetChanged();
        }
    }

    public static void onSlideChanged(Slide slide) {
        projectAdapter.notifyItemChanged(getSlidePosition(slide));
    }

    /*
     * Validation related methods
     */
    public static InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && SharedRuntimeContent.blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public static boolean validateEditTextForNull(EditText E)
    {
        return true;
    }

    /*
     * UploadVideoActivity related methods
     */
    public static List<PlayerModel> getPreviewList() {
        ArrayList<PlayerModel> list = new ArrayList<>();
        if (project.getSlides().getAll() != null) {
            for (Slide slide : project.getSlides().getAll()) {
                PlayerModel playerModel = slide.toPlayerModel();
                if (playerModel != null) {
                    list.add(playerModel);
                }
            }
        }
        return list;
    }

    public static List<QuestionAnswer> getQuestions() {
        int i=0;
        ArrayList<QuestionAnswer> list = new ArrayList<>();
        if (project.getSlides().getAll() != null) {
            int positionThatSaves = 0;
            for (Slide slide : project.getSlides().getAll()) {
                if(slide.getSlideType().equals(Slide.SlideType.QUESTION)){
                    Question question = (Question) slide.getResource();
                    QuestionAnswer questionAnswer = new QuestionAnswer();
                    String[] optionsArray = new String[question.getOptions().size()];
                    optionsArray = question.getOptions().toArray(optionsArray);
                    questionAnswer.setOptions(optionsArray);
                    positionThatSaves = getSlidePosition(slide);
                    questionAnswer.setTime(getDurationThatSavesQuestion(positionThatSaves));
                    questionAnswer.setQuestion(question.getQuestionString());
                    list.add(questionAnswer);
                }
            }
        }
        return list;
    }

    public static int getDurationThatSavesQuestion(int position) {
        int totalTime=0;
        List<Slide> slides = getAllSlides();
        for(int stub=0;stub<position;stub++)
        {
            if(slides.get(stub).getSlideType() == Slide.SlideType.IMAGE) {
                if(!(((Image) slides.get(stub).getResource()).hasAudio())) {
                    continue;
                }
                else
                    totalTime+= getASlideDuration(((Image) slides.get(stub).getResource()).getAudio().getResourceFile().getAbsolutePath());

            } else if(slides.get(stub).getSlideType() == Slide.SlideType.VIDEO) {

                totalTime+= getASlideDuration((slides.get(stub).getResource().getResourceFile().getAbsolutePath()));
            } else  {
            }
        }
        return totalTime;
    }


    public static long getASlideDuration(String url) {
        MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
        long timeMilliSec = 0;
        try {
            FileInputStream inputStream = new FileInputStream(url);
            mRetriever.setDataSource(inputStream.getFD());
            timeMilliSec = Long.parseLong(mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            inputStream.close();
            mRetriever.release();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return timeMilliSec;
    }


}
