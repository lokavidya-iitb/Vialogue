package com.comp.iitb.vialogue.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.comp.iitb.vialogue.coordinators.OnThumbnailCreated;

import java.io.File;

/**
 * Created by ironstein on 03/03/17.
 */

public class VideoThumbnailAsync extends AsyncTask<String, Integer, Bitmap> {

    private String mImagePath;
    private Context mContext;
    private Storage mStorage;
    private OnThumbnailCreated mOnThumbnailCreated;
    ProgressDialog mProgressDialog;

    public VideoThumbnailAsync(@NonNull Context context, @NonNull Storage storage, @NonNull OnThumbnailCreated onThumbnailCreated) {
        mContext = context;
        mStorage = storage;
        mOnThumbnailCreated = onThumbnailCreated;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext, "Generating Thumbnail", "Please wait...", true);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mProgressDialog.dismiss();
        mOnThumbnailCreated.onThumbnailCreated(bitmap);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap thumbnail = Storage.getVideoThumbnail((new File(params[0])).getAbsolutePath());
        return thumbnail;
    }

}
