package com.comp.iitb.vialogue.activity;

/**
 * Created by jeffrey on 27/2/17.
 */

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.QuestionAnswerDialog;
import com.comp.iitb.vialogue.dialogs.SingleOptionQuestion;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.QuestionAnswer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import tcking.github.com.giraffeplayer.PlayerDialogAdapter;
import tcking.github.com.giraffeplayer.PlayerModel;
import tcking.github.com.giraffeplayer.SimulationHandler;
import tcking.github.com.giraffeplayer.VPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;


public class VideoPlayer extends AppCompatActivity {

    public VPlayer mPlayer;
    private boolean isFirstTime;
    List<Integer> time = new ArrayList<>();

    public static String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        URL=getIntent().getStringExtra("URL");
        Log.d("-------URL",""+URL);

        mPlayer = new VPlayer(this);
        mPlayer.play(new PlayerModel(URL, null));
        mPlayer.setTitle(URL);
        mPlayer.addPlayerDialogAdapter(new PlayerDialogAdapter() {
            private SimulationHandler mSimulationHandler;

            @Override
            public void bind(SimulationHandler simulationHandler) {
                mSimulationHandler = simulationHandler;
                isFirstTime = true;
            }

            @Override
            public void timeChanged(int currentPosition, boolean isUser) {
                Log.d("---is time changing","good question"+ currentPosition);
                QuestionAnswer questionAnswer = new QuestionAnswer();
                questionAnswer.setOptions(new String[]{"1", "2", "3"});
                questionAnswer.setQuestion("Do you like it?");

                time.add(2000);
                time.add(4000);
                time.add(6000);
                time.add(8000);
                Log.d("time now",""+time.get(0));
                if(currentPosition> time.get(0)) {
                    popupQuestion(currentPosition, time.get(0), mSimulationHandler, questionAnswer);
                }
            }

            @Override
            public boolean seekToDifferentPosition(int currentPosition, int mediaIndex) {
                return false;
            }

            @Override
            public int moveTo(List<PlayerModel> playerModelList, int currentPosition, int mediaIndex) {
                Log.d(getClass().getName(), "timeChanged " + currentPosition + " is mediaIndex " + mediaIndex);
                return currentPosition;
            }
        });
        mPlayer.onComplete(new Runnable() {
            @Override
            public void run() {
                //callback when video is finish
                Toast.makeText(getApplicationContext(), R.string.videoCompleted, Toast.LENGTH_SHORT).show();
            }
        }).onInfo(new VPlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //do something when buffering start
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //do something when buffering end
                        break;
                }
            }
        }).onError(new VPlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                Toast.makeText(getApplicationContext(), R.string.videoError, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayer != null) {
            mPlayer.onResume();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mPlayer != null) {
            mPlayer.onConfigurationChanged(newConfig);
        }

    }

    @Override
    public void onBackPressed() {
        if (mPlayer != null && mPlayer.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    public void popupQuestion(int currentPosition,int intendedPosition, SimulationHandler mSimulationHandler, QuestionAnswer question)
    {
    if (currentPosition > intendedPosition  && currentPosition<intendedPosition+500) {
        mPlayer.pause();

        /*questionAnswer.setIsCompulsory(false);*/
        QuestionAnswerDialog adapter = new SingleOptionQuestion(mSimulationHandler.getActivity(), question);
        adapter.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mPlayer.start();
                Log.d("popped time",""+time.remove(0));

            }
        });
        adapter.show();
    }
    }

}
