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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import java.io.File;

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
    private String mRecordName = null;
    private SeekBar mSeekBar;
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
    private Button mDone;
    private Slide mSlide;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(SLIDE_NO);
            mSlide = SharedRuntimeContent.getSlideAt(position);

            Image image = (Image) mSlide.getResource();
            mImagePath = image.getFile().getUrl();

            Audio audio;
            try {
                audio = (Audio) image.getChildrenResources().get(0);
                mRecordName = audio.getFile().getUrl();
            } catch (Exception e) {
                // Throws an error if image.getChildrenResources() is null
                // or image.getChildrenResources().get(0) is null
                // both of which mean that audio is not present for the corresponding slide
                mRecordName = null;
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
        setUpUI();
        Uri imagePathUri = mStorage.getUriFromPath(mImagePath);
        if (imagePathUri != null)
            mImageView.setImageURI(imagePathUri);
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

        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
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
                    mRetryButton.setEnabled(false);
                }
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionToRecordAccepted) {
                    mAudioRecorder.onRecord(false);
                    isRecording = false;
                    setUpUI();
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
        if (isPlaying) {
            mTimeDisplay.setText(formatTime);
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
        File file = null;
        if (mRecordName != null) {
            file = new File(mRecordName);
        }
        Log.d(LOG_TAG, "hello file " + String.valueOf(file.exists()));

        if ((file == null) || (!file.exists())) {
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
            try {
                mSlide.addResource(new Audio(Uri.parse(file.getAbsolutePath())), Slide.ResourceType.AUDIO);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "something went wrong :(", Toast.LENGTH_SHORT).show();
            }
            mRecordButton.setEnabled(false);
            mStopButton.setEnabled(false);
        }
        if (mAudioRecorder != null && !isPlaying && !isRecording) {
            mAudioRecorder.release();
        }
        if (!isPlaying && !isRecording)
            mAudioRecorder = new AudioRecorder(file.getAbsolutePath(), this, this);
    }

}