package com.comp.iitb.vialogue.listeners;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_IMAGE;

/**
 * Created by shubh on 12-01-2017.
 */

public class ImagePickerClick implements View.OnClickListener {

    private Fragment mFragment;

    public ImagePickerClick(Fragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mFragment.startActivityForResult(galleryIntent, GET_IMAGE);
        //TODO: Update with multiple image selection
    }
}
