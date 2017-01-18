package com.comp.iitb.vialogue.library;

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
    private final int UPDATE_INTERVAL = 1000;
    private MediaRecorder mRecorder = null;
    private String mFileName;
    private final String LOG_TAG = "AudioRecorder";
    private MediaPlayer mPlayer = null;
    private MediaTimeUpdateListener mMediaTimeUpdate;
    private RecordTimeUpdateListener mRecordTimeUpdate;
    private Handler mHandler;
    private boolean mCompletedPlaying;
    private boolean mCompletedRecording;


    public AudioRecorder(@NonNull String fileName, MediaTimeUpdateListener mediaTimeUpdateListener, RecordTimeUpdateListener recordTimeUpdateListener) {
        mFileName = fileName;
        mMediaTimeUpdate = mediaTimeUpdateListener;
        mRecordTimeUpdate = recordTimeUpdateListener;
        mHandler = new Handler();
    }


    private void startRecording() {
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
                    private int time = 1;

                    @Override
                    public void run() {
                        if (!mCompletedRecording) {
                            mHandler.postDelayed(this, UPDATE_INTERVAL);
                            mRecordTimeUpdate.onRecordTimeUpdate(time++);
                        }

                    }
                }, UPDATE_INTERVAL);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        mCompletedRecording = true;
    }

    public void release() {
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
        if (start) {
            Log.d(LOG_TAG, "Started Recording");
            startRecording();
        } else {
            Log.d(LOG_TAG, "Stopped Recording");
            stopRecording();
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        mCompletedPlaying = false;
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            if (mMediaTimeUpdate != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mCompletedPlaying && mPlayer != null) {
                            mMediaTimeUpdate.onMediaTimeUpdate(mPlayer.getCurrentPosition(), mPlayer.getDuration());
                            mHandler.postDelayed(this, UPDATE_INTERVAL);
                        }
                    }
                }, UPDATE_INTERVAL);
            }
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mCompletedPlaying = true;
                    if (mMediaTimeUpdate != null) {
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

}
