package com.comp.iitb.vialogue.activity;

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
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.dialogs.SingleOptionQuestion;
import com.comp.iitb.vialogue.models.QuestionAnswer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcking.github.com.giraffeplayer.PlayerDialogAdapter;
import tcking.github.com.giraffeplayer.PlayerModel;
import tcking.github.com.giraffeplayer.SimulationHandler;
import tcking.github.com.giraffeplayer.VPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class UploadVideoActivity extends AppCompatActivity {

    public VPlayer mPlayer;
    private boolean isFirstTime;
    private Spinner mCategories;
    private FloatingActionButton mUploadButton;
    public static String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        URL=getIntent().getStringExtra("URL");
        Log.d("-------URL",""+URL);

        mPlayer = new VPlayer(this);

       /* mUploadButton = (FloatingActionButton) findViewById(R.id.fab);
        mPlayer.play(new PlayerModel("http://"+URL, null));
        mPlayer.setTitle(URL);*/

        mUploadButton = (FloatingActionButton) findViewById(R.id.preview_fab);

        mPlayer.addPlayerDialogAdapter(new PlayerDialogAdapter() {
            private SimulationHandler mSimulationHandler;

            @Override
            public void bind(SimulationHandler simulationHandler) {
                mSimulationHandler = simulationHandler;
                isFirstTime = true;
            }

            @Override
            public void timeChanged(int currentPosition, boolean isUser) {
                if (currentPosition > 20000 && isFirstTime) {
                    mSimulationHandler.blockPlay();
                    QuestionAnswer questionAnswer = new QuestionAnswer();
                    questionAnswer.setOptions(new String[]{"O1", "O2", "O3"});
                    questionAnswer.setQuestion("Hello World");
                    questionAnswer.setIsCompulsory(false);
                    QuestionAnswerDialog adapter = new SingleOptionQuestion(mSimulationHandler.getActivity(), questionAnswer);
                    adapter.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mSimulationHandler.notifyProcessComplete();
                        }
                    });
                    adapter.show();
                    isFirstTime = false;
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
                Toast.makeText(getApplicationContext(), "video play completed", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "video play error", Toast.LENGTH_SHORT).show();
            }
        });
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_play_sample_1) {
                    //String url = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
                    String url = URL;
                    mPlayer.play(new PlayerModel("http://"+url, null));
                    mPlayer.setTitle(url);
                } else if (v.getId() == R.id.btn_play_sample_2) {
                    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                    List<PlayerModel> playerModels = new ArrayList<>();
                    playerModels.add(new PlayerModel(url, null));
                    playerModels.add(new PlayerModel(url, null));
                    playerModels.add(new PlayerModel("http://www.planwallpaper.com/static/images/1080p-wallpaper-14854-15513-hd-wallpapers.jpg",
                            "http://dl.smp3dl.com/files/convert/29363/128/08%20Zaalima%20-%20Abhijeet%20Sawant%20Version%20(SongsMp3.Com).mp3"));
                    mPlayer.play(playerModels);
                    mPlayer.setTitle(url);
                    mPlayer.setShowNavIcon(false);
                }
            }
        };
        findViewById(R.id.btn_play_sample_1).setOnClickListener(clickListener);
        findViewById(R.id.btn_play_sample_2).setOnClickListener(clickListener);

        mPlayer.play(SharedRuntimeContent.getPreviewList());
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

        mCategories = (Spinner) findViewById(R.id.category_choice);
        String[] plants = new String[]{
                // TODO retrieve from Parse Database
                "Select a category", // let this be
                "California sycamore",
                "Mountain mahogany",
                "Butterfly weed",
                "Carrot weed"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.category_option_view, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };/*
        spinnerArrayAdapter.setDropDownViewResource(R.layout.category_option_view);*/
        mCategories.setAdapter(spinnerArrayAdapter);

        mCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mUploadButton.hide();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mUploadButton.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mPlayer != null && mPlayer.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
