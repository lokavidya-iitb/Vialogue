package com.comp.iitb.vialogue.listeners;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by shubh on 16-01-2017.
 */

public class ClearFocusTouchListener implements View.OnTouchListener {

    private View mView;
    public ClearFocusTouchListener(View source){
        mView = source;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mView.isFocused()) {
                Rect outRect = new Rect();
                mView.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    mView.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return false;
    }
}
