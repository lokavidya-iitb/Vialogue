package com.comp.iitb.vialogue.listeners;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;

import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;

import java.io.File;

import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.IMAGE_FOLDER_NAME;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.TEMP_FOLDER;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.projectFolder;

/**
 * Created by shubh on 16-01-2017.
 */

public class CameraImagePicker implements View.OnClickListener {
    private Fragment mFragment;
    private File mCameraFile;
    private Storage mStorage;

    public CameraImagePicker(Storage storage, Fragment fragment) {
        mStorage = storage;
        mFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFolder = mStorage.addFolder(projectFolder,TEMP_FOLDER);
        File tempImageFolder = mStorage.addFolder(tempFolder,IMAGE_FOLDER_NAME);
        mCameraFile = new File(tempImageFolder, "image_temp.png");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
        mFragment.startActivityForResult(cameraIntent, SharedRuntimeContent.GET_CAMERA_IMAGE);
    }

    public File getCameraFile() {
        return mCameraFile;
    }
}
