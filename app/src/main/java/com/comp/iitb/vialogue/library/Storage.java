package com.comp.iitb.vialogue.library;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
    public boolean addFilesToDirectory(@NonNull File root, String destination, @NonNull List<String> filePaths, OnProgressUpdateListener progressUpdateListener, OnFileCopyCompleted fileCopyCompleted) {
        for (String source : filePaths) {
            if (!addFileToDirectory(root, destination, new File(source),progressUpdateListener, fileCopyCompleted))
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
    public boolean addFileToDirectory(@NonNull File root, String destination, @NonNull File sourceFile, OnProgressUpdateListener progressUpdateListener, OnFileCopyCompleted fileCopyCompleted) {

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
            CopyFileAsync copyFileAsync = null;
            copyFileAsync = new CopyFileAsync(mActivity.getApplicationContext(), progressUpdateListener, fileCopyCompleted);
            copyFileAsync.execute(sourceFile, destinationFile);
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

    /**
     * Gets thumbnail of image stored in android by default
     *
     * @param filePath : storage path of original Image
     * @return thumbnail of images
     */
    public Bitmap getImageThumbnail(@NonNull String filePath) {
        Bitmap thumbnail = null;
        ContentResolver contentResolver = mActivity.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=?", new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            thumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
        }
        cursor.close();
        if (thumbnail == null) {
            Log.d("Storage", "Image might be unsupported");
            //TODO: placeholder Image for unsupported image
        }
        return thumbnail;
    }

    public static Bitmap getVideoThumbnail(@NonNull String filePath) {
        Bitmap thumbnail = null;
        thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        if (thumbnail == null) {
            Log.d("Storage", "Video might be unsupported");
            //TODO: placeholder Image for unsupported video
        }
        return thumbnail;
    }

    public static void createThisDirectory(String path)
    {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + path);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
        } else {
        }
    }
    public static List<String> getMeAllTheFilesHere(String path){
        List<String> myStringArray = new ArrayList<String>();
        File sdCard = Environment.getExternalStorageDirectory();
        File targetDirectory = new File (sdCard.getAbsolutePath()+path);
        boolean fileArrayExists= false;
        if(targetDirectory.exists()&& targetDirectory.isDirectory())
        {
            File file[] = targetDirectory.listFiles();
            try {
                fileArrayExists =file.equals(null);
            }
            catch(NullPointerException e) {
                e.printStackTrace();
            }

            if(!fileArrayExists)
            {
                boolean fileExists= false;

                for (int i=0; i < file.length; i++)
                {
                    myStringArray.add(file[i].getName());
                }
            }
            else
            {
            }
        }
        Collections.sort(myStringArray);
        return myStringArray;
    }

    public static List<String> getMeTheeseInThisProject(String projectName, String whichProjectType, String whatFiles)
    {
        List<String> myStringArray = new ArrayList<String>();
        File sdCard = Environment.getExternalStorageDirectory();
        File targetDirectory = new File (sdCard.getAbsolutePath()+"/Lokavidya/Projects/"+whichProjectType+"/"+projectName+"/"+whatFiles);
        boolean fileArrayExists= false;
        if(targetDirectory.exists()&& targetDirectory.isDirectory())
        {
            File file[] = targetDirectory.listFiles();
            try {
                fileArrayExists =file.equals(null);
            }
            catch(NullPointerException e) {
                e.printStackTrace();
            }

            if(!fileArrayExists)
            {
                boolean fileExists= false;

                for (int i=0; i < file.length; i++)
                {
                    myStringArray.add(file[i].getName());
                }
            }
            else
            {
            }
        }
        Collections.sort(myStringArray);
        return myStringArray;
    }



}