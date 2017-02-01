package com.comp.iitb.vialogue.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.MediaTimeUpdateListener;
import com.comp.iitb.vialogue.coordinators.RecordTimeUpdateListener;
import com.comp.iitb.vialogue.library.AudioRecorder;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.library.TimeFormater;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

/**
 * Created by shubh on 17-01-2017.
 */
public class AudioRecordActivity extends AppCompatActivity implements MediaTimeUpdateListener, RecordTimeUpdateListener {

    private static final String LOG_TAG = "AudioRecordActivity";
    public static final String RECORD_PATH = "recordPath";
    public static final String IMAGE_PATH = "imagePath";
    public static final String RECORD_NAME = "recordName";
    public static final String FOLDER_PATH = "folderPath";
    private Toolbar mToolbar;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String mRecordName = null;
    private String mFolderPath = null;
    private AppCompatSeekBar mSeekBar;
    private Button mRecordButton = null;
    private Button mStopButton = null;
    private Button mRetryButton = null;
    private ImageButton mPlayButton = null;
    private ImageView mImageView;

    private AudioRecorder mAudioRecorder = null;
    private String mRecordPath;
    private String mImagePath;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private Storage mStorage;
    private TextView mTimeDisplay;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted)
            finish();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_audio_record);
        // Record to the external cache directory for visibility
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mRecordPath = bundle.getString(RECORD_PATH);
            mImagePath = bundle.getString(IMAGE_PATH);
            mFolderPath = bundle.getString(FOLDER_PATH);
            mRecordName = bundle.getString(RECORD_NAME);
            Log.d(LOG_TAG, mRecordName + "  " + mRecordPath + " " + mImagePath + " " + mFolderPath);
        }

        mStorage = new Storage(this);
        mImageView = (ImageView) findViewById(R.id.selected_image);
        mImageView = (ImageView) findViewById(R.id.selected_image);
        mStopButton = (Button) findViewById(R.id.stop_button);
        mRetryButton = (Button) findViewById(R.id.retry);
        mSeekBar = (AppCompatSeekBar) findViewById(R.id.audio_seek);
        mPlayButton = (ImageButton) findViewById(R.id.play_button);
        mTimeDisplay = (TextView) findViewById(R.id.time_display);
        mRecordButton = (Button) findViewById(R.id.record_button);
        setUpUI();
        mImageView.setImageURI(Uri.parse(mImagePath));
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
                isPlaying = !isPlaying;
                if (mAudioRecorder.isPlayPrepared() || mSeekBar.getProgress() == mSeekBar.getMax())
                    mAudioRecorder.onPlay();
                else {
                    mAudioRecorder.onPlay(mSeekBar.getProgress());
                }
            }
        });

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    Snackbar.make(mRecordButton, R.string.cannot_record, Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (permissionToRecordAccepted) {
                    mAudioRecorder.onRecord(!isRecording);
                    isRecording = !isRecording;
                    mRecordButton.setEnabled(false);
                    mStopButton.setEnabled(true);
                }
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionToRecordAccepted) {
                    mAudioRecorder.onRecord(false);
                    isRecording = false;
                    mStopButton.setEnabled(false);
                    mRetryButton.setEnabled(true);
                }
            }
        });
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    Snackbar.make(mRetryButton, R.string.cannot_record, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (permissionToRecordAccepted) {
                    mAudioRecorder.onRecord(true);
                    isRecording = true;
                    mStopButton.setEnabled(true);
                    mRetryButton.setEnabled(false);
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        if (mAudioRecorder != null) {
            mAudioRecorder.release();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onMediaTimeUpdate(int currentTime, int totalTime) {
        //TODO: Calculate seek and update Timer
        mSeekBar.setMax(totalTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSeekBar.setProgress(currentTime, true);
        } else {
            mSeekBar.setProgress(currentTime);
        }
        setmTimeDisplay(currentTime);
        //Log.d(LOG_TAG, formatTime + " play time " + currentTime + " total " + totalTime);
    }

    @Override
    public void onMediaTimeEndReached() {
        isPlaying = false;
    }

    private void setmTimeDisplay(int currentTime) {
        Log.d(LOG_TAG, "Timew display " + currentTime);
        String formatTime = TimeFormater.getMinutesAndSeconds(currentTime);
        mTimeDisplay.setText(formatTime);
        Log.d(LOG_TAG, "Timew display last" + currentTime);
    }

    @Override
    public void onRecordTimeUpdate(int time) {
        setmTimeDisplay(time);
        Log.d(LOG_TAG, "record time " + time);
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAudioRecorder.setRelativeUpdate(mSeekBar.getWidth());
    }

    private void setUpUI() {
        File file = null;
        if (mFolderPath != null && mRecordName != null) {
            file = new File(mStorage.getStorageDir(mFolderPath, true), mRecordName);
            Log.d(LOG_TAG, "file is" + file);
        }
        if (!file.exists()) {
            mSeekBar.setEnabled(false);
            mPlayButton.setEnabled(false);
            mRetryButton.setEnabled(false);

            mRecordButton.setEnabled(true);
            mStopButton.setEnabled(true);
        } else {
            mSeekBar.setEnabled(true);
            mPlayButton.setEnabled(true);
            mRetryButton.setEnabled(true);

            mRecordButton.setEnabled(false);
            mStopButton.setEnabled(false);
        }
        if (mAudioRecorder != null)
            mAudioRecorder.release();
        mAudioRecorder = new AudioRecorder(file.getAbsolutePath(), this, this);
    }

}