package com.comp.iitb.vialogue.listeners;

import android.graphics.Bitmap;

import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.library.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubh on 17-01-2017.
 */

public class ImageFileCopyCompleted implements OnFileCopyCompleted {

    Storage mStorage;
    List<Bitmap> mThumbnailCollection;
    public ImageFileCopyCompleted(Storage storage, ArrayList<Bitmap> thumbnailCollection){
        mStorage = storage;
        mThumbnailCollection = thumbnailCollection;
    }

    @Override
    public void done(File file, boolean isSuccessful) {

    }
}
