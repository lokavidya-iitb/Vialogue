package com.comp.iitb.vialogue.library;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.comp.iitb.vialogue.adapters.SlideThumbnailsRecyclerViewAdapter;
import com.comp.iitb.vialogue.coordinators.OnSlideThumbnailClicked;

/**
 * Created by ironstein on 09/03/17.
 */

public class SetSlideThumbnailsRecyclerViewAdapterAsyncTask extends AsyncTask<String, Void, Boolean>{

    private RecyclerView mRecyclerView;
    private SlideThumbnailsRecyclerViewAdapter mSlideThumbnailsRecyclerViewAdapter;
    private Context mContext;
    private Activity mActivity;
    private int mCurrentPosition;
    private OnSlideThumbnailClicked mOnSlideThumbnailClicked;

    public SetSlideThumbnailsRecyclerViewAdapterAsyncTask(Activity activity, Context context, int currentPosition, RecyclerView recyclerView, OnSlideThumbnailClicked onSlideThumbnailClicked) {
        mActivity = activity;
        mRecyclerView = recyclerView;
        mContext = context;
        mCurrentPosition = currentPosition;
        mOnSlideThumbnailClicked = onSlideThumbnailClicked;
    }

    @Override
    public Boolean doInBackground(String... params) {
        mSlideThumbnailsRecyclerViewAdapter = new SlideThumbnailsRecyclerViewAdapter(mActivity, mContext, mCurrentPosition, mOnSlideThumbnailClicked);
        return true;
    }

    @Override
    public void onPostExecute(Boolean value) {
        mRecyclerView.setAdapter(mSlideThumbnailsRecyclerViewAdapter);
        mRecyclerView.invalidate();
    }

}
