// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.comp.iitb.vialogue.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.comp.iitb.vialogue.App;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.AudioRecordActivity;
import com.comp.iitb.vialogue.activity.CropMainActivity;
import com.comp.iitb.vialogue.coordinators.CropImageCoordinator;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.dataStructures.LIFOSet;
import com.comp.iitb.vialogue.library.ExifUtils;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.library.cropper.CropImage;
import com.comp.iitb.vialogue.library.cropper.CropImageView;
import com.comp.iitb.vialogue.models.crop.CropDemoPreset;

import java.io.File;
import java.io.IOException;
import java.util.Stack;


/**
 * The fragment that will show the Image Cropping UI by requested preset.
 */
public final class CropMainFragment extends Fragment
        implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener, CropImageCoordinator {
    private static final String CROP_IMAGE_PATH = "crop_image_path";
    private static String LOG_TAG = "CropMainFragment";
    //region: Fields and Consts
    LIFOSet<String> sequence = new LIFOSet<>();
    private Storage mStorage;
    private Button done;
    private CropDemoPreset mDemoPreset;
    private CropImageView mCropImageView;
    private String mCropImagePath;
    private Bitmap mCroppedImage;
    private Bitmap currentBitmap;
    //endregion

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static CropMainFragment newInstance(CropDemoPreset demoPreset, String filePath) {
        CropMainFragment fragment = new CropMainFragment();
        Bundle args = new Bundle();
        args.putString("DEMO_PRESET", demoPreset.name());
        args.putString(CROP_IMAGE_PATH, filePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_main_rect, container, false);
        setHasOptionsMenu(true);

        mStorage = new Storage(getContext());
        mCroppedImage = mStorage.getBitmap(mCropImagePath);
        currentBitmap = mStorage.getBitmap(mCropImagePath);

        ((CropMainActivity)getActivity()).mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mCropImageView.getCroppedImageAsync();*/
                ((CropMainActivity)getActivity()).done(currentBitmap);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* mCroppedImage = decodeFile(mCropImagePath);*/

        mCropImageView = (CropImageView) view.findViewById(R.id.cropImageView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
        /*mCropImageView.setImageUriAsync(mStorage.getUriFromPath(mCropImagePath));*/
        mCropImageView.setImageUriAsync(mStorage.getImageUri(mCroppedImage));
    }

    public Bitmap decodeFile(String filePath) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
        Bitmap b= ExifUtils.rotateBitmap(filePath, b1);
        return b;
        // image.setImageBitmap(bitmap);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.main_action_crop) {
            sequence.push(mCropImagePath);
            mCropImageView.getCroppedImageAsync();
            return true;
        } else if (item.getItemId() == R.id.main_action_rotate) {
            sequence.push(mCropImagePath);
            Bitmap rotator = SharedRuntimeContent.rotateBitmap(mCroppedImage,ExifInterface.ORIENTATION_ROTATE_90);
            currentBitmap = rotator;
            mCropImageView.setImageBitmap(rotator);
            return true;
        }
        else if (item.getItemId() == R.id.undo) {
            if(!sequence.isEmpty()) {
                System.out.println("------------sequence"+ sequence.toString());
                Bitmap tempOne = mStorage.getBitmap(sequence.pop());
                if(tempOne.equals(currentBitmap))
                    tempOne = mStorage.getBitmap(sequence.pop());
                mCropImageView.setImageBitmap(tempOne);
                System.out.println("------------sequenceaterPopping"+ sequence.toString());
            }
            else {
                // Using
                // Toast.makeText(CropMainActivity.this, R.string.cannotUndo, Toast.LENGTH_LONG).show();
                // Leads to a memory leaks (as large as 120 MB)
                // TODO add reference to the commit
                Toast.makeText(getActivity().getApplicationContext(), R.string.cannotUndo, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDemoPreset = CropDemoPreset.valueOf(getArguments().getString("DEMO_PRESET"));
        mCropImagePath = getArguments().getString(CROP_IMAGE_PATH);
        if (context instanceof CropMainActivity)
            ((CropMainActivity) context).setCurrentFragment(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCropImageView != null) {
            mCropImageView.setOnSetImageUriCompleteListener(null);
            mCropImageView.setOnCropImageCompleteListener(null);
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error != null) {
            Log.e(LOG_TAG, "Failed to load image by URI", error);
            Snackbar.make(getView(), R.string.image_load_failed, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCropResult(result);
        }
    }

    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {

            mCroppedImage = result.getBitmap();

            /*ExifInterface exif = null;
            try {
                exif = new ExifInterface(mCropImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);*/
            // WHY?
//            mCroppedImage = SharedRuntimeContent.rotateBitmap(mCroppedImage, orientation);
            currentBitmap = mCroppedImage;
            mCropImageView.setImageBitmap(mCroppedImage);
        } else {
            Log.e(LOG_TAG, "Failed to crop image", result.getError());
            Snackbar.make(getView(), R.string.crop_failed, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public Bitmap getCroppedImage() {
        Bitmap bitmap = null;
        if (mCroppedImage != null)
            bitmap = mCroppedImage;
        return bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
/*

        // recycle bitmap
        mCropImageView.setImageBitmap(null);
        Storage.recycleBitmap(mCroppedImage);
*/
       /* Storage.recycleBitmap(mCroppedImage);*/
        // clear LIFO
        sequence.clear();
        sequence = null;

//        RefWatcher refWatcher = App.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }
}
