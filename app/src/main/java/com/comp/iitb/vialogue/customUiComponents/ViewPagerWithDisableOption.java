package com.comp.iitb.vialogue.customUiComponents;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ironstein on 20/03/17.
 */

public class ViewPagerWithDisableOption extends ViewPager {

    private boolean mSwipeEnabled;

    public ViewPagerWithDisableOption(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mSwipeEnabled = true;
    }

    public boolean isSwipeEnabled() {
        return mSwipeEnabled;
    }

    public void enableSwipe() {
        mSwipeEnabled = true;
    }

    public void disableSwipe() {
        mSwipeEnabled = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSwipeEnabled) {
            return super.onTouchEvent(event);
        } return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mSwipeEnabled) {
            return super.onInterceptTouchEvent(event);
        } return false;
    }

}
