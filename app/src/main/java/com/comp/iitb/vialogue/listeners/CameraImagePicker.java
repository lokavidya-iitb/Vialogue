package com.comp.iitb.vialogue.listeners;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.sangcomz.fishbun.FishBun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        mCameraFile = BaseResourceClass.makeTempResourceFile(Slide.ResourceType.IMAGE, mContext);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
        mFragment.startActivityForResult(cameraIntent, SharedRuntimeContent.GET_CAMERA_IMAGE);
    }

    public File getCameraFile() {
        return mCameraFile;
    }
}
