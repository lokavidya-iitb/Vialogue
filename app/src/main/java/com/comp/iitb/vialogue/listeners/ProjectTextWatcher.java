package com.comp.iitb.vialogue.listeners;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

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
            if (folderName.length() > 0) {
                File directory = mStorage.getStorageDir(folderName, false);
                if (directory == null)
                    Log.d("ProjectTextWatcher", "directory is null");
                mFolder.renameTo(directory);
                mDestination.setText(folderName);
                mFolder = mStorage.getStorageDir(directory.getName(),false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
