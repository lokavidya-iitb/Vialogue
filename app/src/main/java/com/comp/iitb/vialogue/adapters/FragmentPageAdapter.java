package com.comp.iitb.vialogue.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.comp.iitb.vialogue.fragments.CreateVideos;
import com.comp.iitb.vialogue.fragments.DummyFragment;
import com.comp.iitb.vialogue.fragments.Home;
import com.comp.iitb.vialogue.fragments.UserAccount;
import com.comp.iitb.vialogue.fragments.ViewVideos;

/**
 * Created by shubh on 09-01-2017.
 */
//Tab Manager Adapter
public class FragmentPageAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    final int HOME = 0;
    final int CREATE_PROJECT = 1;
    final int VIEW_VIDEOS = 2;
    final int USER_ACCOUNT = 3;

    private Context context;

    public FragmentPageAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        //Generate Fragment based on Positions
        switch (position) {
            case HOME:
                return Home.newInstance();
            case CREATE_PROJECT:
                return CreateVideos.newInstance();
            case VIEW_VIDEOS:
                return ViewVideos.newInstance("")/*DummyFragment.newInstance()*/;
            case USER_ACCOUNT:
                return UserAccount.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return null;
    }

}