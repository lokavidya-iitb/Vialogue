package com.comp.iitb.vialogue.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.CropMainActivity;
import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.OnFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.listeners.CameraImagePicker;
import com.comp.iitb.vialogue.listeners.ChangeVisibilityOnFocus;
import com.comp.iitb.vialogue.listeners.ClearFocusTouchListener;
import com.comp.iitb.vialogue.listeners.FileCopyUpdateListener;
import com.comp.iitb.vialogue.listeners.ImagePickerClick;
import com.comp.iitb.vialogue.listeners.ProjectTextWatcher;
import com.comp.iitb.vialogue.listeners.QuestionPickerClick;
import com.comp.iitb.vialogue.listeners.SwitchVisibilityClick;
import com.comp.iitb.vialogue.listeners.VideoPickerClick;
import com.comp.iitb.vialogue.models.DummyContent;

import java.io.File;

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
public class CreateVideos extends Fragment implements OnProgressUpdateListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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
    private File mFolder;
    private Fragment mFragment;

    private CameraImagePicker mCameraImagePicker;

    public CreateVideos() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateVideos.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateVideos newInstance() {
        CreateVideos fragment = new CreateVideos();
        Bundle args = new Bundle();/*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFragment = this;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_create_videos, container, false);
        //Initialize Storage
        mStorage = new Storage(getContext());
        mProjectName = (EditText) mView.findViewById(R.id.project_name);
        mProjectNameDisplay = (TextView) mView.findViewById(R.id.project_name_display);
        mProjectNameDisplay.setOnClickListener(new SwitchVisibilityClick(getContext(), mProjectNameDisplay, mProjectName));
        mProjectName.setOnFocusChangeListener(new ChangeVisibilityOnFocus(mProjectName, mProjectNameDisplay));
        mRoot = (LinearLayout) mView.findViewById(R.id.create_videos_root);


        //Load Pickers
        mImagePicker = (Button) mView.findViewById(R.id.image_picker);
        mVideoPicker = (Button) mView.findViewById(R.id.video_picker);
        mQuestionPicker = (Button) mView.findViewById(R.id.question_picker);
        mCameraPicker = (Button) mView.findViewById(R.id.camera_image_picker);

        mCameraImagePicker = new CameraImagePicker(mStorage, this);
        mCameraPicker.setOnClickListener(mCameraImagePicker);

        //Image Picker
        ImagePickerClick imagePickerClickListener = new ImagePickerClick(this);
        mImagePicker.setOnClickListener(imagePickerClickListener);

        //Video Picker
        VideoPickerClick videoPickerClickListener = new VideoPickerClick(this);
        mVideoPicker.setOnClickListener(videoPickerClickListener);

        //Question Picker
        QuestionPickerClick questionPickerClickListener = new QuestionPickerClick(getContext());
        mQuestionPicker.setOnClickListener(questionPickerClickListener);
        //set SlideLayout
        mSlideFragment = SlideFragment.newInstance(3);
        getFragmentManager().beginTransaction().add(R.id.create_videos_root, mSlideFragment).commit();

        //If User clicks at a place other than project Name EditText should convert to textView loosing focus
        FrameLayout touchInterceptor = (FrameLayout) mView.findViewById(R.id.touch_interceptor);
        touchInterceptor.setOnTouchListener(new ClearFocusTouchListener(mProjectName));

        setUpProject();
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("Create Videos", "Visible");
            // setUpProject();
        } else {
            Log.d("Create Videos", "Not visible");
        }
    }

    public void setUpProject() {
        mFolder = mStorage.getStorageDir(getString(R.string.app_name), true);
        mFolder = mStorage.addFolder(mFolder, getString(R.string.projects));
        mFolder = mStorage.addFolder(mFolder, getString(R.string.my_projects));
        mFolder = mStorage.addFolder(mFolder, getString(R.string.create_project));
        SharedRuntimeContent.projectFolder = mFolder;

        mProjectNameDisplay.setText(getString(R.string.create_project));
        boolean success = true;
        if (mFolder != null) {
            if (!mFolder.exists()) {
                success = mFolder.mkdirs();
            }
            if (success) {
                mProjectName.addTextChangedListener(new ProjectTextWatcher(mStorage, SharedRuntimeContent.projectFolder, mProjectNameDisplay));
            } else {
                Snackbar.make(getView(), R.string.storage_error, Snackbar.LENGTH_LONG).show();
                mRoot.setEnabled(false);
            }
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
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getName(), requestCode + " " + resultCode);
        if (resultCode == RESULT_OK)
            handlePickedData(requestCode, data);
    }

    private String mFilePath = null;

    public void handlePickedData(int requestCode, Intent data) {
        Log.d(getClass().getName(), "data " + String.valueOf(data == null));
        if (requestCode == GET_CAMERA_IMAGE && data == null) {

            Intent intent = new Intent(getContext(), CropMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(CropMainActivity.IMAGE_PATH, mCameraImagePicker.getCameraFile().getAbsolutePath());
            intent.putExtras(bundle);
            mFragment.startActivity(intent);
        }
        if (data != null) {
            String selectedPath = null;
            Uri imageUri;
            if (requestCode == GET_CAMERA_IMAGE) {
                Bundle bundle = data.getExtras();
                Log.d(getClass().getName(), "bundle " + String.valueOf(bundle == null));
                if (bundle != null) {
                    Bitmap photo = (Bitmap) bundle.get("data");
                    imageUri = mStorage.getImageUri(photo);
                    selectedPath = mStorage.getRealPathFromURI(imageUri);
                    Log.d(getClass().getName(), "Got selected path" + selectedPath);
                }
            } else {
                selectedPath = mStorage.getRealPathFromURI(data.getData());
            }

            if (selectedPath != null) {
                File pickedFile = new File(selectedPath);
                switch (requestCode) {
                    case GET_IMAGE:

                        Intent intent = new Intent(getContext(), CropMainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(CropMainActivity.IMAGE_PATH, selectedPath);
                        intent.putExtras(bundle);
                        mFragment.startActivity(intent);

                        break;
                    case GET_VIDEO:
                        mStorage.addFileToDirectory(SharedRuntimeContent.projectFolder,
                                SharedRuntimeContent.VIDEO_FOLDER_NAME,
                                SharedRuntimeContent.projectFolder.getName(),
                                pickedFile,
                                new FileCopyUpdateListener(getContext()),
                                new OnFileCopyCompleted() {
                                    @Override
                                    public void done(File file, boolean isSuccessful) {
                                        SharedRuntimeContent.videoPathList.add(file.getName());
                                        Bitmap thumbnail = Storage.getVideoThumbnail(file.getAbsolutePath());
                                        SharedRuntimeContent.addSlide(new DummyContent.Slide(file.getAbsolutePath(),null,thumbnail, DummyContent.SlideType.VIDEO));
                                    }
                                });
                        break;
                }
            }
        }

    }

    @Override
    public void onProgressUpdate(int progress) {

    }
}
