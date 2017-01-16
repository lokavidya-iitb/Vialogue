package com.comp.iitb.vialogue.library;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by shubh on 12-01-2017.
 */

/**
 * A simple class that can get storage related Queries.
 */
public class Storage {
    private static final String LOG_TAG = "Storage";
    private Activity mActivity;

    public Storage(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Activity cannot be null");
        }
        mActivity = activity;
    }

    /**
     * Checks if external storage is available for read and write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isInternalStorageWriteable() {
        File file = Environment.getDataDirectory();
        Log.d(LOG_TAG, "Internal storage " + String.valueOf(file.exists()));
        return true;
    }

    /**
     * Checks if external storage is available to at least read
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getStorageDir(String folderName, boolean createNew) {
        File file = null;
        isInternalStorageWriteable();
        // Get the directory for the user's public directory.
        if (isStoragePermissionGranted()) {
            if (isExternalStorageWritable()) {
                Log.e(LOG_TAG, "External storage is used");
                file = new File(Environment.getExternalStorageDirectory(), folderName);
            } else {
                Log.e(LOG_TAG, "Internal storage is used");
                file = new File(Environment.getDataDirectory(), folderName);
            }
            if (createNew) {
                if (!file.mkdirs()) {
                    Log.e(LOG_TAG, "Directory not created");
                }
            }
        }
        return file;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mActivity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                //ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    /**
     * Copies file to root directory in specified directory if present
     *
     * @param root        : main directory
     * @param destination : directory inside main
     * @param filePaths   : source of the files to be copied
     * @return was success
     */
    public boolean addFilesToDirectory(@NonNull File root, String destination, @NonNull List<String> filePaths) {
        for (String source : filePaths) {
            if (!addFileToDirectory(root, destination, new File(source)))
                return false;
        }
        return true;
    }

    /**
     * Copies file to root directory in specified directory if present
     *
     * @param root        : main directory
     * @param destination : directory inside main
     * @param sourceFile  : source of the file to be copied
     * @return was success
     */
    public boolean addFileToDirectory(@NonNull File root, String destination, @NonNull File sourceFile) {

        File destinationFile = root;
        if (destination != null) {
            destinationFile = new File(root.getAbsolutePath(), destination);
            if (!destinationFile.exists()) {
                if (!destinationFile.mkdir()) {
                    Log.d("Storage", sourceFile.getName() + "This is the place I failed " + destinationFile.getAbsolutePath());
                    return false;
                }
            }
        }
        try {
            if (mActivity instanceof OnProgressUpdateListener) {
                CopyFileAsync copyFileAsync = new CopyFileAsync(mActivity.getApplicationContext(), (OnProgressUpdateListener) mActivity);
                copyFileAsync.execute(sourceFile,destinationFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, mActivity.getResources().getString(R.string.captured_image_name), null);
        return Uri.parse(path);
    }
}