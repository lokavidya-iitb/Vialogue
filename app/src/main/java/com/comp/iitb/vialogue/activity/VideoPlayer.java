package com.comp.iitb.vialogue.activity;

/**
 * Created by jeffrey on 27/2/17.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.QuestionAnswerDialog;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.dialogs.SingleOptionQuestion;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.QuestionAnswer;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import tcking.github.com.giraffeplayer.PlayerDialogAdapter;
import tcking.github.com.giraffeplayer.PlayerModel;
import tcking.github.com.giraffeplayer.SimulationHandler;
import tcking.github.com.giraffeplayer.VPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.comp.iitb.vialogue.coordinators.SharedRuntimeContent.Download;


public class VideoPlayer extends AppCompatActivity {

    public VPlayer mPlayer;
    private boolean isFirstTime;
    private List<QuestionAnswer> questionLists= new ArrayList();
    private List<ParseObject> recieveEm = new ArrayList<>();
    private ParseObject project;
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
        id = getIntent().getStringExtra("id");
        Log.d("-------id",""+id);

        ParseQuery<ParseObject> mainQuery = ParseQuery.getQuery("Project");
        mainQuery.whereEqualTo("objectId",id);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> comments, ParseException e) {
                if (e == null) {
                    try {
                        project = mainQuery.getFirst();
                        ParseFile video = (ParseFile)project.get("project_video");
                        name = (String)project.get("name");
                        URL= video.getUrl();
                        Log.d("URL from video",""+URL);
                        /*mPlayer.play(URL);*/
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });



        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Videos");
        innerQuery.whereEqualTo("objectId",id);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");

        query.whereMatchesQuery("video",innerQuery);

        /*ParseObject queryingObj = ParseObject.createWithoutData("Videos",id);
        query.whereEqualTo("video", queryingObj);*/
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> comments, ParseException e) {
                if (e == null) {
                    mPlayer = new VPlayer(VideoPlayer.this);
                    try {
                        recieveEm = query.find();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    for (ParseObject iterator : recieveEm) {
                        QuestionAnswer questionAnswer = new QuestionAnswer();
                        String question_string = ((String) iterator.get("question_string"));
                        List<String> optionList = (List<String>) iterator.get("options");
                        String options[] = new String[optionList.size()];
                        options = optionList.toArray(options);
                        int time = ((int) iterator.get("time"));
                        questionAnswer.setOptions(options);
                        questionAnswer.setTime(time);
                        questionAnswer.setQuestion(question_string);
                        questionLists.add(questionAnswer);
                    }
                    Log.d("-------questionList",""+questionLists);

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
                }else {
                    // Something went wrong...
                }
            }
        });

        button = (Button) findViewById(R.id.button);

        // Capture button clicks
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                new downloader().execute();
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


    private class downloader extends AsyncTask<String, String, String> {

        private String resp ="";
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            Storage.createThisDirectory(Master.getSavedVideosPath()+"/"+name);
            Download(URL, Master.getSavedVideosPath(), name, name+".mp4", getBaseContext());

            ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Video");
            innerQuery.whereEqualTo("objectId",id);
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");

            query.whereMatchesQuery("video",innerQuery);
            JSONArray completeFile = new JSONArray();
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> comments, ParseException e) {
                    if (e == null) {
                        mPlayer = new VPlayer(VideoPlayer.this);
                        try {
                            recieveEm = query.find();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        for (ParseObject iterator : recieveEm) {
                            String question_string = ((String) iterator.get("question_string"));
                            List<String> optionList = (List<String>) iterator.get("options");
                            String options[] = new String[optionList.size()];
                            String solution = (String) iterator.get("solution");
                            options = optionList.toArray(options);
                            int time = ((int) iterator.get("time"));
                            JSONObject singleQuestion = new JSONObject();
                            try {
                                singleQuestion.put("question_string",question_string);
                                singleQuestion.put("answer",solution);
                                singleQuestion.put("time",time);
                                singleQuestion.put("options",options);

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            completeFile.put(singleQuestion);
                        }
                        try {
                            Writer output = null;
                            File file = new File(Environment.getExternalStorageDirectory() +Master.getSavedVideosPath() +"/" + name + "/"+ "questions.json");
                            output = new BufferedWriter(new FileWriter(file));
                            Log.d("---complete",""+completeFile.toString());
                            output.write(completeFile.toString());
                            output.close();
                            Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();

                        } catch (Exception justInCase) {
                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }}});
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(VideoPlayer.this,
                    "ProgressDialog","Downloading..");
            progressDialog.setCancelable(false);
        }


        @Override
        protected void onProgressUpdate(String... text) {

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
