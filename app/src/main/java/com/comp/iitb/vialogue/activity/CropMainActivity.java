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
import android.graphics.BitmapFactory;
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
            try {
                from = bundle.getString("from");
                mFilePath = bundle.getString(IMAGE_PATH);
                System.out.print("--------"+mFilePath);
                switch (from) {
                    case "AudioRecording":
                        mSlidePosition = bundle.getInt("SlidePosition");
                        break;
                    case "CreateVideos":
                        mSlidePosition = -1;
                        break;
                }

            } catch (NullPointerException e) {
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
        new ProcessAsync().execute();
    }

    public void done(Bitmap bitmap) {
        mDone.setEnabled(false);
        mPleaseWait.setVisibility(View.VISIBLE);
        new ProcessAsyncAfterCrop(bitmap).execute();
    }


//    private OnThumbnailCreated mThumbnailCreated = new OnThumbnailCreated() {
//        @Override
//        public void onThumbnailCreated(Bitmap thumbnail) {
//            System.out.println("onThumbnailCreated : called");
//            (new AsyncTask<Bitmap, Void, Slide>() {
//                public Slide doInBackground(Bitmap... params) {
//                    Bitmap thumbnail = params[0];
//                    Slide slide = new Slide();
//                    try {
//                        Image image = new Image(getBaseContext());
//                        mStorage.getBitmap(mCroppedImagePath);
//                        mStorage.saveBitmapToFile(image.getResourceFile(), mStorage.getBitmap(mCroppedImagePath));
//
//                        /*SharedRuntimeContent.getSlideAt(mSlidePosition)*//*
//                        TODO Look into this Jeffrey
//                        slide.addASharedRuntimeContent.getSlideAt(mSlidePosition).getAudio()*/
//
//                        slide.addResource(image, Slide.ResourceType.IMAGE);
//                        slide.setThumbnail(thumbnail);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    return slide;
//                }
//
//                @Override
//                public void onPostExecute(Slide slide) {
//                    if (from.equals("AudioRecording")) {
//                        SharedRuntimeContent.changeSlideAtPosition(mSlidePosition, slide);
//                    } else {
//                        try {
//                            SharedRuntimeContent.addSlide(slide);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    mPleaseWait.setVisibility(View.GONE);
//                    finish();
//                }
//            }).execute(thumbnail);
//
//        }
//    };

    private class ProcessAsync extends AsyncTask<Void, Void, Slide> {

        private Bitmap mPhoto;

        @Override
        protected Slide doInBackground(Void... params) {

            mPhoto = mCropImageCoordinator.getCroppedImage();
            mPhoto = BitmapFactory.decodeFile(mCroppedImagePath);
            mCroppedImagePath = mStorage.getRealPathFromURI(mStorage.getImageUri(mPhoto));

            Slide slide = new Slide();

            try {
                Image image = new Image(getBaseContext());
                mStorage.saveBitmapToFile(image.getResourceFile(), mStorage.getBitmap(mCroppedImagePath));
                slide.addResource(image, Slide.ResourceType.IMAGE);
            } catch (Exception e) {}
            return slide;
        }

        @Override
        public void onPostExecute(Slide slide) {
            if (from.equals("AudioRecording")) {
                // update current slide
                if(((Image) SharedRuntimeContent.getSlideAt(mSlidePosition).getResource()).hasAudio()) {
                    ((Image) slide.getResource()).addAudio(((Image) SharedRuntimeContent.getSlideAt(mSlidePosition).getResource()).getAudio());
                }
                SharedRuntimeContent.changeSlideAtPosition(mSlidePosition, slide);

                // clearing bitmap to release memory
                /*Storage.recycleBitmap(mPhoto);*/

                // sending result (which is nothing)
                setResult(RESULT_OK);
                finish();
            } else {
                // add new slide
                try {
                    SharedRuntimeContent.addSlide(slide);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // clearing bitmap to release memory
                /*Storage.recycleBitmap(mPhoto);*/

                // finishing activity
                finish();
            }
            mPleaseWait.setVisibility(View.GONE);
            System.out.println("hullallal");

            finish();
        }
    }

    private class ProcessAsyncAfterCrop extends AsyncTask<Void, Void, Slide> {
        private Bitmap mBitmap;

        ProcessAsyncAfterCrop(Bitmap bitmap) {
            mBitmap = bitmap;
        }

        @Override
        protected Slide doInBackground(Void... params) {

            mCroppedImagePath = mStorage.getRealPathFromURI(mStorage.getImageUri(mBitmap));
            Slide slide = new Slide();
            try {
                Image image = new Image(getBaseContext());
                mStorage.saveBitmapToFile(image.getResourceFile(), mStorage.getBitmap(mCroppedImagePath));
                slide.addResource(image, Slide.ResourceType.IMAGE);
            } catch (Exception e) {}
            return slide;
        }

        @Override
        public void onPostExecute(Slide slide) {
            if (from.equals("AudioRecording")) {
                if(((Image) SharedRuntimeContent.getSlideAt(mSlidePosition).getResource()).hasAudio()) {
                    ((Image) slide.getResource()).addAudio(((Image) SharedRuntimeContent.getSlideAt(mSlidePosition).getResource()).getAudio());
               System.out.println("----------audio is there");
                }
                SharedRuntimeContent.changeSlideAtPosition(mSlidePosition, slide);
            } else {
                try {
                    SharedRuntimeContent.addSlide(slide);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mPleaseWait.setVisibility(View.GONE);
            System.out.println("hullallal");

            // clearing bitmap to release memory
            /*Storage.recycleBitmap(mBitmap);*/
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // calling garbage collector
        Runtime.getRuntime().gc();
    }
}
