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

    private File mFolder;
    private Storage mStorage;
    private TextView mDestination;

    public ProjectTextWatcher(@NonNull Storage storage, @NonNull File folder, @NonNull TextView destination) {
        mFolder = folder;
        mStorage = storage;
        mDestination = destination;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            String folderName = s.toString();
            if (folderName.length() > 0 && folderName.length() < 50) {
                File directory = new File(mFolder.getParentFile(), folderName);
                if (directory == null)
                    Log.d("ProjectTextWatcher", "directory is null");
                mFolder.renameTo(directory);
                mDestination.setText(folderName);
                mFolder = directory;
                SharedRuntimeContent.projectFolder = mFolder;
            } else if(folderName.length() >= 50){
                Snackbar.make(mDestination, R.string.storage_error,Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
