package com.comp.iitb.vialogue.coordinators;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.MyProjectsAdapter;
import com.comp.iitb.vialogue.library.ExifUtils;
import com.comp.iitb.vialogue.models.BlankImage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.comp.iitb.vialogue.models.QuestionAnswer;
import com.comp.iitb.vialogue.utils.ProjectNameUtils;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
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
    public static final int GET_SINGLE_CAMERA_IMAGE = 548;
    public static final String blockCharacterSet = "~#^|$%&*!/><.,;:{}[]+=-*|()@#%\n";
    public static final int VIDEO_INTERVAL_TIME = 80;

    // Variables
    public static RecyclerView.Adapter projectAdapter;
    public static MyProjectsAdapter myProjectsAdapter = null;
    public static EditText projectName;
    public static TextView projectNameDisplay;

    public static FloatingActionButton previewFab;
    // TODO change implementation for isSelected
    public static boolean isSelected = false;
    public static int selectedPosition;
    public static List<QuestionAnswer> questionsList = new ArrayList<>();
    public static List<BlankImage> blankImages = new ArrayList<>();
    public static AVLoadingIndicatorView loadingAnimation;
    public static MenuItem saveMenuItem;

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
        project.pinInBackground();
        projectAdapter.notifyItemInserted(project.getSlides().size() - 1);
        calculatePreviewFabVisibility();
        calculateSaveMenuItemVisibility();
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
        try {
            projectAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            // Throws an exception when this method is called when the
            // recycler view is being scrolled. Exactly why the onBindViewHolder
            // method in SlidesRecyclerViewAdapterMark2 is being
            // called is still a mystery, but I don't have time to figure it out
            // -- ironstein
            e.printStackTrace();
        }
        calculatePreviewFabVisibility();
    }

    public static void deleteSlide(int position) {
        project.deleteSlide(position);
        project.pinInBackground();
        projectAdapter.notifyItemRemoved(position);
        updateAdapterView(position);
        calculatePreviewFabVisibility();
        calculateSaveMenuItemVisibility();
    }

    public static void justDeleteSlide(int position) {
        System.out.println("size before deleting : " + project.getSlides().getAll().size());
        project.deleteSlide(position);
        project.pinInBackground();
        System.out.println("size after deleting : " + project.getSlides().getAll().size());
        calculatePreviewFabVisibility();
    }

    public static void setProjectName(String name) {
        project.setName(name);
        project.pinInBackground();
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
        calculatePreviewFabVisibility();
        calculateSaveMenuItemVisibility();

        // display project name
        String projectNameString = null;
        if((project.getName() != null) && (project.getName() != "") && (!project.getName().matches(ProjectNameUtils.untitledProjectNameRegex))) {
            projectNameString = project.getName();
        } else {
            projectNameString = "Add project title";
        }

        projectNameDisplay.setText("");
        projectNameDisplay.setHint(projectNameString);
        if(projectNameString != "Add project title") {
            projectName.setText(projectNameString);
        }

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
                                        } else {}

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
                                currentProject.unpinInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Toast.makeText(activity.getBaseContext(), "Project discarded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Save existing Project?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            } else {
                (new AsyncTask<Void, Void, Void>() {

                    @Override
                    public Void doInBackground(Void... params) {
                        if ((currentProject.getName() == null) || (currentProject.getName() == "")) {
                            // save project with a temporary name
                            String newProjectName = ProjectNameUtils.getNewUndefinedProjectName();
                            currentProject.setName(newProjectName);
                        } else {}

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
                        // calculate preview fab visibility based on the new project
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
     * Save Menu Item related methods
     */
    public static void calculateSaveMenuItemVisibility() {
        if(project.getSlides().getAll().size() != 0) {
            if(saveMenuItem != null) {
                saveMenuItem.setVisible(true);
            } else {
                saveMenuItem.setVisible(false);
            }
        }
    }

    /*
     * Preview FAB related methods
     */
    public static void calculatePreviewFabVisibility() {

        if (previewFab == null) {
            return;
        }

        if (getNumberOfSlides() != 0) {
            for (Slide s : project.getSlides().getAll()) {
                if (s.getSlideType() == Slide.SlideType.VIDEO) {
                    previewFab.show();
                    previewFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    return;
                } else if (s.getSlideType() == Slide.SlideType.IMAGE) {
                    if (((Image) s.getResource()).hasAudio()) {
                        previewFab.show();
                        previewFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
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

            // Ironstein - did this because Riha did not want filters in
            // project names
//            if (source != null && SharedRuntimeContent.blockCharacterSet.contains(("" + source))) {
//                return "";
//            }
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
                    Integer[] answerArray = new Integer[question.getCorrectOptions().size()];
                    answerArray = question.getCorrectOptions().toArray(answerArray);
                    String[] stringConvertedAnswers = new String[answerArray.length];
                    for(int iterator=0;iterator<answerArray.length;iterator++)
                        stringConvertedAnswers[iterator]=optionsArray[answerArray[iterator]];
                    questionAnswer.setAnswers(stringConvertedAnswers);
                    positionThatSaves = getSlidePosition(slide);
                    questionAnswer.setTime(getDurationBeforeASlide(positionThatSaves));
                    questionAnswer.setQuestion(question.getQuestionString());
                    list.add(questionAnswer);
                }
            }
        }
        return list;
    }


    public static List<BlankImage> getBlankSlides() {
        int i=0;
        ArrayList<BlankImage> list = new ArrayList<>();
        if (project.getSlides().getAll() != null) {
            int positionThatSaves = 0;
            for (Slide slide : project.getSlides().getAll()) {
                BlankImage one = new BlankImage();
                if(slide.getSlideType().equals(Slide.SlideType.IMAGE)){
                   if((((Image)SharedRuntimeContent.getSlideAt(positionThatSaves).getResource()).doesReallyHaveImage)==false) {
                       one.setPosition(positionThatSaves);
                       one.setTime(getDurationBeforeASlide(positionThatSaves));
                       list.add(one);
                   }
                }
                positionThatSaves++;
            }
        }
        return list;
    }

    public static int getDurationBeforeASlide(int position) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeMilliSec;
    }



    public static void Download(String link, String whereToStore, String folderName, String videoName, Context context){
        boolean isDownloadComplete = false;
        String DownloadUrl = link;
        whereToStore = whereToStore + "/" + folderName;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request.setDescription("Downloading your video");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        File destfile= new File(Environment.getExternalStorageDirectory()+ whereToStore);
        if(!destfile.exists()){
            destfile.mkdir();
        }
        request.setDestinationInExternalPublicDir(whereToStore, videoName);
        DownloadManager manager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap decodeFile(String filePath) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
        Bitmap b= ExifUtils.rotateBitmap(filePath, b1);
        return b;
        // image.setImageBitmap(bitmap);
    }



}
