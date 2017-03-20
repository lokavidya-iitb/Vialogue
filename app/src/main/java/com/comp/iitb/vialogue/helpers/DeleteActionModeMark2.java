package com.comp.iitb.vialogue.helpers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnDone;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;

import java.util.ArrayList;

/**
 * Created by ironstein on 20/03/17.
 */

public class DeleteActionModeMark2 implements ActionMode.Callback {

    private Activity mActivity;
    private OnDone mOnDone;

    public DeleteActionModeMark2(Activity activity, OnDone onDone) {
        mActivity = activity;
        mOnDone = onDone;
    }

    @Override
    public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.create_project_action, menu);
        MenuItem item = menu.findItem(R.id.delete_option);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mOnDone.done(true);
                mode.finish();
                return true;
            }
        });
        if (mActivity instanceof AppCompatActivity)
            ((AppCompatActivity) mActivity).getSupportActionBar().hide();
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
        mOnDone.done(false);
        if (mActivity instanceof AppCompatActivity)
            ((AppCompatActivity) mActivity).getSupportActionBar().show();
    }

}
