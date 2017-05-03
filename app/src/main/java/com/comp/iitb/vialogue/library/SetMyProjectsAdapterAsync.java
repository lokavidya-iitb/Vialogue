package com.comp.iitb.vialogue.library;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.comp.iitb.vialogue.adapters.MyProjectsAdapter;
import com.comp.iitb.vialogue.coordinators.OnAdapterSet;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by ironstein on 09/03/17.
 */

public class SetMyProjectsAdapterAsync extends AsyncTask<String, Void, Boolean> {

    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private AVLoadingIndicatorView mLoadingAnimation;
    private MyProjectsAdapter mMyProjectsAdapter;
    private OnAdapterSet mOnAdapterSet;

    public SetMyProjectsAdapterAsync(Activity activity, AVLoadingIndicatorView loadingAnimation, RecyclerView recyclerView, OnAdapterSet onAdapterSet) {
        mActivity = activity;
        mLoadingAnimation = loadingAnimation;
        mRecyclerView = recyclerView;
        mOnAdapterSet = onAdapterSet;
    }

    @Override
    public Boolean doInBackground(String... params) {
        mMyProjectsAdapter = new MyProjectsAdapter(mActivity, mLoadingAnimation, mRecyclerView);
        return true;
    }

    @Override
    public void onPostExecute(Boolean b) {
        mRecyclerView.setAdapter(mMyProjectsAdapter);
        mRecyclerView.invalidate();
        mOnAdapterSet.onDone(mMyProjectsAdapter);
    }
}
