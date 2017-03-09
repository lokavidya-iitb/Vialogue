package com.comp.iitb.vialogue.library;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.comp.iitb.vialogue.adapters.SlideThumbnailsRecyclerViewAdapter;

/**
 * Created by ironstein on 09/03/17.
 */

public class SetSlideThumbnailsRecyclerViewAdapterAsyncTask extends AsyncTask<String, Void, Boolean>{

    private RecyclerView mRecyclerView;
    private SlideThumbnailsRecyclerViewAdapter mSlideThumbnailsRecyclerViewAdapter;
    private Context mContext;
    private Activity mActivity;
    private int mCurrentPosition;

    public SetSlideThumbnailsRecyclerViewAdapterAsyncTask(Activity activity, Context context, int currentPosition, RecyclerView recyclerView) {
        mActivity = activity;
        mRecyclerView = recyclerView;
        mContext = context;
        mCurrentPosition = currentPosition;
    }

    @Override
    public Boolean doInBackground(String... params) {
        mSlideThumbnailsRecyclerViewAdapter = new SlideThumbnailsRecyclerViewAdapter(mActivity, mContext, mCurrentPosition);
        return true;
    }

    @Override
    public void onPostExecute(Boolean value) {
        mRecyclerView.setAdapter(mSlideThumbnailsRecyclerViewAdapter);
        mRecyclerView.invalidate();
    }

}
