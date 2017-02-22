package com.comp.iitb.vialogue.coordinators;

import android.graphics.Bitmap;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.adapters.SlideRecyclerViewAdapter;
import com.comp.iitb.vialogue.models.DummyContent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubh on 11-01-2017.
 */

public class SharedRuntimeContent {
    public static List<String> videoPathList = new ArrayList<>();
    public static List<String> imagePathList = new ArrayList<>();
    public static List<Bitmap> imageThumbnails = new ArrayList<>();
    public static final int GET_IMAGE = 541;
    public static final int GET_VIDEO = 542;
    public static final int GET_CAMERA_IMAGE = 543;
    public static final String IMAGE_FOLDER_NAME = "images";
    public static final String VIDEO_FOLDER_NAME = "videos";
    public static final String AUDIO_FOLDER_NAME = "audio";
    public static final String VIALOGUE_FOLDER_NAME = "vialogue";
    public static final String TEMP_FOLDER = "temp_folder";
    public static final String TEMP_IMAGE_NAME = "image_temp";
    /**
     * An array of sample (dummy) items.
     */
    public static final ArrayList<DummyContent.Slide> ITEMS = new ArrayList<DummyContent.Slide>();
    public static File projectFolder;
    public static SlideRecyclerViewAdapter projectAdapter;
    public static MainActivity mainActivity;

    public static void addSlide(DummyContent.Slide slide) {
        ITEMS.add(slide);
        projectAdapter.notifyItemInserted(ITEMS.size() - 1);

    }

    public static void changeSlidePosition(int current, int destination) {
        DummyContent.Slide temp = ITEMS.get(current);
        ITEMS.remove(current);
        ITEMS.add(destination, temp);
        projectAdapter.notifyDataSetChanged();
    }

    public static void deleteSlide(int position) {
        ITEMS.remove(position);
        projectAdapter.notifyItemRemoved(position);
        projectAdapter.notifyItemChanged(position,ITEMS.size());
        //
    }

    public static DummyContent.Slide getSlideAt(int position) {
        return ITEMS.get(position);
    }

    public static void updateAdapterView(int position) {
        projectAdapter.notifyItemChanged(position);
    }

    public static void onSlideChanged(DummyContent.Slide slide) {
        projectAdapter.notifyItemChanged(SharedRuntimeContent.ITEMS.indexOf(slide));
    }

    public static int getSlidePosition(DummyContent.Slide item) {
        return ITEMS.indexOf(item);
    }
}
