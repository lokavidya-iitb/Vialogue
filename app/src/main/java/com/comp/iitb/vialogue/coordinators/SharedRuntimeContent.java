package com.comp.iitb.vialogue.coordinators;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubh on 11-01-2017.
 */

public class SharedRuntimeContent {
    public static List<String> videoPathList = new ArrayList<>();
    public static List<String> imagePathList = new ArrayList<>();
    public static List<Bitmap> imageThunbnails = new ArrayList<>();
    public static List<Bitmap> videoThunbnails = new ArrayList<>();
    public static final int GET_IMAGE = 541;
    public static final int GET_VIDEO = 542;
    public static final int GET_CAMERA_IMAGE = 543;
    public static final String IMAGE_FOLDER_NAME="images";
    public static final String VIDEO_FOLDER_NAME="videos";
    public static final String AUDIO_FOLDER_NAME="audio";
    public static final String VIALOGUE_FOLDER_NAME="vialogue";

}
