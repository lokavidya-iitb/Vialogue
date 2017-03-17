package com.comp.iitb.vialogue.coordinators;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.MyProjectsAdapter;
import com.comp.iitb.vialogue.adapters.SlideRecyclerViewAdapter;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.comp.iitb.vialogue.models.QuestionAnswer;
import com.comp.iitb.vialogue.utils.ProjectNameUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wang.avi.AVLoadingIndicatorView;

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
    public static final int CROP_MAIN_ACTIVITY_RESULT = 545;
    public static final int GET_MULTIPLE_IMAGES = 546;
    public static final int GET_MULTIPLE_CAMERA_IMAGES = 547;
    public static final String blockCharacterSet = "~#^|$%&*!/><.,;:{}[]+=-*|()@#%\n";

    // Variables
    public static SlideRecyclerViewAdapter projectAdapter;
    public static MyProjectsAdapter myProjectsAdapter = null;
    public static EditText projectName;
    public static TextView projectNameDisplay;

    public static FloatingActionButton previewFab;
    // TODO change implementation for isSelected
    public static boolean isSelected = false;
    public static int selectedPosition;
    public static List<QuestionAnswer> questionsList = new ArrayList<>();
    public static AVLoadingIndicatorView loadingAnimation;

    /*
     * All the Project related methods
     */
    private static Project project = new Project();

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project newProject) {
        project = newProject;
    }

    public static boolean addSlide(Slide slide) throws Exception {
        project.addSlide(slide);
        slide.pinInBackground();
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

    public static void pinProject(Context context, Project project) {

        // save project with a temporary name
        if ((project.getName() == null) || (project.getName() == "")) {
            String newProjectName = ProjectNameUtils.getNewUndefinedProjectName();
            project.setName(newProjectName);
        } else {}

        if (project.getSlides().getAll().size() == 0) {
            return;
        }

        try {
            project.pin();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void pinProject(Context context) {
        pinProject(context, project);
    }

    public static void pinProjectInBackground(final Context context, final OnProjectSaved onProjectSaved) {
        (new AsyncTask<Void, Void, Void>() {

            @Override
            public Void doInBackground(Void... params) {
                pinProject(context);
                return null;
            }

            @Override
            public void onPostExecute(Void result) {
                onProjectSaved.done(true);
            }
        }).execute();
    }

    public static void createEmptyProject(Activity activity) {
        loadNewProject(activity, new Project());
    }

    public static void loadNewProject(final Activity activity, final Project newProject) {
        // cache current project
        final Project currentProject = project;

        // load new project
        project = newProject;
        updateAdapterView();

        // display project name
        String projectNameString = null;
        if((SharedRuntimeContent.getProjectName() != null) && (!SharedRuntimeContent.getProjectName().matches(ProjectNameUtils.untitledProjectNameRegex))) {
            projectNameString = project.getName();
        } else {
            projectNameString = "Add project title";
        }
        projectNameDisplay.setText(projectNameString);
        projectName.setText(projectNameString);
        projectNameDisplay.setHint(projectNameString);
        projectName.setHint(projectNameString);

        if(currentProject.getSlides().getAll().size() != 0) {

            if(!currentProject.doesItExistInLocalDatastore()) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                (new AsyncTask<Void, Void, Void>() {

                                    @Override
                                    public Void doInBackground(Void... params) {
                                        if ((currentProject.getName() == null) || (currentProject.getName() == "")) {
                                            // save project with a temporary name
                                            String newProjectName = ProjectNameUtils.getNewUndefinedProjectName();
                                            currentProject.setName(newProjectName);
                                        } else {
                                        }

                                        // save existing project
                                        pinProject(activity.getBaseContext(), currentProject);
                                        return null;
                                    }

                                    @Override
                                    public void onPostExecute(Void result) {
                                        // refresh InceptionMyProjects view to accommodate the changes
                                        // in the current project (if the name is changed, or the project
                                        // was not previously displayed (because it was new))
                                        myProjectsAdapter.addProject(currentProject);
                                        Toast.makeText(activity.getBaseContext(), "Project saved successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).execute();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Save existing Project?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            } else {
                (new AsyncTask<Void, Void, Void>() {

                    @Override
                    public Void doInBackground(Void... params) {
                        if ((currentProject.getName() == null) || (currentProject.getName() == "")) {
                            // save project with a temporary name
                            String newProjectName = ProjectNameUtils.getNewUndefinedProjectName();
                            currentProject.setName(newProjectName);
                        } else {
                        }

                        // save existing project
                        pinProject(activity.getBaseContext(), currentProject);
                        return null;
                    }

                    @Override
                    public void onPostExecute(Void result) {
                        // refresh InceptionMyProjects view to accommodate the changes
                        // in the current project (if the name is changed, or the project
                        // was not previously displayed (because it was new))
                        myProjectsAdapter.addProject(currentProject);
                        Toast.makeText(activity.getBaseContext(), "Project saved successfully", Toast.LENGTH_SHORT).show();
                    }
                }).execute();
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

    public static int getNumberOfSlides() {
        ParseObjectsCollection<Slide> slides = project.getSlides();
        if (slides == null) {
            return 0;
        }
        return slides.size();
    }

    public static Slide getSlideAt(int position) {
        return project.getSlide(position);
    }

    public static int getSlidePosition(Slide item) {
        return project.getSlides().getObjectPosition(item);
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
