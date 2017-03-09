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
import android.net.Uri;
import android.os.Bundle;
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

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.AudioRecordActivity;
import com.comp.iitb.vialogue.activity.CropMainActivity;
import com.comp.iitb.vialogue.coordinators.CropImageCoordinator;
import com.comp.iitb.vialogue.dataStructures.LIFOSet;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.library.cropper.CropImage;
import com.comp.iitb.vialogue.library.cropper.CropImageView;
import com.comp.iitb.vialogue.models.crop.CropDemoPreset;

import java.util.Stack;


/**
 * The fragment that will show the Image Cropping UI by requested preset.
 */
public final class CropMainFragment extends Fragment
        implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener, CropImageCoordinator {
    private static final String CROP_IMAGE_PATH = "crop_image_path";
    private static String LOG_TAG = "CropMainFragment";
    //region: Fields and Consts
    LIFOSet<Bitmap> sequence = new LIFOSet<>();
    private Storage mStorage;
    private Button done;
    private CropDemoPreset mDemoPreset;
    private CropImageView mCropImageView;
    private String mCropImagePath;
    private Bitmap mCroppedImage;
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

        ((CropMainActivity)getActivity()).mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropImageView.getCroppedImageAsync();
                ((CropMainActivity)getActivity()).done();
            }
        });
        mStorage = new Storage(getContext());
        mCroppedImage = mStorage.getBitmap(mCropImagePath);
        sequence.push( mStorage.getBitmap(mCropImagePath));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCropImageView = (CropImageView) view.findViewById(R.id.cropImageView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);

        mCropImageView.setImageUriAsync(mStorage.getUriFromPath(mCropImagePath));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.undo);
        if(sequence.size()==0)
        item.setVisible(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_action_crop) {
            mCropImageView.getCroppedImageAsync();
            return true;
        } else if (item.getItemId() == R.id.main_action_rotate) {
            mCropImageView.rotateImage(-90);
            return true;
        }
        else if (item.getItemId() == R.id.undo) {
            if(sequence.size()!=0)
            mCropImageView.setImageBitmap(sequence.pop());
            else {
                Toast.makeText(getContext(), "Seriously?!", Toast.LENGTH_LONG).show();
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
            sequence.push(mCroppedImage);
            System.out.println(""+ sequence.size());
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
}
