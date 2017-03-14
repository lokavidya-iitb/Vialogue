package com.comp.iitb.vialogue.library;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.comp.iitb.vialogue.adapters.MyProjectsAdapter;
import com.comp.iitb.vialogue.coordinators.OnAdapterSet;

/**
 * Created by ironstein on 09/03/17.
 */

public class SetMyProjectsAdapterAsync extends AsyncTask<String, Void, Boolean> {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private MyProjectsAdapter mMyProjectsAdapter;
    private OnAdapterSet mOnAdapterSet;

    public SetMyProjectsAdapterAsync(Context context, RecyclerView recyclerView, OnAdapterSet onAdapterSet) {
        mContext = context;
        mRecyclerView = recyclerView;
        mOnAdapterSet = onAdapterSet;
    }

    @Override
    public Boolean doInBackground(String... params) {
        mMyProjectsAdapter = new MyProjectsAdapter(mContext);
        return true;
    }

    @Override
    public void onPostExecute(Boolean b) {
        mRecyclerView.setAdapter(mMyProjectsAdapter);
        mRecyclerView.invalidate();
        mOnAdapterSet.onDone();
    }
}
