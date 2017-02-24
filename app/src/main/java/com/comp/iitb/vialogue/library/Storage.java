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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;
import com.comp.iitb.vialogue.coordinators.OnThumbnailCreated;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Context mContext;

    @Deprecated
    public Storage(@NonNull Activity activity) {
        mContext = activity;
    }

    public Storage(@NonNull Context context) {
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

    public File addFolder(File source, String name) {
        File destination = null;
        if (isStoragePermissionGranted()) {
            destination = new File(source, name);
            if (!destination.exists())
                destination.mkdir();
        }
        return destination;
    }

    /**
     * Checks storage permissions
     */
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
     * Copies file to root directory in specified directory if present
     *
     * @param root        : main directory
     * @param destination : directory inside main
     * @param sourceFile  : source of the file to be copied
     * @return was success
     */
    public boolean addFileToDirectory(@NonNull File root, String destination, @NonNull String fileName, @NonNull File sourceFile, OnProgressUpdateListener progressUpdateListener, OnFileCopyCompleted fileCopyCompleted) {

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
            copyFileAsync.setFileName(fileName);
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
        /*String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            try {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
        cursor.close();*/
        return MediaFilePath.getPath(mContext, contentUri);
    }

    /*
    * Gets Uri for given Bitmap
    *
    * */
    @Deprecated
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
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
        image.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), image, mContext.getResources().getString(R.string.captured_image_name), null);
        return Uri.parse(path);
    }

    public File saveBitmapToFile(File pictureFile, Bitmap image) {
        File file = null;
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(LOG_TAG, "Error accessing file: " + e.getMessage());
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
                thumbnail = decodeSampledBitmapFromResource(filePath, 512, 384);
                //If above doesn't work use this
                //thumbnail = ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(contentResolver, getUriFromPath(filePath)), 512, 384);
                Log.d(LOG_TAG, "getImageThumb " + String.valueOf(thumbnail == null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thumbnail;
    }

    public AsyncTask getImageThumbnailAsync(@NonNull String filePath, @NonNull OnThumbnailCreated thumbnailCreated) {
        ImageThumbnailAsync imageThumbnailAsync = new ImageThumbnailAsync(mContext, this, thumbnailCreated);
        return imageThumbnailAsync.execute(filePath);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String filePath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Get the thumbnail of the video from filePath
     *
     * @param filePath : filePath of the video.
     * @return Bitmap : bitmap of the thumbnail.
     */
    public static Bitmap getVideoThumbnail(@NonNull String filePath) {
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

    public Bitmap getBitmap(String imagePath) {
        File image = new File(imagePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        return bitmap;
    }

    public File getUniqueFileName(File directory, String prefix, String extension) {
        File file = null;
        isInternalStorageWritable();
        // Get the directory for the user's public directory.
        if (isStoragePermissionGranted()) {
            if (isExternalStorageWritable()) {
                Log.e(LOG_TAG, "External storage is used");
                int temp = 0;
                do {
                    file = new File(directory, prefix + (temp++) + "." + extension);
                } while (file.exists());
            }
        }
        return file;
    }

    /**
     * Jeff
     */

    public static void createThisDirectory(String path) {
        File folder = new File(Environment.getExternalStorageDirectory() + path);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
        } else {
        }
    }

    public static List<String> getMeAllTheFilesHere(String path) {
        List<String> myStringArray = new ArrayList<String>();
        File sdCard = Environment.getExternalStorageDirectory();
        File targetDirectory = new File(sdCard.getAbsolutePath() + path);
        boolean fileArrayExists = false;
        if (targetDirectory.exists() && targetDirectory.isDirectory()) {
            File file[] = targetDirectory.listFiles();
            try {
                fileArrayExists = file.equals(null);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (!fileArrayExists) {
                boolean fileExists = false;

                for (int i = 0; i < file.length; i++) {
                    myStringArray.add(file[i].getName());
                }
            } else {
            }
        }
        Collections.sort(myStringArray);
        return myStringArray;
    }

    public static List<String> getMeTheeseInThisProject(String projectName, String whichProjectType, String whatFiles) {
        List<String> myStringArray = new ArrayList<String>();
        File sdCard = Environment.getExternalStorageDirectory();
        File targetDirectory = new File(sdCard.getAbsolutePath() + Master.AppPath + Master.ProjectsPath + "/" + whichProjectType + "/" + projectName + "/" + whatFiles);
        boolean fileArrayExists = false;
        if (targetDirectory.exists() && targetDirectory.isDirectory()) {
            File file[] = targetDirectory.listFiles();
            try {
                fileArrayExists = file.equals(null);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (!fileArrayExists) {
                boolean fileExists = false;

                for (int i = 0; i < file.length; i++) {
                    myStringArray.add(file[i].getName());
                }
            } else {
            }
        }
        Collections.sort(myStringArray);
        return myStringArray;
    }

    public static void deleteThisFolder(String projectPath) {
        File parentDirectory = new File(Environment.getExternalStorageDirectory() + projectPath);
        deleteDirectory(parentDirectory);
    }

    static private void deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
    }

    public static void setupLokavidyaLegacy() {
        Storage.createThisDirectory(Master.AppPath);
        Storage.createThisDirectory(Master.AppPath + Master.ProjectsPath);
        Storage.createThisDirectory(Master.AppPath + Master.ProjectsPath + Master.MyProjectsPath);
        Storage.createThisDirectory(Master.AppPath + Master.ProjectsPath + Master.SavedProjectsPath);
        Storage.createThisDirectory(Master.AppPath + Master.VideosPath + Master.SavedVideosPath);
    }

}