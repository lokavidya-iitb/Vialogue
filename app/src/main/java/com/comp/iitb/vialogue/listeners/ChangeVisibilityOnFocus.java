package com.comp.iitb.vialogue.listeners;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

/**
 * Created by shubh on 14-01-2017.
 */

public class ChangeVisibilityOnFocus implements View.OnFocusChangeListener {
    View mSourceView;
    View mDestinationView;


    public ChangeVisibilityOnFocus(@NonNull View source, @NonNull View destination){
        mSourceView = source;
        mDestinationView = destination;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d("Focus",""+hasFocus);
        if(!hasFocus)
        {
            mSourceView.setVisibility(View.GONE);
            mDestinationView.setVisibility(View.VISIBLE);
        }
    }
}
