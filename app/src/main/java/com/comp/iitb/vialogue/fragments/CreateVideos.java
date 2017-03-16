package com.comp.iitb.vialogue.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.App;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.CameraActivity;
import com.comp.iitb.vialogue.activity.CropMainActivity;
import com.comp.iitb.vialogue.coordinators.ConditionListener;
import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.OnFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.listeners.CameraImagePicker;
import com.comp.iitb.vialogue.listeners.MultipleImagePicker;
import com.comp.iitb.vialogue.listeners.ChangeVisibilityOnFocus;
import com.comp.iitb.vialogue.listeners.ClearFocusTouchListener;
import com.comp.iitb.vialogue.listeners.FileCopyUpdateListener;
import com.comp.iitb.vialogue.listeners.ProjectTextWatcher;
import com.comp.iitb.vialogue.listeners.QuestionPickerClick;
import com.comp.iitb.vialogue.listeners.SwitchVisibilityClick;
import com.comp.iitb.vialogue.listeners.VideoPickerClick;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Video;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.sangcomz.fishbun.define.Define;
import com.squareup.leakcanary.RefWatcher;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_CAMERA_IMAGE;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_IMAGE;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_VIDEO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateVideos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateVideos extends Fragment implements OnProgressUpdateListener, ConditionListener {
    private Storage mStorage;
    private Button mImagePicker;
    private Button mVideoPicker;
    private Button mCameraPicker;
    private Button mQuestionPicker;
    private EditText mProjectName;
    private TextView mProjectNameDisplay;
    private SlideFragment mSlideFragment;
    private View mView;
    private OnFragmentInteractionListener mListener;
    private LinearLayout mRoot;
    private Fragment mFragment;
    private Project mProject;
    private AVLoadingIndicatorView mLoadingAnimation;

    private CameraImagePicker mCameraImagePicker;
    private MultipleImagePicker mMultipleImagePicker;

    // Required empty public constructor
    public CreateVideos() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateVideos.
     */
    public static CreateVideos newInstance() {
        CreateVideos fragment = new CreateVideos();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView : called");
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_create_videos, container, false);

        //Initialize Storage
        mStorage = new Storage(getContext());

        // Initialize UI Components
        mProjectName = (EditText) mView.findViewById(R.id.project_name);
        mProjectNameDisplay = (TextView) mView.findViewById(R.id.project_name_display);
        mProjectNameDisplay.setOnClickListener(new SwitchVisibilityClick(getContext(), mProjectNameDisplay, mProjectName));
        mRoot = (LinearLayout) mView.findViewById(R.id.create_videos_root);
        mLoadingAnimation = (AVLoadingIndicatorView) mView.findViewById(R.id.loading_animation);
        SharedRuntimeContent.loadingAnimation = mLoadingAnimation;

        //Load Pickers
        mImagePicker = (Button) mView.findViewById(R.id.image_picker);
        mVideoPicker = (Button) mView.findViewById(R.id.video_picker);
        mQuestionPicker = (Button) mView.findViewById(R.id.question_picker);
        mCameraPicker = (Button) mView.findViewById(R.id.camera_image_picker);

        // Initialize State
        if((SharedRuntimeContent.getProjectName() != null) && (!SharedRuntimeContent.getProjectName().matches(SharedRuntimeContent.untitledProjectNameRegex))) {
            mProjectNameDisplay.setText(SharedRuntimeContent.getProjectName());
            mProjectName.setText(SharedRuntimeContent.getProjectName());
            mProjectNameDisplay.setHint(SharedRuntimeContent.getProjectName());
            mProjectName.setHint(SharedRuntimeContent.getProjectName());
        }

        // Add Listeners
        mProjectName.setOnFocusChangeListener(new ChangeVisibilityOnFocus(mProjectName, mProjectNameDisplay));
        mProjectName.addTextChangedListener(new ProjectTextWatcher(mProjectNameDisplay));
        mProjectName.setFilters(new InputFilter[] { SharedRuntimeContent.filter });

        // Camera Image Picker
//        mCameraImagePicker = new CameraImagePicker(mStorage, this, getContext());
//        mCameraPicker.setOnClickListener(mCameraImagePicker);
        mCameraPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CameraActivity.class);
                startActivityForResult(intent, SharedRuntimeContent.GET_MULTIPLE_CAMERA_IMAGES);
            }
        });
        //Image Picker
        //        ImagePickerClick imagePickerClickListener = new ImagePickerClick(this);
        mMultipleImagePicker = new MultipleImagePicker(getContext(), CreateVideos.this);
        mImagePicker.setOnClickListener(mMultipleImagePicker);
        //Video Picker
        VideoPickerClick videoPickerClickListener = new VideoPickerClick(this);
        mVideoPicker.setOnClickListener(videoPickerClickListener);
        //Question Picker
        QuestionPickerClick questionPickerClickListener = new QuestionPickerClick(getActivity(), CreateVideos.this);
        mQuestionPicker.setOnClickListener(questionPickerClickListener);

        //set SlideLayout
        mSlideFragment = SlideFragment.newInstance(3);
        getFragmentManager().beginTransaction().add(R.id.create_videos_root, mSlideFragment).commit();

        //If User clicks at a place other than project Name EditText should convert to textView loosing focus
        FrameLayout touchInterceptor = (FrameLayout) mView.findViewById(R.id.touch_interceptor);
        touchInterceptor.setOnTouchListener(new ClearFocusTouchListener(mProjectName));

        SharedRuntimeContent.calculatePreviewFabVisibility();
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            SharedRuntimeContent.previewFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            SharedRuntimeContent.calculatePreviewFabVisibility();
        } else {}
    }

    public void setUpNewProject() {

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            // ...
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getName(), requestCode + " " + resultCode);
        if (resultCode == RESULT_OK) {
            handlePickedData(requestCode, data);
        } else {
            Toast.makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
        }
    }

    private String mFilePath = null;

    public void handlePickedData(int requestCode, Intent data) {
        System.out.println("handlePickedData : called");
        Log.d(getClass().getName(), "data " + String.valueOf(data == null));

        if (requestCode == GET_CAMERA_IMAGE && data == null) {
            // CAPTURE IMAGE FROM CAMERA
            Intent intent = new Intent(getContext(), CropMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("from", "CreateVideos");
            bundle.putString(CropMainActivity.IMAGE_PATH, mCameraImagePicker.getCameraFile().getAbsolutePath());
            intent.putExtras(bundle);
            mFragment.startActivity(intent);

        } else if (requestCode == GET_IMAGE) {
            // GET IMAGE FROM GALLERY
            if (data != null) {
                String selectedPath = mStorage.getRealPathFromURI(data.getData());
                Intent intent = new Intent(getContext(), CropMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from", "CreateVideos");
                bundle.putString(CropMainActivity.IMAGE_PATH, selectedPath);
                intent.putExtras(bundle);
                mFragment.startActivity(intent);
            } else {
                // TODO maybe show a toast
            }

        } else if (requestCode == GET_VIDEO) {
            // GET VIDEO FROM GALLERY
            if (data != null) {

                System.out.println(mStorage.getRealPathFromURI(data.getData()));
                System.out.println(data.getData());

                try {
                    new File(mStorage.getRealPathFromURI(data.getData()));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "The selected video file is either corrupted or not supported", Toast.LENGTH_LONG).show();
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
                                    if(!SharedRuntimeContent.addSlide(slide)) {
                                        throw new Exception();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                // TODO maybe show a toast
            }

        } else if(requestCode == SharedRuntimeContent.GET_QUESTION) {
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
                Toast.makeText(getContext(),  R.string.wrongBuddy, Toast.LENGTH_SHORT);
            }
        } else if(requestCode == SharedRuntimeContent.GET_MULTIPLE_IMAGES) {
            ArrayList<Uri> paths = new ArrayList<>();
            for(int i=0; i<data.getParcelableArrayListExtra(Define.INTENT_PATH).size(); i++) {
                paths.add(Uri.parse(data.getParcelableArrayListExtra(Define.INTENT_PATH).get(i).toString()));
            }

            for(Uri uri : paths) {
                Slide slide = new Slide();
                final Image image = new Image(getContext());

                mStorage.addFileToDirectory(
                        new File(mStorage.getRealPathFromURI(uri)),
                        image.getResourceFile(),
                        new FileCopyUpdateListener(getContext()),
                        new OnFileCopyCompleted() {
                            @Override
                            public void done(File file, boolean isSuccessful) {

                                Slide slide = new Slide();
                                try {
                                    slide.addResource(image, Slide.ResourceType.IMAGE);
                                    if(!SharedRuntimeContent.addSlide(slide)) {
                                        throw new Exception();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        } else if(requestCode == SharedRuntimeContent.GET_MULTIPLE_CAMERA_IMAGES) {
            ArrayList<String> paths = data.getStringArrayListExtra(CameraActivity.RESULT_KEY);

            for(String path : paths) {

                try {
                    Slide slide = new Slide();
                    Image image = new Image(Uri.fromFile(new File(path)));
                    slide.addResource(image, Slide.ResourceType.IMAGE);
                    if(!SharedRuntimeContent.addSlide(slide)) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onProgressUpdate(int progress) {

    }


    @Override
    public void conditionSatisfied(EditText sender) {

    }

    @Override
    public void conditionFailed(EditText sender) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("CreateVideos", "onDestroyView : called");
//        RefWatcher refWatcher = App.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }
}
