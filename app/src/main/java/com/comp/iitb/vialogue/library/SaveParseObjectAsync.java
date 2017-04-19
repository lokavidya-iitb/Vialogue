package com.comp.iitb.vialogue.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.comp.iitb.vialogue.coordinators.OnProjectSaved;
import com.comp.iitb.vialogue.coordinators.OnThumbnailCreated;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.parse.ParseException;

import java.io.File;

/**
 * Created by ironstein on 07/03/17.
 */

public class SaveParseObjectAsync extends AsyncTask<String, Integer, Boolean> {

    private Project mProject;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private OnProjectSaved mOnProjectSaved;

    public SaveParseObjectAsync(@NonNull Context context, @NonNull ProgressDialog progressDialog, @NonNull OnProjectSaved onProjectSaved, @NonNull Project project) {
        mProject = project;
        mContext = context;
        mProgressDialog = progressDialog;
        mOnProjectSaved = onProjectSaved;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean isSaved) {
        super.onPostExecute(isSaved);
        try {mProgressDialog.dismiss();} catch (Exception e) {}
        mOnProjectSaved.done(isSaved);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            mProject.saveParseObject(mContext);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
