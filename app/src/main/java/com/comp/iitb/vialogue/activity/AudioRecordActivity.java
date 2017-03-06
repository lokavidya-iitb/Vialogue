package com.comp.iitb.vialogue.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.MediaTimeUpdateListener;
import com.comp.iitb.vialogue.coordinators.RecordTimeUpdateListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.AudioRecorder;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.library.TimeFormater;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Audio;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.CanSaveAudioResource;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by shubh on 17-01-2017.
 */
public class AudioRecordActivity extends AppCompatActivity implements MediaTimeUpdateListener, RecordTimeUpdateListener {

    private static final String LOG_TAG = "AudioRecordActivity";
    public static final String SLIDE_NO = "recordPath";
    public static final String IMAGE_PATH = "imagePath";
    public static final String RECORD_NAME = "recordName";
    public static final String FOLDER_PATH = "folderPath";
    private Toolbar mToolbar;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SeekBar mSeekBar;
    private Button mRecordButton = null;
    private Button mStopButton = null;
    private Button mRetryButton = null;
    private ImageButton mPlayButton = null;
    private ImageView mImageView;
    private Audio mAudio;
    CanSaveAudioResource mSlideResource;

    private AudioRecorder mAudioRecorder = null;
    private String mRecordPath;
    private String mImagePath;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private Storage mStorage;
    private TextView mTimeDisplay;
    // Requesting permission to RECORD_AUDIO
    private Button mDone;
    private Slide mSlide;
    private int mSlidePosition;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
        }
        Toast.makeText(AudioRecordActivity.this, "Audio record feature can only be used if Microphone permission is granted", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_audio_record);

        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(!(ContextCompat.checkSelfPermission(AudioRecordActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }

        // Record to the external cache directory for visibility
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mSlidePosition = bundle.getInt(SLIDE_NO);
            mSlide = SharedRuntimeContent.getSlideAt(mSlidePosition);

            // TODO think of some other way to handle this
            try {
                CanSaveAudioResource s = (CanSaveAudioResource) mSlide.getResource();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "something went wrong :(", Toast.LENGTH_SHORT).show();
                Log.e("AudioRecordActivity", "Slide Resource class does not implement CanSaveAudioResource");
                finish();
            }

            mSlideResource = (CanSaveAudioResource) mSlide.getResource();
            mImagePath = mSlideResource.getResourceFile().getAbsolutePath();
            mAudio = mSlideResource.getAudio();
            if(mAudio == null) {
                mAudio = new Audio(getBaseContext());
            }
        }

        mDone = (Button) findViewById(R.id.done_button);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mStorage = new Storage(getApplicationContext());
        mImageView = (ImageView) findViewById(R.id.selected_image);
        mImageView = (ImageView) findViewById(R.id.selected_image);
        mStopButton = (Button) findViewById(R.id.stop_button);
        mRetryButton = (Button) findViewById(R.id.retry);
        mSeekBar = (SeekBar) findViewById(R.id.audio_seek);

        mPlayButton = (ImageButton) findViewById(R.id.play_button);
        mTimeDisplay = (TextView) findViewById(R.id.time_display);
        mRecordButton = (Button) findViewById(R.id.record_button);
        mTimeDisplay.setVisibility(View.GONE);
        setUpUI();
        Uri imagePathUri = mStorage.getUriFromPath(mImagePath);
        if (imagePathUri != null) {
            mImageView.setImageURI(imagePathUri);
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean isTouch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isTouch)
                    mAudioRecorder.seekTo(progress);
                setmTimeDisplay(progress);
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
                mAudioRecorder.onRecord(false);
                isRecording = false;
                setUpUI();
                mStopButton.setEnabled(false);
                mRetryButton.setEnabled(true);
                mSlideResource.addAudio(mAudio);
                try {
                    mSlide.addResource(mSlideResource, Slide.ResourceType.IMAGE);
                } catch (Exception e) {}
                SharedRuntimeContent.changeSlideAtPosition(mSlidePosition, mSlide);
                SharedRuntimeContent.updateAdapterView();
            }
        });

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    Snackbar.make(mRetryButton, R.string.cannot_record, Snackbar.LENGTH_LONG).show();
                    return;
                }
                mAudioRecorder.onRecord(true);
                isRecording = true;
                mStopButton.setEnabled(true);
                mRetryButton.setEnabled(false);
            }
        });
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
        mSeekBar.setMax(totalTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSeekBar.setProgress(currentTime, true);
        } else {
            mSeekBar.setProgress(currentTime);
        }
        setmTimeDisplay(currentTime);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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
    }

    private void setmTimeDisplay(int currentTime) {
        String formatTime = TimeFormater.getMinutesAndSeconds(currentTime);
        if (isPlaying) {/*
            mTimeDisplay.setText(formatTime);*/
        } else if (isRecording) {
            mRecordButton.setText(formatTime);
        }
    }

    @Override
    public void onRecordTimeUpdate(int time) {
        setmTimeDisplay(time);
    }

    @Override
    public void onRecordStopped() {
        setUpUI();
        isRecording = false;
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
        mAudioRecorder.setRelativeUpdate(mSeekBar.getWidth());
    }

    private void setUpUI() {
        if (!((Image) mSlide.getResource()).hasAudio()) {
            mSeekBar.setEnabled(false);
            mSeekBar.invalidate();
            mSeekBar.requestLayout();
            mPlayButton.setEnabled(false);
            mRetryButton.setEnabled(false);
            mRecordButton.setEnabled(true);
            mStopButton.setEnabled(true);
        } else {
            mSeekBar.setEnabled(true);
            mSeekBar.invalidate();
            mSeekBar.requestLayout();
            mPlayButton.setEnabled(true);
            mRetryButton.setEnabled(true);
            mRecordButton.setEnabled(false);
            mStopButton.setEnabled(false);
        }

        if (mAudioRecorder != null && !isPlaying && !isRecording) {
            mAudioRecorder.release();
        }
        if (!isPlaying && !isRecording)
            mAudioRecorder = new AudioRecorder(mAudio.getResourceFile().getAbsolutePath(), this, this);
    }

}