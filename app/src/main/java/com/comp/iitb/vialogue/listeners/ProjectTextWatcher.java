package com.comp.iitb.vialogue.listeners;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;

import java.io.File;

/**
 * Created by shubh on 13-01-2017.
 * Renames the folder as soon as text changes.
 */

public class ProjectTextWatcher implements TextWatcher {

    private TextView mDestination;

    public ProjectTextWatcher(@NonNull TextView destination) {
        mDestination = destination;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            String projectName = s.toString();
            if (projectName.length() > 0 && projectName.length() < 50) {
                mDestination.setText(projectName);
                SharedRuntimeContent.setName(projectName);
            } else if(projectName.length() >= 50){
                Snackbar.make(mDestination, R.string.storage_error,Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
