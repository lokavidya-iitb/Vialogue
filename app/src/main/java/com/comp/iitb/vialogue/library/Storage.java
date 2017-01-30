package com.comp.iitb.vialogue.library;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Context mContext;

    @Deprecated
    public Storage(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Activity cannot be null");
        }
        mContext = activity;
    }

    public Storage(@NonNull Context context) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        }
        mContext = context;
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

    public boolean isInternalStorageWritable() {
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

    /**
     * Gets storage folder
     *
     * @param folderName : name of the folder to be created
     * @param createNew  : create a new folder or return existing folder
     * @return File : file handler of the folder.
     */
    public File getStorageDir(String folderName, boolean createNew) {
        File file = null;
        isInternalStorageWritable();
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

    /**
     * Checks storage permissions
     */
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                //ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
            if (!addFileToDirectory(root, destination, new File(source), progressUpdateListener, fileCopyCompleted))
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
            CopyFileAsync copyFileAsync = new CopyFileAsync(mContext.getApplicationContext());
            copyFileAsync.addProgressUpdateListener(progressUpdateListener);
            copyFileAsync.addFileCopyCompletedListener(fileCopyCompleted);
            copyFileAsync.execute(sourceFile, destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Gets real path from Uri of the file
     *
     * @param contentUri : uri of the file
     * @return String : file path
     */
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /*
    * Gets Uri for given Bitmap
    *
    * */
    @Deprecated
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, mContext.getResources().getString(R.string.captured_image_name), null);
        return Uri.parse(path);
    }

    /**
     * Gets Uri for bitmap
     *
     * @param image : bitmap Image
     */
    public Uri getImageUri(Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), image, mContext.getResources().getString(R.string.captured_image_name), null);
        return Uri.parse(path);
    }

    public File saveBitmapToFile(File pictureFile, Bitmap image) {
        File file = null;
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return file;
    }

    /**
     * Gets thumbnail of image stored in android by default
     *
     * @param filePath : storage path of original Image
     * @return thumbnail of images
     */
    public Bitmap getImageThumbnail(@NonNull String filePath) {
        Bitmap thumbnail = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=?", new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            thumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
        }
        cursor.close();
        if (thumbnail == null) {
            try {
                thumbnail = ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(contentResolver, getUriFromPath(filePath)), 512, 384);
                Log.d(LOG_TAG, "getImageThumb " + String.valueOf(thumbnail == null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thumbnail;
    }

    /**
     * Get the thumbnail of the video from filePath
     *
     * @param filePath : filePath of the video.
     * @return Bitmap : bitmap of the thumbnail.
     */
    public Bitmap getVideoThumbnail(@NonNull String filePath) {
        Bitmap thumbnail = null;
        thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        if (thumbnail == null) {
            Log.d("Storage", "Video might be unsupported");
            //TODO: placeholder Image for unsupported video
        }
        return thumbnail;
    }

    /**
     * Gets Uri from file path
     *
     * @param filePath : path of the file
     * @return Uri : uri of the file
     **/
    public Uri getUriFromPath(String filePath) {
        Uri uriPath = null;
        try {
            uriPath = Uri.fromFile(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uriPath;
    }

    public Bitmap getBitmap(String imagePath)
    {
        File image = new File(imagePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        return bitmap;
    }
}