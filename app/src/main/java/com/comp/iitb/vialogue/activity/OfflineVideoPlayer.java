package com.comp.iitb.vialogue.activity;

/**
 * Created by jeffrey on 3/4/17.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.QuestionAnswerDialog;
import com.comp.iitb.vialogue.dialogs.SingleOptionQuestion;
import com.comp.iitb.vialogue.models.QuestionAnswer;
import com.comp.iitb.vialogue.utils.ReadJSONFile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer.PlayerDialogAdapter;
import tcking.github.com.giraffeplayer.PlayerModel;
import tcking.github.com.giraffeplayer.SimulationHandler;
import tcking.github.com.giraffeplayer.VPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.Download;

public class OfflineVideoPlayer extends AppCompatActivity {

    public VPlayer mPlayer;
    private boolean isFirstTime;
    private List<QuestionAnswer> questionLists= new ArrayList();
    private List<ParseObject> recieveEm = new ArrayList<>();
    private Button button;
    private ProgressDialog progressDialog;

    public static String URL;
    public static String id;
    public static String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        URL=getIntent().getStringExtra("url");
        name=getIntent().getStringExtra("name");
                    mPlayer = new VPlayer(OfflineVideoPlayer.this);
                    mPlayer.play(new PlayerModel(URL, null));
                    mPlayer.setTitle(URL);
                    String videoDirectory = URL.substring(0,URL.lastIndexOf("/"));
                    questionLists = ReadJSONFile.getQuestionsFromJSON(Master.getSavedVideosPath()+"/"+name+"/questions.json");
                    Log.d("-----json",""+ questionLists.toString());
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
                            try{
                                if(questionLists.size()!=0)
                                {
                                    questionAnswer = questionLists.get(0);

                                    Log.d("time now",""+questionLists.get(0).getTime());
                                    if(currentPosition> questionLists.get(0).getTime()) {
                                        popupQuestion(currentPosition, (int)questionLists.get(0).getTime(), mSimulationHandler, questionAnswer);
                                    }
                                }
                            }
                            catch(NullPointerException e)
                            {
                                e.printStackTrace();
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


        button = (Button) findViewById(R.id.button);

        // Capture button clicks
        button.setVisibility(View.GONE);
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
                    Log.d("popped time",""+questionLists.remove(0));

                }
            });
            adapter.show();
        }
    }

}
