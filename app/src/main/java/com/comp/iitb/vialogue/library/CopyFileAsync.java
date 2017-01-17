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

    public CopyFileAsync(@NonNull Context context, @NonNull OnProgressUpdateListener progressUpdateListener, OnFileCopyCompleted fileCopyCompleted) {
        mContext = context;
        mProgressUpdateListener = progressUpdateListener;
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
        } catch (IOException e) {
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
    public boolean copyFile(File sourceFile, File destinationFolder) throws IOException {
        double sourceFileSize = sourceFile.length();
        double current = 0;
        InputStream in = new FileInputStream(sourceFile);
        mDestinationFile = new File(destinationFolder, sourceFile.getName());
        OutputStream out = new FileOutputStream(mDestinationFile);

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
        in.close();
        out.close();
        return true;
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
        if(aBoolean&&mFileCopyCompleted!=null){
            mFileCopyCompleted.done(mDestinationFile,true);
        } else {
            mFileCopyCompleted.done(mDestinationFile,false);
        }
    }
}
