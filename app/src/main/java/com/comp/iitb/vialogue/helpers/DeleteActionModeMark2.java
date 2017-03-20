package com.comp.iitb.vialogue.helpers;

import android.app.Activity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by ironstein on 20/03/17.
 */

public class DeleteActionModeMark2 implements ActionMode.Callback {

    Activity mActivity;
    ArrayList<Integer> mSlidePositions;

    public DeleteActionModeMark2(Activity activity, ArrayList<Integer> slidePositions) {
        mActivity = activity;
        mSlidePositions = slidePositions;
    }

    @Override
    public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

}
