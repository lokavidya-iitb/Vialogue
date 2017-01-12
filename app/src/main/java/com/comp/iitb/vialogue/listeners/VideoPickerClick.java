package com.comp.iitb.vialogue.listeners;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_VIDEO;

/**
 * Created by shubh on 12-01-2017.
 */

public class VideoPickerClick implements View.OnClickListener {

    private Fragment mFragment;
    public VideoPickerClick(Fragment fragment){
        mFragment = fragment;
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mFragment.startActivityForResult(Intent.createChooser(intent, "Select Video"), GET_VIDEO);
    }
}
