package com.comp.iitb.vialogue.library;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by shubh on 16-01-2017.
 */

public class CopyFileAsync extends AsyncTask<File, Integer, Boolean> {

    private File mDestinationFile;
    private Context mContext;
    private OnProgressUpdateListener mProgressUpdateListener;
    private OnFileCopyCompleted mFileCopyCompleted;

    @Deprecated
    public CopyFileAsync(@NonNull Context context, OnProgressUpdateListener progressUpdateListener, @NonNull OnFileCopyCompleted fileCopyCompleted) {
        mContext = context;
        mProgressUpdateListener = progressUpdateListener;
        mFileCopyCompleted = fileCopyCompleted;
    }

    public CopyFileAsync(@NonNull Context context) {
        mContext = context;
    }

    public void addProgressUpdateListener(OnProgressUpdateListener progressUpdateListener) {
        mProgressUpdateListener = progressUpdateListener;
    }

    public void addFileCopyCompletedListener(OnFileCopyCompleted fileCopyCompleted) {
        mFileCopyCompleted = fileCopyCompleted;
    }

    @Override
    protected Boolean doInBackground(File... params) {
        try {
            if (params.length < 2) {
                throw new NullPointerException("Two file objects are compulsory");
            }
            boolean completed = copyFile(params[0], params[1]);
            return completed;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copies file to root directory in specified directory if present
     *
     * @param sourceFile        : main file
     * @param destinationFolder : directory inside which file should be copied
     * @return was success
     */
    public boolean copyFile(File sourceFile, File destinationFolder) {
        double sourceFileSize = sourceFile.length();
        double current = 0;
        boolean isSuccess = true;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(sourceFile);
            mDestinationFile = new File(destinationFolder, sourceFile.getName());
            out = new FileOutputStream(mDestinationFile);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
                if (mProgressUpdateListener != null) {
                    current += len;
                    publishProgress((int) ((current / sourceFileSize) * 100.0));
                }
            }
        } catch (IOException exception) {
            isSuccess = false;
            exception.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    isSuccess = false;
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    isSuccess = false;
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressUpdateListener.onProgressUpdate(values[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (mFileCopyCompleted != null) {
            mFileCopyCompleted.done(mDestinationFile, aBoolean);
        }
    }
}
