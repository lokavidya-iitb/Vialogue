package com.comp.iitb.vialogue.listeners;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;

import com.comp.iitb.vialogue.helpers.TabSelectedHelper;

/**
 * Created by shubh on 09-01-2017.
 */

public class OnTabSelectedListener implements TabLayout.OnTabSelectedListener {

    private Activity mActivity;
    private String[] mTitles;
    private int mSelectedTabColor;
    private int mUnselectedTabColor;
    private TabSelectedHelper mTabSelectedHelper;

    public OnTabSelectedListener(Activity activity, String[] titles, int selectedTabColor, int unselectedTabColor) {
        mActivity = activity;
        mTitles = titles;
        mSelectedTabColor = selectedTabColor;
        mUnselectedTabColor = unselectedTabColor;
    }

    public void setTabSelectedHelper(TabSelectedHelper tabSelectedHelper) {
        mTabSelectedHelper = tabSelectedHelper;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        if (tab.getIcon() != null)
            tab.getIcon().setColorFilter(mSelectedTabColor, PorterDuff.Mode.SRC_IN);
        if (mActivity != null) {
            int position = tab.getPosition();
            if (mTabSelectedHelper != null) {
                mTabSelectedHelper.onTabSelected(position);
            }
            if (mTitles.length >= position)
                mActivity.setTitle(mTitles[position]);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(mUnselectedTabColor, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
