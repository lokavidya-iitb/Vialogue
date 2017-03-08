package com.comp.iitb.vialogue.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.QuestionAnswerDialog;
import com.comp.iitb.vialogue.coordinators.OnProjectSaved;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.dialogs.SingleOptionQuestion;
import com.comp.iitb.vialogue.library.SaveParseObjectAsync;
import com.comp.iitb.vialogue.models.QuestionAnswer;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private EditText name, description, language, tags;
    private final int MAX_WORD_LIMIT = 50;
    private List<QuestionAnswer> questionLists= new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        URL=getIntent().getStringExtra("URL");
        Log.d("-------URL",""+URL);

        mPlayer = new VPlayer(this);
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setOptions(new String[]{"O1", "O2", "O3"});
        questionAnswer.setQuestion("Hello World");
        questionAnswer.setIsCompulsory(false);
        questionLists.add(questionAnswer);

       /* mUploadButton = (FloatingActionButton) findViewById(R.id.fab);
        mPlayer.play(new PlayerModel("http://"+URL, null));
        mPlayer.setTitle(URL);*/

        mUploadButton = (FloatingActionButton) findViewById(R.id.preview_fab);
        name= (EditText) findViewById(R.id.video_name);
        description= (EditText) findViewById(R.id.video_description);
        language= (EditText) findViewById(R.id.video_language);
        tags= (EditText) findViewById(R.id.video_tags);
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ParseUser.getCurrentUser() == null) {
                     // User not signed in
                    Intent intent = new Intent(UploadVideoActivity.this, SignIn.class);
                    startActivity(intent);
                } else {
                    // User signed in, save project
                    SharedRuntimeContent.project.put("user", ParseUser.getCurrentUser());
                    new SaveParseObjectAsync(
                            UploadVideoActivity.this,
                            new ProgressDialog(UploadVideoActivity.this).show(UploadVideoActivity.this, "Saving Project", "Please wait...", true),
                            new OnProjectSaved() {
                                @Override
                                public void done(boolean isSaved) {
                                    if(isSaved) {
                                        Toast.makeText(UploadVideoActivity.this, "Project saved successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UploadVideoActivity.this, "Could not upload project. Please check your network connection.", Toast.LENGTH_LONG).show();
                                    }
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            },
                            SharedRuntimeContent.project
                    ).execute();
                }


//                Intent intent = new Intent(getApplicationContext(), SignIn.class);
//                startActivity(intent);
            }
        });


        tags.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Nothing
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] words = s.toString().split(" "); // Get all words
                if (words.length > MAX_WORD_LIMIT) {
                    tags.setText("");
                }
            }
        });

        mPlayer.addPlayerDialogAdapter(new PlayerDialogAdapter() {
            private SimulationHandler mSimulationHandler;

            @Override
            public void bind(SimulationHandler simulationHandler) {
                mSimulationHandler = simulationHandler;
                isFirstTime = true;
            }

            @Override
            public void timeChanged(int currentPosition, boolean isUser) {
               /*popupQuestion(currentPosition,1000 ,isUser, mSimulationHandler);*//*
                popupQuestion(currentPosition,2000 ,isUser, mSimulationHandler);*/
                new DialogOpener().execute();
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
        mPlayer.play(SharedRuntimeContent.getPreviewList());
        /*if(mPlayer.isPlaying())
        new DialogOpener().execute();*/
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
                "Agriculture",
                "Something",
                "Nothing",
                "Manything"
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

    public void popupQuestion(int currentPosition, int popUpAt, boolean isUser, final SimulationHandler mSimulationHandler)
    {

        if ((currentPosition > popUpAt && currentPosition < popUpAt+500 || currentPosition > 2000 && currentPosition < 2000+500 )) {
            mSimulationHandler.blockPlay();

            QuestionAnswerDialog adapter = new SingleOptionQuestion(mSimulationHandler.getActivity(), questionLists.get(0));
            adapter.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mSimulationHandler.notifyProcessComplete();
                }
            });
            adapter.show();
        }

    }


    private class DialogOpener extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            while (mPlayer.isPlaying()) {
                if(mPlayer.getDuration()>2000 && mPlayer.getDuration()<2500)
                Log.d("---getting time",""+mPlayer.getCurrentPosition());
                    /*final Dialog dialog = new Dialog(getBaseContext());
                    dialog.setContentView(R.layout.question_answer_layout);
                    dialog.setTitle("Question");
                    dialog.show();*/
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
