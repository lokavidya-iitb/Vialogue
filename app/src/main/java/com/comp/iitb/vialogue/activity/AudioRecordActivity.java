package com.comp.iitb.vialogue.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.SlideThumbnailsRecyclerViewAdapter;
import com.comp.iitb.vialogue.coordinators.MediaTimeUpdateListener;
import com.comp.iitb.vialogue.coordinators.OnFileCopyCompleted;
import com.comp.iitb.vialogue.coordinators.OnSlideThumbnailClicked;
import com.comp.iitb.vialogue.coordinators.OnThumbnailCreated;
import com.comp.iitb.vialogue.coordinators.RecordTimeUpdateListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.AudioRecorder;
import com.comp.iitb.vialogue.library.SetSlideThumbnailsRecyclerViewAdapterAsyncTask;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.library.TimeFormater;
import com.comp.iitb.vialogue.library.VideoThumbnailAsync;
import com.comp.iitb.vialogue.listeners.CameraImagePicker;
import com.comp.iitb.vialogue.listeners.CameraImagePickerActivity;
import com.comp.iitb.vialogue.listeners.FileCopyUpdateListener;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Audio;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Video;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.CanSaveAudioResource;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.os.Build.VERSION.SDK_INT;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.CROP_MAIN_ACTIVITY_RESULT;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_CAMERA_IMAGE;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_IMAGE;
import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.GET_VIDEO;

/**
 * Created by shubh on 17-01-2017.
 */
public class AudioRecordActivity extends AppCompatActivity implements MediaTimeUpdateListener, RecordTimeUpdateListener {

    // Constants
    private static final String LOG_TAG = "AudioRecordActivity";
    public static final String SLIDE_NO = "recordPath";
    public static final String IMAGE_PATH = "imagePath";
    public static final String RECORD_NAME = "recordName";
    public static final String FOLDER_PATH = "folderPath";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private Toolbar mToolbar;
    private String currentImagePath;
    private SeekBar mSeekBar;
    private Button mRecordButton = null;
    private Button mStopButton = null;
    private Button mRetryButton = null;
    private ImageButton mPlayButton = null;
    private ImageView mImageView;
    private Audio mAudio;
    private Button mImagePicker;
    private Button mCameraPicker;
    private CanSaveAudioResource mSlideResource;
    private RecyclerView mSlideThumbnailsRecyclerView;
    private TextView mTimerTextView;
    private AudioRecorder mAudioRecorder = null;
    private String mRecordPath;
    private String mImagePath;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private Storage mStorage;
    // Requesting permission to RECORD_AUDIO
    private Button mDone;
    private Slide mSlide;
    private int mSlidePosition;
    private static File mCameraImageFile = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
        }
        Toast.makeText(AudioRecordActivity.this, R.string.gimmeAudio, Toast.LENGTH_LONG).show();
        endActivity();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        System.out.println("audioRecordActivity : onCreate : called");
        setContentView(R.layout.activity_audio_record);
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(!(ContextCompat.checkSelfPermission(AudioRecordActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }

        // Initialize UI Components
        mCameraPicker = (Button)findViewById(R.id.camera_image_picker);
        mImagePicker = (Button)findViewById(R.id.image_picker);
        mDone = (Button) findViewById(R.id.done_button);
        mImageView = (ImageView) findViewById(R.id.selected_image);
        mStopButton = (Button) findViewById(R.id.stop_button);
        mRetryButton = (Button) findViewById(R.id.retry);
        mSeekBar = (SeekBar) findViewById(R.id.audio_seek);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPlayButton = (ImageButton) findViewById(R.id.play_button);
        mRecordButton = (Button) findViewById(R.id.record_button);
        mTimerTextView = (TextView) findViewById(R.id.timer_text_view);

        // Initialize ActionBar related stuff
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        bundle = getIntent().getExtras();

        // Initialize variables
        mStorage = new Storage(getApplicationContext());
        if(mCameraImageFile == null) {
            mCameraImageFile = BaseResourceClass.makeTempResourceFile(Slide.ResourceType.IMAGE, AudioRecordActivity.this);
        }

        // Load State
        if (bundle != null) {
            mSlidePosition = bundle.getInt(SLIDE_NO);
        }

        // Setup Listeners
        mCameraPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    stopRecording();
                }
                stopRecording();
                Intent intent = new Intent(AudioRecordActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.CAPTURE_SINGLE_IMAGE_INTENT_KEY, "true");
                startActivityForResult(intent, SharedRuntimeContent.GET_SINGLE_CAMERA_IMAGE);
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraImageFile));
//                startActivityForResult(cameraIntent, SharedRuntimeContent.GET_CAMERA_IMAGE);
            }
        });

        mImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    stopRecording();
                }
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GET_IMAGE);
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save the recording
                if(isRecording) {
                    stopRecording();
                }
//                String selectedPath = mSlideResource.getResourceFile().getAbsolutePath();
//                Intent intent = new Intent(getBaseContext(), CropMainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("SlidePosition",mSlidePosition);
//                bundle.putString("from", "AudioRecording");
//                bundle.putString(CropMainActivity.IMAGE_PATH, currentImagePath);
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                startActivity(intent);
//                finish();
                startCropMainActivity();
            }
        });

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    stopRecording();
                }
                endActivity();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean isTouch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isTouch)
                    mAudioRecorder.seekTo(progress);
                setTimeDisplay(progress, false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = false;
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    Snackbar.make(mPlayButton, R.string.cannot_play, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (isPlaying) {
                    if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        mPlayButton.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    else
                        mPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                } else {
                    if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        mPlayButton.setImageDrawable(getDrawable(R.drawable.ic_pause_black_24dp));
                    else
                        mPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                }
                if (mSeekBar.getProgress() != mSeekBar.getMax() && !isPlaying)
                    mAudioRecorder.onPlay(mSeekBar.getProgress());
                else {
                    mAudioRecorder.onPlay();
                }
                isPlaying = !isPlaying;
            }
        });

        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    Snackbar.make(mRecordButton, R.string.cannot_record, Snackbar.LENGTH_LONG).show();
                    return;
                }
                setTimeDisplay(0, true);
                /*mRecordButton.setBackgroundColor(getResources().getColor(R.color.sysWhite));*/
                mAudioRecorder.onRecord(!isRecording);
                isRecording = !isRecording;
                mRecordButton.setEnabled(false);
                mStopButton.setEnabled(true);
                mRetryButton.setEnabled(false);
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    Snackbar.make(mRetryButton, R.string.cannot_record, Snackbar.LENGTH_LONG).show();
                    return;
                }
                mRecordButton.setEnabled(true);
                mRecordButton.setText(R.string.record_audio);
                mPlayButton.setEnabled(false);
                isRecording = false;
                mStopButton.setEnabled(false);
                mRetryButton.setEnabled(false);

                // remove audio from slide
                mSlideResource.removeAudio();
                try {
                    mSlide.addResource(mSlideResource, Slide.ResourceType.IMAGE);
                } catch (Exception e) {}
                SharedRuntimeContent.changeSlideAtPosition(mSlidePosition, mSlide);
                SharedRuntimeContent.updateAdapterView();
                mTimerTextView.setText(R.string.timer_null_text);

                mSlideThumbnailsRecyclerView.getAdapter().notifyItemChanged(mSlidePosition);
            }
        });

        // Initialize SlideThumbnails Recycler View
        mSlideThumbnailsRecyclerView = (RecyclerView) findViewById(R.id.slide_thumbnails_recycler_view);
        mSlideThumbnailsRecyclerView.setAdapter(new SlideThumbnailsRecyclerViewAdapter(AudioRecordActivity.this));
        new SetSlideThumbnailsRecyclerViewAdapterAsyncTask(
                AudioRecordActivity.this,
                AudioRecordActivity.this,
                mSlidePosition,
                mSlideThumbnailsRecyclerView,
                new OnSlideThumbnailClicked() {
                    @Override
                    public void onClicked(Slide slide, int slidePosition) {
                        // if recording going on, stop recording and save current slide
                        if(isRecording) {
                            stopRecording();
                        }

                        // load the new state and UI
                        loadStateFromSlide(slide, slidePosition);
                        setUpUI();
                    }
                }
        ).execute();
    }

    public void startCropMainActivity(String path) {
        stopRecording();
        Intent intent = new Intent(getBaseContext(), CropMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("SlidePosition",mSlidePosition);
        bundle.putString("from", "AudioRecording");
        bundle.putString("imagePath", path);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(intent, SharedRuntimeContent.CROP_MAIN_ACTIVITY_RESULT);
    }
    public void startCropMainActivity() {
        stopRecording();
        Intent intent = new Intent(getBaseContext(), CropMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("SlidePosition",mSlidePosition);
        bundle.putString("from", "AudioRecording");
        bundle.putString("imagePath", currentImagePath);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(intent, SharedRuntimeContent.CROP_MAIN_ACTIVITY_RESULT);
    }


    public void loadStateFromSlide(Slide slide, int slidePosition) {
        loadStateFromSlide(slide, slidePosition, true);
    }

    public void loadStateFromSlide(Slide slide, int slidePosition, boolean replaceCurrentAudio) {
        // TODO think of some other way to handle this
        try {
            CanSaveAudioResource s = (CanSaveAudioResource) slide.getResource();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
            Log.e("AudioRecordActivity", "Slide Resource class does not implement CanSaveAudioResource");
            endActivity();
        }

        mSlide = slide;
        mSlidePosition = slidePosition;
        mSlideResource = (CanSaveAudioResource) slide.getResource();
        mImagePath = mSlideResource.getResourceFile().getAbsolutePath();
        mAudio = mSlideResource.getAudio();
        if(mAudio == null) {
            // slide does not contain Audio
            mAudio = new Audio(AudioRecordActivity.this);
        }
    }

    private void setUpUI() {
        if (!((Image) mSlide.getResource()).hasAudio()) {
            mSeekBar.setEnabled(false);
            mSeekBar.invalidate();
            mSeekBar.requestLayout();
            mPlayButton.setEnabled(false);
            mRetryButton.setEnabled(false);
            mRecordButton.setEnabled(true);
            mStopButton.setEnabled(false);
            mTimerTextView.setText(R.string.timer_null_text);
        } else {
            mSeekBar.setEnabled(true);
            mSeekBar.invalidate();
            mSeekBar.requestLayout();
            mPlayButton.setEnabled(true);
            mRetryButton.setEnabled(true);
            mRecordButton.setEnabled(false);
            mRecordButton.setText(R.string.record_audio);
            mStopButton.setEnabled(false);
            int duration = mStorage.getAudioFileDuration(mAudio.getResourceFile().getAbsolutePath());
            if(duration != 0) {
                setSeekBarTime(0, duration);
                setTimeDisplay(duration, true);
            }

        }

        if (mAudioRecorder != null && !isPlaying && !isRecording) {
            mAudioRecorder.release();
        }
        if (!isPlaying && !isRecording) {
            mAudioRecorder = new AudioRecorder(mAudio.getResourceFile().getAbsolutePath(), this, this);
        }

        Uri imagePathUri = mStorage.getUriFromPath(mImagePath);
        if (imagePathUri != null) {
            currentImagePath=mImagePath;
            Glide
                    .with(this)
                    .load(imagePathUri)
                    .placeholder(R.drawable.app_logo)
                    .into(mImageView);
        }
    }

    public void stopRecording() {
        mAudioRecorder.onRecord(false);
        isRecording = false;
        mStopButton.setEnabled(false);
        mRetryButton.setEnabled(true);
        mSlideResource.addAudio(mAudio);
        mPlayButton.setEnabled(true);
        try {
            mSlide.addResource(mSlideResource, Slide.ResourceType.IMAGE);
        } catch (Exception e) {}
        SharedRuntimeContent.changeSlideAtPosition(mSlidePosition, mSlide);
        SharedRuntimeContent.updateAdapterView();
        setUpUI();
        mSlideThumbnailsRecyclerView.getAdapter().notifyItemChanged(mSlidePosition);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAudioRecorder != null) {
            mAudioRecorder.release();
        }
    }

    @Override
    public void onMediaTimeUpdate(int currentTime, int totalTime) {
        setSeekBarTime(currentTime, totalTime);
    }

    public void setSeekBarTime(int currentTime, int totalTime) {
        mSeekBar.setMax(totalTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSeekBar.setProgress(currentTime, true);
        } else {
            mSeekBar.setProgress(currentTime);
        }
        setTimeDisplay(currentTime, false);
    }

    public void setSeekBarTime(int currentTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSeekBar.setProgress(currentTime, true);
        } else {
            mSeekBar.setProgress(currentTime);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                endActivity();
                break;
        }
        return true;
    }

    @Override
    public void onMediaTimeEndReached() {
        isPlaying = false;
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mPlayButton.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
        else
            mPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        setSeekBarTime(0);
    }

    private void setTimeDisplay(int currentTime, boolean displayWhenNotPlaying) {
        String formatTime = TimeFormater.getMinutesAndSeconds(currentTime);
        if(displayWhenNotPlaying) {
            mTimerTextView.setText(formatTime);
        } else {
            if (isPlaying) {} else if (isRecording) {
                mTimerTextView.setText(formatTime);
            }
        }

    }

    @Override
    public void onRecordTimeUpdate(int time) {
        setTimeDisplay(time, false);
    }

    @Override
    public void onRecordStopped() {
        isRecording = false;
        setUpUI();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AudioRecord Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStateFromSlide(SharedRuntimeContent.getSlideAt(mSlidePosition), mSlidePosition);

        // Setup the User Interface
        setUpUI();
        mAudioRecorder.setRelativeUpdate(mSeekBar.getWidth());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SLIDE_NO, mSlidePosition);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getName(), requestCode + " " + resultCode);
        if (resultCode == RESULT_OK) {
            handlePickedData(requestCode, data);
        } else {
            System.out.println("onActivityResutl : boooo");
            Toast.makeText(this, R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
        }
    }

    public void handlePickedData(int requestCode, Intent data) {
        Log.d(getClass().getName(), "data " + String.valueOf(data == null));

        if (requestCode == GET_CAMERA_IMAGE && data == null) {
            currentImagePath = mCameraImageFile.getAbsolutePath();
            System.out.println("----" + currentImagePath);
            startCropMainActivity(currentImagePath);

        } else if (requestCode == GET_IMAGE) {
            // GET IMAGE FROM GALLERY
            if (data != null) {
                currentImagePath = mStorage.getRealPathFromURI(data.getData());
                startCropMainActivity(currentImagePath);
            }
        } else if(requestCode == SharedRuntimeContent.CROP_MAIN_ACTIVITY_RESULT) {
            // Update State (but don't replace the current audio)
            loadStateFromSlide(SharedRuntimeContent.getSlideAt(mSlidePosition), mSlidePosition, false);
            // Update UI
            setUpUI();
            // Update thumbnails
            mSlideThumbnailsRecyclerView.getAdapter().notifyItemChanged(mSlidePosition);
        } else if(requestCode == SharedRuntimeContent.GET_SINGLE_CAMERA_IMAGE) {
            ArrayList<String> paths = data.getStringArrayListExtra(CameraActivity.RESULT_KEY);
            currentImagePath = paths.get(0);
            System.out.println(currentImagePath);
            startCropMainActivity();
        }
    }

    public void endActivity() {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("AudioRecordActivity : onDestroy : called");
        mImageView.setImageBitmap(null);
    }

}