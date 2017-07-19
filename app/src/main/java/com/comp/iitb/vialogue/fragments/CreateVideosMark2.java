package com.comp.iitb.vialogue.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.AudioRecordActivity;
import com.comp.iitb.vialogue.activity.CameraActivity;
import com.comp.iitb.vialogue.adapters.SlidesRecyclerViewAdapterMark2;
import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.OnListFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.dialogs.ChooseImageDialog;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.listeners.ChangeVisibilityOnFocus;
import com.comp.iitb.vialogue.listeners.FileCopyUpdateListener;
import com.comp.iitb.vialogue.listeners.GridLayoutItemTouchHelperCallback;
import com.comp.iitb.vialogue.listeners.ImagePickerClick;
import com.comp.iitb.vialogue.listeners.MultipleImagePicker;
import com.comp.iitb.vialogue.listeners.ProjectTextWatcher;
import com.comp.iitb.vialogue.listeners.QuestionPickerClick;
import com.comp.iitb.vialogue.listeners.SwitchVisibilityClick;
import com.comp.iitb.vialogue.listeners.VideoPickerClick;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Video;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.utils.ProjectNameUtils;
import com.darsh.multipleimageselect.helpers.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_IMAGE;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_VIDEO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateVideosMark2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateVideosMark2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateVideosMark2 extends Fragment {

    // UI Components
    private Button mImagePicker;
    private Button mVideoPicker;
    private Button mAudioPicker;
    private Button mQuestionPicker;
    private EditText mProjectName;
    private TextView mProjectNameDisplay;
    private AVLoadingIndicatorView mLoadingAnimationView;
    private RecyclerView mSlidesRecyclerView;
    private LinearLayout mCreateVideosRootView;

    // variables
    private OnListFragmentInteractionListener mListener;
    private SlidesRecyclerViewAdapterMark2 mSlideRecyclerViewAdapter;
    private ItemTouchHelper.Callback mItemTouchHelperCallback;
    private ItemTouchHelper mItemTouchHelper;
    private MultipleImagePicker mMultipleImagePicker;
    private Storage mStorage;
    // temp
    private ImagePickerClick mImagePickerClick;

    // Required empty public constructor
    public CreateVideosMark2() {}

    public static CreateVideosMark2 newInstance() {
        CreateVideosMark2 fragment = new CreateVideosMark2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("container's id : "+ container.getId());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_videos_mark2, container, false);

        // Initialize UI Components
        mImagePicker = (Button) view.findViewById(R.id.image_picker);
        mAudioPicker = (Button) view.findViewById(R.id.camera_image_picker);
        mVideoPicker = (Button) view.findViewById(R.id.video_picker);
        mQuestionPicker = (Button) view.findViewById(R.id.question_picker);
        mProjectName = (EditText) view.findViewById(R.id.project_name);
        mProjectNameDisplay = (TextView) view.findViewById(R.id.project_name_display);
        mLoadingAnimationView = (AVLoadingIndicatorView) view.findViewById(R.id.loading_animation);
        mSlidesRecyclerView = (RecyclerView) view.findViewById(R.id.slides_recycler_view);
        mCreateVideosRootView = (LinearLayout) view.findViewById(R.id.create_videos_root);

        // instantiate Variables
        mSlidesRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        mSlideRecyclerViewAdapter = new SlidesRecyclerViewAdapterMark2(view.getContext(), mListener, mSlidesRecyclerView);
        mItemTouchHelperCallback = new GridLayoutItemTouchHelperCallback(mSlideRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mStorage = new Storage(getContext());

        // Initialize dependent variables in ShredRuntimeContent
        SharedRuntimeContent.loadingAnimation = mLoadingAnimationView;
        SharedRuntimeContent.projectName = mProjectName;
        SharedRuntimeContent.projectNameDisplay = mProjectNameDisplay;
        SharedRuntimeContent.projectAdapter = mSlideRecyclerViewAdapter;

        // Initialize State
        if((SharedRuntimeContent.getProjectName() != null) && (!SharedRuntimeContent.getProjectName().matches(ProjectNameUtils.untitledProjectNameRegex))) {
            mProjectNameDisplay.setText(SharedRuntimeContent.getProjectName());
            mProjectName.setText(SharedRuntimeContent.getProjectName());
            mProjectNameDisplay.setHint(SharedRuntimeContent.getProjectName());
            mProjectName.setHint(SharedRuntimeContent.getProjectName());
        }

        // Add Listeners
        mProjectNameDisplay.setOnClickListener(new SwitchVisibilityClick(getContext(), mProjectNameDisplay, mProjectName));
        mProjectName.setOnFocusChangeListener(new ChangeVisibilityOnFocus(mProjectName, mProjectNameDisplay));
        mProjectName.addTextChangedListener(new ProjectTextWatcher(mProjectNameDisplay));
        mProjectName.setFilters(new InputFilter[] { SharedRuntimeContent.filter });

        // Audio Picker
        mAudioPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new AsyncTask<Void, Void, Void>() {
                    private Image mImage;
                    private ProgressDialog mProgressDialog;

                    @Override
                    public void onPreExecute() {
                        mProgressDialog = ProgressDialog.show(getContext(), "Creating New Slide", "Please Wait...");
                }

                    @Override
                    public Void doInBackground(Void... params) {
                        mImage = new Image(getContext());
                        mImage.doesReallyHaveImage = false;
                        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image);
                        mStorage.saveBitmapToFile(mImage.getResourceFile(), bitmap);
                        return null;
                    }

                    @Override
                    public void onPostExecute(Void result) {
                        Slide slide = new Slide();
                        try {
                            slide.addResource(mImage, Slide.ResourceType.IMAGE);
                            SharedRuntimeContent.addSlide(slide);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mProgressDialog.dismiss();

                        // start AudioRecordActivity
                        Intent intent = new Intent(getContext(), AudioRecordActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(AudioRecordActivity.SLIDE_NO, SharedRuntimeContent.getAllSlides().size()-1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).execute();
            }
        });

        //Image Picker
        mImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImageDialog d = new ChooseImageDialog(getActivity(), CreateVideosMark2.this);
                d.show();
            }
        });

        //Video Picker
        VideoPickerClick videoPickerClickListener = new VideoPickerClick(this);
        mVideoPicker.setOnClickListener(videoPickerClickListener);
        //Question Picker
        QuestionPickerClick questionPickerClickListener = new QuestionPickerClick(getActivity(), CreateVideosMark2.this);
        mQuestionPicker.setOnClickListener(questionPickerClickListener);
        // slideRecyclerView
        mSlidesRecyclerView.setAdapter(mSlideRecyclerViewAdapter);
        mItemTouchHelper.attachToRecyclerView(mSlidesRecyclerView);

        // return the inflated view
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            SharedRuntimeContent.calculatePreviewFabVisibility();
        } else {
            try {
                View view = getActivity().getCurrentFocus();
                if(view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
            } catch (Exception e) {}
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getName(), requestCode + " " + resultCode);
        if (resultCode == RESULT_OK) {
            handlePickedData(requestCode, data);
        } else {
            makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
        }
    }

    private String mFilePath = null;

    public void handlePickedData(int requestCode, Intent data) {
        System.out.println("handlePickedData : called");
        Log.d(getClass().getName(), "data " + String.valueOf(data == null));

        if (requestCode == GET_VIDEO) {
            // GET VIDEO FROM GALLERY
            if (data != null) {

                System.out.println(mStorage.getRealPathFromURI(data.getData()));
                System.out.println(data.getData());

                try {
                    new File(mStorage.getRealPathFromURI(data.getData()));
                } catch (Exception e) {
                    makeText(getContext(), "The selected video file is either corrupted or not supported", Toast.LENGTH_LONG).show();
                    return;
                }

                final Video video = new Video(getContext());
                final File v = video.getResourceFile();
                mStorage.addFileToDirectory(
                        new File(mStorage.getRealPathFromURI(data.getData())),
                        v,
                        new FileCopyUpdateListener(getContext()),
                        new OnFileCopyCompleted() {
                            @Override
                            public void done(File file, boolean isSuccessful) {

                                Slide slide = new Slide();
                                try {
                                    slide.addResource(video, Slide.ResourceType.VIDEO);
                                    if (!SharedRuntimeContent.addSlide(slide)) {
                                        throw new Exception();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
                                }

                                MediaMetadataRetriever m = new MediaMetadataRetriever();
                                m.setDataSource(v.getAbsolutePath());
                                String orientation = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
                                if(Integer.parseInt(orientation) != 0) {
                                    Toast.makeText(getContext(), "The imported video has been taken portrait mode. As such, it will be displayed as rotated by 90 degrees in the stitched video.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                );
            } else {
                // TODO maybe show a toast
            }

        } else if (requestCode == SharedRuntimeContent.GET_QUESTION) {
            // GET QUESTION
            Bundle extras = data.getExtras();
            Question question = new Question(
                    extras.getString(Question.Fields.QUESTION_STRING_FIELD),
                    extras.getString(Question.Fields.QUESTION_TYPE_FIELD),
                    extras.getStringArrayList(Question.Fields.OPTIONS_FIELD),
                    extras.getIntegerArrayList(Question.Fields.CORRECT_OPTIONS_FIELD),
                    extras.getString(Question.Fields.SOLUTION_FIELD),
                    extras.getStringArrayList(Question.Fields.HINTS_FIELD),
                    extras.getBoolean(Question.Fields.IS_COMPULSORY_FIELD, true)
            );

            Slide slide = new Slide();
            try {
                slide.addResource(question, Slide.ResourceType.QUESTION);
                SharedRuntimeContent.addSlide(slide);
            } catch (Exception e) {
                e.printStackTrace();
                makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT);
            }
        } else if (requestCode == Constants.REQUEST_CODE) {
            ArrayList<com.darsh.multipleimageselect.models.Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            ArrayList<String> paths = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                paths.add(images.get(i).path.toString());
            }

            (new AsyncTask<Void, Void, Void>() {
                private ProgressDialog mProgressDialog;
                private ArrayList<Slide> mSlides;

                @Override
                public void onPreExecute() {
                    mProgressDialog = ProgressDialog.show(getContext(), "Importing Images", "Please Wait...");
                    mSlides = new ArrayList<Slide>();
                }

                @Override
                public Void doInBackground(Void... params) {
                    for (String path : paths) {
                        Uri resizedImage = Image.getResizedImage(getContext(), Uri.parse(path));
                        if(resizedImage == null) {
                            makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            continue;
                        }
                        Slide slide = new Slide();
                        Image image = new Image(resizedImage);
                        try {
                            slide.addResource(image, Slide.ResourceType.IMAGE);
                            mSlides.add(slide);
                        } catch (Exception e) {
                            e.printStackTrace();
                            makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
                        }
                    }
                    return null;
                }

                @Override
                public void onPostExecute(Void result) {
                    for(Slide slide: mSlides) {
                        try {
                            if (!SharedRuntimeContent.addSlide(slide)) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
                        }
                    }
                    mProgressDialog.dismiss();
                }
            }).execute();

        } else if (requestCode == SharedRuntimeContent.GET_MULTIPLE_CAMERA_IMAGES) {
            ArrayList<String> paths = data.getStringArrayListExtra(CameraActivity.RESULT_KEY);
            System.out.print("-------------reached" + paths.toString());
            for (String path : paths) {

                try {
                    Slide slide = new Slide();
                    Image image = new Image(Uri.fromFile(new File(path)));
                    slide.addResource(image, Slide.ResourceType.IMAGE);
                    if (!SharedRuntimeContent.addSlide(slide)) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
                }

            }
        } else if (requestCode == GET_IMAGE) {
            // GET IMAGE FROM GALLERY

            if (data != null) {

                System.out.println(mStorage.getRealPathFromURI(data.getData()));
                System.out.println(data.getData());

                try {
                    new File(mStorage.getRealPathFromURI(data.getData()));
                } catch (Exception e) {
                    makeText(getContext(), "The selected video file is either corrupted or not supported", Toast.LENGTH_LONG).show();
                    return;
                }

                final Image image = new Image(getContext());
                final File i = image.getResourceFile();
                mStorage.addFileToDirectory(
                        new File(mStorage.getRealPathFromURI(data.getData())),
                        i,
                        new FileCopyUpdateListener(getContext()),
                        new OnFileCopyCompleted() {
                            @Override
                            public void done(File file, boolean isSuccessful) {

                                Slide slide = new Slide();
                                try {
                                    slide.addResource(image, Slide.ResourceType.IMAGE);
                                    if (!SharedRuntimeContent.addSlide(slide)) {
                                        throw new Exception();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                // TODO maybe show a toast
            }

        }
    }
}
