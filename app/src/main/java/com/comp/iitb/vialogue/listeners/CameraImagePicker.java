package com.comp.iitb.vialogue.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;

import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shubh on 16-01-2017.
 */

public class CameraImagePicker implements View.OnClickListener {
    private Fragment mFragment;
    private File mCameraFile;
    private Context mContext;
    private Storage mStorage;

    public CameraImagePicker(Storage storage, Fragment fragment, Context context) {
        mStorage = storage;
        mFragment = fragment;
        mContext = context;
    }

    @Override
    public void onClick(View v) {

//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        File outputDir = mContext.getCacheDir(); // context being the Activity pointer

//        //Remove if exists, the file MUST be created using the lines below
//        mCameraFile = new File(mContext.getFilesDir(), "tmp_image.jpg");
//        mCameraFile.delete();
//        //Create new file
//        mCameraFile = new File(mContext.getFilesDir(), "tmp_image.jpg");
//        mCameraFile.setReadable(true);
//
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
//        mFragment.startActivityForResult(cameraIntent, SharedRuntimeContent.GET_CAMERA_IMAGE);

//        File tempFolder = mStorage.addFolder(projectFolder,TEMP_FOLDER);
//        File tempImageFolder = mStorage.addFolder(tempFolder,IMAGE_FOLDER_NAME);
//        mCameraFile = new File(tempImageFolder, "image_temp.png");
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
//        mFragment.startActivityForResult(cameraIntent, SharedRuntimeContent.GET_CAMERA_IMAGE);

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".png",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCameraFile = new File(image.getAbsolutePath());

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
        mFragment.startActivityForResult(cameraIntent, SharedRuntimeContent.GET_CAMERA_IMAGE);

    }

    public File getCameraFile() {
        return mCameraFile;
    }
}
