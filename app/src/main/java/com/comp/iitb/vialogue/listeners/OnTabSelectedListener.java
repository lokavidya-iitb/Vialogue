package com.comp.iitb.vialogue.listeners;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;

/**
 * Created by shubh on 09-01-2017.
 */

public class OnTabSelectedListener implements TabLayout.OnTabSelectedListener {

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
