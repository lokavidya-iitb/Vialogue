package com.comp.iitb.vialogue.library;

/**
 * Created by shubh on 20-01-2017.
 */

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.comp.iitb.vialogue.coordinators.MediaTimeUpdateListener;
import com.comp.iitb.vialogue.coordinators.RecordTimeUpdateListener;

import java.io.IOException;

/**
 * Created by shubh on 17-01-2017.
 */

public class AudioRecorder {
    private int mUpdateInterval;
    private static int RECORD_UPDATE_INTERVAL = 1000;
    private MediaRecorder mRecorder = null;
    private String mFileName;
    private final String LOG_TAG = "AudioRecorder";
    private MediaPlayer mPlayer = null;
    private MediaTimeUpdateListener mMediaTimeUpdate;
    private RecordTimeUpdateListener mRecordTimeUpdate;
    private Handler mHandler;
    private boolean mCompletedPlaying;
    private boolean mCompletedRecording;
    private int mWidth;

    public AudioRecorder(@NonNull String fileName, MediaTimeUpdateListener mediaTimeUpdateListener, RecordTimeUpdateListener recordTimeUpdateListener) {
        Log.d(LOG_TAG, "here AudioRecorder");
        mFileName = fileName;
        mMediaTimeUpdate = mediaTimeUpdateListener;
        mRecordTimeUpdate = recordTimeUpdateListener;
        mHandler = new Handler();
        mUpdateInterval = 10;
    }


    private void startRecording() {
        Log.d(LOG_TAG, "startRecording");
        mCompletedRecording = false;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mRecorder.prepare();
            if (mRecordTimeUpdate != null) {
                mHandler.postDelayed(new Runnable() {
                    private int time = 1000;

                    @Override
                    public void run() {
                        if (!mCompletedRecording) {
                            mHandler.postDelayed(this, RECORD_UPDATE_INTERVAL);
                            mRecordTimeUpdate.onRecordTimeUpdate(time);
                            time += 1000;
                        }

                    }
                }, RECORD_UPDATE_INTERVAL);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }

    private void stopRecording() {
        Log.d(LOG_TAG, "stopRecording");
        if (mRecorder != null) {
            Log.d(LOG_TAG, "stopRecording");
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            mCompletedRecording = true;
            if (mRecordTimeUpdate != null)
                mRecordTimeUpdate.onRecordStopped();
        }
    }

    public void release() {
        Log.d(LOG_TAG, "release");
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void onRecord(boolean start) {
        Log.d(LOG_TAG, "onRecord " + String.valueOf(start));
        if (start) {
            Log.d(LOG_TAG, "Started Recording");
            startRecording();
        } else {
            Log.d(LOG_TAG, "Stopped Recording");
            stopRecording();
        }
    }

    public void onPlay() {
        Log.d(LOG_TAG, "onPlay");
        if (mPlayer != null && mPlayer.isPlaying()) {
            Log.d(LOG_TAG, "mPlayer isPlaying");
            mPlayer.pause();
        } else {
            Log.d(LOG_TAG, "mPlayer null");
            startPlaying();
        }
    }

    private void startPlaying() {
        Log.d(LOG_TAG, "startPlaying");
        mPlayer = new MediaPlayer();
        mCompletedPlaying = false;
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            if (mWidth != 0)
                mUpdateInterval = mPlayer.getDuration() / mWidth;
            mPlayer.start();
            if (mMediaTimeUpdate != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mCompletedPlaying && mPlayer != null && mPlayer.isPlaying()) {
                            mMediaTimeUpdate.onMediaTimeUpdate(mPlayer.getCurrentPosition(), mPlayer.getDuration());
                            mHandler.postDelayed(this, mUpdateInterval);
                        }
                    }
                }, mUpdateInterval);
            }
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mCompletedPlaying = true;
                    if (mMediaTimeUpdate != null && mPlayer != null) {
                        mMediaTimeUpdate.onMediaTimeUpdate(mPlayer.getDuration(), mPlayer.getDuration());
                    }
                    Log.d(LOG_TAG, "completed playing");
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void seekTo(int milliseconds) {
        if (mPlayer != null)
            mPlayer.seekTo(milliseconds);
    }

    private void stopPlaying() {
        if (mPlayer != null)
            mPlayer.release();
        mPlayer = null;
    }

    public boolean isPlayPrepared() {
        if (mPlayer == null)
            return false;
        else
            return true;

    }

    public void onPlay(int progress) {
        startPlaying();
        mPlayer.seekTo(progress);
    }

    public void setRelativeUpdate(int width) {
        mWidth = width;
    }
}