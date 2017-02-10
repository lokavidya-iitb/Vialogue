package com.comp.iitb.vialogue.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer.PlayerDialogAdapter;
import tcking.github.com.giraffeplayer.PlayerModel;
import tcking.github.com.giraffeplayer.SimulationHandler;
import tcking.github.com.giraffeplayer.VPlayer;
import tcking.github.com.giraffeplayer.VPlayerActivity;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class UploadVideoActivity extends AppCompatActivity {

    public VPlayer mPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPlayer = new VPlayer(this);
        mPlayer.addPlayerDialogAdapter(new PlayerDialogAdapter() {
            private SimulationHandler mSimulationHandler;
            @Override
            public void bind(SimulationHandler simulationHandler) {
                Log.d(getClass().getName(),"bind");
                mSimulationHandler = simulationHandler;
            }

            @Override
            public void timeChanged(int currentPosition, boolean isUser) {
                // Log.d(getClass().getName(),"timeChanged "+currentPosition + " is User "+ isUser);

            }

            @Override
            public boolean seekToDifferentPosition(int currentPosition,int mediaIndex){
                return false;
            }

            @Override
            public int moveTo(List<PlayerModel> playerModelList, int currentPosition, int mediaIndex) {
                Log.d(getClass().getName(),"timeChanged "+currentPosition + " is mediaIndex "+ mediaIndex);
                return currentPosition;
            }
        });
        mPlayer.onComplete(new Runnable() {
            @Override
            public void run() {
                //callback when video is finish
                Toast.makeText(getApplicationContext(), "video play completed",Toast.LENGTH_SHORT).show();
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
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        //download speed
                        ((TextView) findViewById(R.id.tv_speed)).setText(Formatter.formatFileSize(getApplicationContext(),extra)+"/s");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        //do something when video rendering
                        findViewById(R.id.tv_speed).setVisibility(View.GONE);
                        break;
                }
            }
        }).onError(new VPlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                Toast.makeText(getApplicationContext(), "video play error",Toast.LENGTH_SHORT).show();
            }
        });
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_play) {
                    String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
                    mPlayer.play(url);
                    mPlayer.setTitle(url);
                } else if (v.getId() == R.id.btn_play_sample_1) {
                    //String url = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
                    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                    ((EditText) findViewById(R.id.et_url)).setText(url);
                    mPlayer.play(new PlayerModel(url,null));
                    mPlayer.setTitle(url);
                } else if (v.getId() == R.id.btn_play_sample_2) {
                    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                    ((EditText) findViewById(R.id.et_url)).setText(url);
                    List<PlayerModel> playerModels = new ArrayList<>();
                    playerModels.add(new PlayerModel(url,null));
                    playerModels.add(new PlayerModel(url,null));
                    playerModels.add(new PlayerModel("http://www.planwallpaper.com/static/images/1080p-wallpaper-14854-15513-hd-wallpapers.jpg",
                            "http://dl.smp3dl.com/files/convert/29363/128/08%20Zaalima%20-%20Abhijeet%20Sawant%20Version%20(SongsMp3.Com).mp3"));
                    mPlayer.play(playerModels);
                    mPlayer.setTitle(url);
                    mPlayer.setShowNavIcon(false);
                }else if (v.getId() == R.id.btn_play_sample_3) {
                    String url = "https://r13---sn-o097znes.googlevideo.com/videoplayback?mt=1455852432&mv=m&ms=au&source=youtube&key=yt6&requiressl=yes&mm=31&mn=sn-o097znes&initcwndbps=16485000&id=o-AEGdeTbgSTzVGqwV2s8MjH5mlDPz3APWVwGfftr9GDqy&upn=D3A5w5WYU1k&lmt=1410665930307178&ip=2600:3c01::f03c:91ff:fe70:35ff&sparams=dur,id,initcwndbps,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,nh,pl,ratebypass,requiressl,source,upn,expire&fexp=9416126,9420452,9422596,9423341,9423661,9423662,9424038,9424862,9425077,9425730,9426472,9426698,9427379,9428544,9428649,9429218,9429237,9429435,9429589&pl=32&dur=106.370&sver=3&expire=1455874197&nh=IgpwcjAxLnNqYzA3KgkxMjcuMC4wLjE&ratebypass=yes&mime=video/mp4&itag=18&signature=22C4633FCD1259D5F6CD1E0B54AB649982895534.378BAAC5AFAAEA737246C5CE5B92212E40B765BD&ipbits=0";
                    ((EditText) findViewById(R.id.et_url)).setText(url);
                    mPlayer.play(url);
                    mPlayer.setTitle(url);
                    mPlayer.setShowNavIcon(false);
                }else if (v.getId() == R.id.btn_open) {
                    String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
                    VPlayerActivity.configPlayer(UploadVideoActivity.this).setTitle(url).play(url);
//                    more configuration example:
//                    VPlayerActivity.configPlayer(MainActivity.this)
//                            .setScaleType(VPlayer.SCALETYPE_FITPARENT)
//                            .setDefaultRetryTime(5 * 1000)
//                            .setFullScreenOnly(false)
//                            .setTitle(url)
//                            .play(url);
                }else if (v.getId() == R.id.btn_start) {
                    mPlayer.start();
                }else if (v.getId() == R.id.btn_pause) {
                    mPlayer.pause();
                }else if (v.getId() == R.id.btn_toggle) {
                    mPlayer.toggleFullScreen();
                }else if (v.getId() == R.id.btn_forward) {
                    mPlayer.forward(0.2f);
                }else if (v.getId() == R.id.btn_back) {
                    mPlayer.forward(-0.2f);
                }else if (v.getId() == R.id.btn_toggle_ratio) {
                    mPlayer.toggleAspectRatio();
                }
            }
        };
        findViewById(R.id.btn_play).setOnClickListener(clickListener);
        findViewById(R.id.btn_play_sample_1).setOnClickListener(clickListener);
        findViewById(R.id.btn_play_sample_2).setOnClickListener(clickListener);
        findViewById(R.id.btn_play_sample_3).setOnClickListener(clickListener);
        findViewById(R.id.btn_pause).setOnClickListener(clickListener);
        findViewById(R.id.btn_start).setOnClickListener(clickListener);
        findViewById(R.id.btn_toggle).setOnClickListener(clickListener);
        findViewById(R.id.btn_open).setOnClickListener(clickListener);
        findViewById(R.id.btn_forward).setOnClickListener(clickListener);
        findViewById(R.id.btn_back).setOnClickListener(clickListener);
        findViewById(R.id.btn_toggle_ratio).setOnClickListener(clickListener);
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
}
