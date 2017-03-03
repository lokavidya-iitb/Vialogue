package com.comp.iitb.vialogue.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.comp.iitb.vialogue.coordinators.OnThumbnailCreated;

/**
 * Created by shubh on 06-02-2017.
 */

public class ImageThumbnailAsync extends AsyncTask<String, Integer, Bitmap> {

    private String mImagePath;
    private Context mContext;
    private Storage mStorage;
    private OnThumbnailCreated mOnThumbnailCreated;
    ProgressDialog mProgressDialog;

    public ImageThumbnailAsync(@NonNull Context context, @NonNull Storage storage, @NonNull OnThumbnailCreated onThumbnailCreated, ProgressDialog progressDialog) {
        mContext = context;
        mStorage = storage;
        mOnThumbnailCreated = onThumbnailCreated;
        mProgressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mOnThumbnailCreated.onThumbnailCreated(bitmap);
        mProgressDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap thumbnail = mStorage.getImageThumbnail(params[0]);
        return thumbnail;
    }

}
