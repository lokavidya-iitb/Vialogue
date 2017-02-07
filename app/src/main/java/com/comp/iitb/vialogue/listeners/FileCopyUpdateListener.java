package com.comp.iitb.vialogue.listeners;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;

/**
 * Created by shubh on 02-02-2017.
 */

public class FileCopyUpdateListener implements OnProgressUpdateListener {

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public FileCopyUpdateListener(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(0);
        mProgressDialog.setTitle(mContext.getResources().getString(R.string.file_copy_title));
        try {
            mProgressDialog.show();
        } catch (Exception e) {
            Log.d("FileCopyUpdateListener",e.getMessage());
            Toast.makeText(mContext,R.string.please_wait, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProgressUpdate(int progress) {
        mProgressDialog.setProgress(progress);
        if (progress >= 100) {
            mProgressDialog.dismiss();
        }
    }
}
