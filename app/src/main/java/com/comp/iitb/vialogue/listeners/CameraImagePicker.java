package com.comp.iitb.vialogue.listeners;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;

/**
 * Created by shubh on 16-01-2017.
 */

public class CameraImagePicker implements View.OnClickListener {
    Fragment mFragment;
    public CameraImagePicker(Fragment fragment){
        mFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        mFragment.startActivityForResult(cameraIntent, SharedRuntimeContent.GET_CAMERA_IMAGE);
    }
}
