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

package com.comp.iitb.vialogue.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.CropImageCoordinator;
import com.comp.iitb.vialogue.coordinators.FragmentBinder;
import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.OnThumbnailCreated;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.fragments.CropMainFragment;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.crop.CropDemoPreset;
import com.comp.iitb.vialogue.models.crop.CropImageViewOptions;

import org.apache.commons.lang3.ObjectUtils;

import java.io.File;

public class CropMainActivity extends AppCompatActivity implements FragmentBinder {

    //region: Fields and Consts

    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment mCurrentFragment;
    private CropImageCoordinator mCropImageCoordinator;
    private Storage mStorage;
    public Button mDone;
    private Uri mCropImageUri;
    private RelativeLayout mPleaseWait;
    private String mCroppedImagePath;
    private CropImageViewOptions mCropImageViewOptions = new CropImageViewOptions();
    AppCompatActivity mActivity;
    public static final String IMAGE_PATH = "imagePath";
    private String mFilePath;
    public String from;
    private int mSlidePosition;
    private ProgressDialog mProgressDialog;


    public void setCurrentFragment(Fragment fragment) {
        mCurrentFragment = fragment;
        if (fragment instanceof CropImageCoordinator)
            mCropImageCoordinator = (CropImageCoordinator) fragment;
    }

    public void setCurrentOptions(CropImageViewOptions options) {
        mCropImageViewOptions = options;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorage = new Storage(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mActivity = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try
            {
                from=bundle.getString("from");
                mFilePath = bundle.getString(IMAGE_PATH);
                switch(from)
                {
                    case "AudioRecording":
                        mSlidePosition=bundle.getInt("SlidePosition");
                        AudioRecordActivity.traitor.finish();
                        break;
                    case "CreateVideos":
                        mSlidePosition=-1;
                        break;
                }

            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
        mDone = (Button) findViewById(R.id.done_button);
        /*mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });*/
        mPleaseWait = (RelativeLayout) findViewById(R.id.please_wait);
        setMainFragmentByPreset(CropDemoPreset.RECT);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (mCurrentFragment != null && mCurrentFragment.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMainFragmentByPreset(CropDemoPreset demoPreset) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, CropMainFragment.newInstance(demoPreset, mFilePath))
                .commit();
    }

    public void done() {
        mDone.setEnabled(false);
        mPleaseWait.setVisibility(View.VISIBLE);
        // TODO this leads to the main thread hanging
//        mProgressDialog = ProgressDialog.show(CropMainActivity.this, "Generating Thumbnail", "Please wait...", true);
        new ProcessAsync().execute();
    }
    public void done(Bitmap bitmap) {
        mDone.setEnabled(false);
        mPleaseWait.setVisibility(View.VISIBLE);
        // TODO this leads to the main thread hanging
//        mProgressDialog = ProgressDialog.show(CropMainActivity.this, "Generating Thumbnail", "Please wait...", true);
        new ProcessAsyncAfteCrop(bitmap).execute();
    }


    private OnThumbnailCreated mThumbnailCreated = new OnThumbnailCreated() {
        @Override
        public void onThumbnailCreated(Bitmap thumbnail) {
            Slide slide = new Slide();
            try
            {
                Image image = new Image(getBaseContext());
                mStorage.getBitmap(mCroppedImagePath);
                mStorage.saveBitmapToFile(image.getResourceFile(), mStorage.getBitmap(mCroppedImagePath));
                /*SharedRuntimeContent.getSlideAt(mSlidePosition)*//*

                TODO Look into this Jeffrey
                slide.addASharedRuntimeContent.getSlideAt(mSlidePosition).getAudio()*/
                slide.addResource(image, Slide.ResourceType.IMAGE);
                slide.setThumbnail(thumbnail);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(from.equals("AudioRecording")) {

                SharedRuntimeContent.changeSlideAtPosition(mSlidePosition, slide);
            }

            else {
                try {
                    SharedRuntimeContent.addSlide(slide);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mPleaseWait.setVisibility(View.GONE);
            finish();
        }
    };

    private class ProcessAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Bitmap photo = mCropImageCoordinator.getCroppedImage();
            mCroppedImagePath = mStorage.getRealPathFromURI(mStorage.getImageUri(photo));
            mStorage.getImageThumbnailAsync(new File(mCroppedImagePath).getAbsolutePath(), mThumbnailCreated, mProgressDialog);
            return null;

        }
    }

    private class ProcessAsyncAfteCrop extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        ProcessAsyncAfteCrop(Bitmap bitmap)
        {
            this.bitmap= bitmap;
        }
        @Override
        protected Void doInBackground(Void... params) {

            Bitmap photo = bitmap;
            mCroppedImagePath = mStorage.getRealPathFromURI(mStorage.getImageUri(photo));
            mStorage.getImageThumbnailAsync(new File(mCroppedImagePath).getAbsolutePath(), mThumbnailCreated, mProgressDialog);
            return null;

        }
    }
}
