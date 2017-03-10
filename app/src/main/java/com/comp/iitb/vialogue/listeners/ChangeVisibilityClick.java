package com.comp.iitb.vialogue.listeners;

import android.view.View;
import android.widget.EditText;

/**
 * Created by shubh on 01-02-2017.
 */

public class ChangeVisibilityClick implements View.OnClickListener {
    private View mDestination;
    private int mSetVisibility;

    /**
     * @param destination:   View for changing visibility
     * @param setVisibility: Visibility gone or visible
     */
    public ChangeVisibilityClick(View destination, int setVisibility) {
        mDestination = destination;
        if(setVisibility != View.GONE && setVisibility != View.INVISIBLE && setVisibility != View.VISIBLE)
            throw new IllegalArgumentException("Incorrect Visibility Mode");
        mSetVisibility = setVisibility;
    }

    @Override
    public void onClick(View v) {
        if (mDestination.getVisibility() == View.VISIBLE) {
            mDestination.setVisibility(mSetVisibility);
            try {((EditText) mDestination).setText(""); } catch (Exception e) {}
        } else {
            mDestination.setVisibility(View.VISIBLE);
        }
    }
}
