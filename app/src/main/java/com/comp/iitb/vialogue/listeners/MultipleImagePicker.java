package com.comp.iitb.vialogue.listeners;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.sangcomz.fishbun.FishBun;

import java.util.ArrayList;

/**
 * Created by ironstein on 16/03/17.
 */

public class MultipleImagePicker implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;
    private ArrayList<Uri> mPaths;

    public ArrayList<Uri> getPaths() {
        return mPaths;
    }

    public MultipleImagePicker(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        mPaths = new ArrayList<>();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        System.out.println("beforeeeeeeeeeeee");
        FishBun.with(mActivity)
                .setAlbumSpanCountOnlPortrait(1)
                .setPickerSpanCount(2)
                .setActionBarColor(
                        mContext.getResources().getColor(R.color.colorPrimary),
                        mContext.getResources().getColor(R.color.colorPrimaryDark),
                        false
                )
                .setAlbumThumbnailSize(width/2)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .setArrayPaths(mPaths)
                .setButtonInAlbumActivity(true)
                .setCamera(false)
                .setReachLimitAutomaticClose(false)
                .setAllViewTitle("All")
                .setActionBarTitle("Pick Images")
                .textOnImagesSelectionLimitReached("Limit Reached!")
                .textOnNothingSelected("Nothing Selected")
                .setRequestCode(SharedRuntimeContent.GET_MULTIPLE_IMAGES)
                .startAlbum();
    }
}
