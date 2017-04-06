package com.comp.iitb.vialogue.listeners;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;

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
        /*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mFragment.startActivityForResult(galleryIntent, GET_IMAGE);*/
        Intent intent = new Intent(mFragment.getActivity(), AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        mFragment.startActivityForResult(intent, Constants.REQUEST_CODE);
        //TODO: Update with multiple image selection
    }
}
