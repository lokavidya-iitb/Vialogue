package com.comp.iitb.vialogue.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
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

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.QuestionAnswerDialog;
import com.comp.iitb.vialogue.coordinators.OnDone;
import com.comp.iitb.vialogue.coordinators.OnProjectSaved;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.dialogs.SingleOptionQuestion;
import com.comp.iitb.vialogue.library.SaveParseObjectAsync;
import com.comp.iitb.vialogue.models.ParseObjects.models.Category;
import com.comp.iitb.vialogue.models.ParseObjects.models.CategoryType;
import com.comp.iitb.vialogue.models.ParseObjects.models.Language;
import com.comp.iitb.vialogue.models.QuestionAnswer;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
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
    List<Integer> time = new ArrayList<>();
    private boolean isFirstTime;
    private int goodToGo = 0;
    private Spinner mCategories;
    private FloatingActionButton mUploadButton;
    public static String URL;
    private EditText name, description, language, tags;
    private final int MAX_WORD_LIMIT = 50;
    private List<QuestionAnswer> questionLists = new ArrayList();
    List<String> tagsToUpload = new ArrayList<>();
    List<String> categories = new ArrayList<>();
    ArrayList<Category> categoryObjects = new ArrayList<>();
    List<ParseObject> receiveEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("---size of questions", "" + SharedRuntimeContent.questionsList.size());

        // Initialize variables
        mPlayer = new VPlayer(this);
        questionLists = SharedRuntimeContent.questionsList;
        URL = getIntent().getStringExtra("URL");
       /* mUploadButton = (FloatingActionButton) findViewById(R.id.fab);
        mPlayer.play(new PlayerModel("htt p://"+URL, null));
        mPlayer.setTitle(URL);*/

        // initialize UI Components
        mUploadButton = (FloatingActionButton) findViewById(R.id.preview_fab);
        name = (EditText) findViewById(R.id.video_name);
        mCategories = (Spinner) findViewById(R.id.category_choice);
        description = (EditText) findViewById(R.id.video_description);
        language = (EditText) findViewById(R.id.video_language);
        tags = (EditText) findViewById(R.id.video_tags);


        // Initialize UI State
        name.setText(SharedRuntimeContent.getProjectName());

        // set filters
        name.setFilters(new InputFilter[]{SharedRuntimeContent.filter});
        description.setFilters(new InputFilter[]{SharedRuntimeContent.filter});
        language.setFilters(new InputFilter[]{SharedRuntimeContent.filter});
        tags.setFilters(new InputFilter[]{SharedRuntimeContent.filter});

        // set listeners
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().length() == 0) {
                    name.setError("Required");
                } else if (description.getText().toString().trim().length() == 0) {
                    description.setError("Required");

                } else if (language.getText().toString().trim().length() == 0) {
                    language.setError("Required");

                } else if (tags.getText().toString().trim().length() == 0) {
                    tags.setError("Required");
                } else if(categories == null || categories.size() == 0) {
                    final ProgressDialog dialog = ProgressDialog.show(UploadVideoActivity.this, "Loading Categories", "Please Wait...");
                    loadCategoriesInBackground(new OnDone() {
                        @Override
                        public void done(boolean isDone) {
                            dialog.dismiss();
                            if(isDone) {
                                Toast.makeText(UploadVideoActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UploadVideoActivity.this, "Could not load categories. Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if(mCategories.getSelectedItem().toString().equals("Select a category")) {
                    Toast.makeText(UploadVideoActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
                } else {
                    uploadProject();
                }

                /* if(mCategories.getSelectedItem().toString().equals("Select a category"))
                {
                    mCategories.set
                }*/
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
                    return;
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
                Log.d("---is time changing", "good question" + currentPosition);
                QuestionAnswer questionAnswer = new QuestionAnswer();
                try {
                    if (questionLists.size() != 0) {
                        questionAnswer = questionLists.get(0);

                        Log.d("time now", "" + questionLists.get(0).getTime());
                        if (currentPosition > questionLists.get(0).getTime()) {
                            popupQuestion(currentPosition, questionLists.get(0).getTime(), mSimulationHandler, questionAnswer);
                        }
                    }
                } catch (NullPointerException e) {
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
        new playHandler().execute();

        /*if(mPlayer.isPlaying())
        new DialogOpener().execute();*/

        // Try to load categories
        loadCategoriesInBackground(new OnDone() {
            @Override
            public void done(boolean isDone) {
                // do nothing
            }
        });
    }

    public void uploadProject() {
        if (ParseUser.getCurrentUser() == null) {
            // User not signed in
            Toast.makeText(UploadVideoActivity.this, R.string.signIn, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UploadVideoActivity.this, SignIn.class);
            startActivity(intent);
        } else {
            // User signed in, save project
            SharedRuntimeContent.getProject().put("user", ParseUser.getCurrentUser());
            SharedRuntimeContent.getProject().setName(name.getText().toString());
            SharedRuntimeContent.getProject().setDescription(description.getText().toString());
            Language lang = new Language(name.getText().toString());
            SharedRuntimeContent.getProject().setLanguage(lang);
            SharedRuntimeContent.getProject().setCategory(categoryObjects.get(mCategories.getSelectedItemPosition()-1));
            tagsToUpload = Arrays.asList(tags.getText().toString().split(" "));

//            final ProgressDialog progressDialog = ProgressDialog.show(UploadVideoActivity.this, "Uploading Project", "Please Wait...");
//            SharedRuntimeContent.getProject().saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    progressDialog.dismiss();
//                }
//            });
            new SaveParseObjectAsync(
                    UploadVideoActivity.this,
                    ProgressDialog.show(UploadVideoActivity.this,"Uploading Project", "Please Wait..."),
                    new OnProjectSaved() {
                        @Override
                        public void done(boolean isSaved) {
                            if (isSaved) {
                                Toast.makeText(UploadVideoActivity.this, R.string.projectSaved, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UploadVideoActivity.this, R.string.couldntUpload, Toast.LENGTH_LONG).show();
                            }
                   /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);*/
                        }
                    },
                    SharedRuntimeContent.getProject()
            ).execute();
        }
    }


    private class playHandler extends AsyncTask<String, String, String> {

        private String resp = "";
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            mPlayer.play(SharedRuntimeContent.getPreviewList());
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(UploadVideoActivity.this,
                    "ProgressDialog", "Loading the preview");
            progressDialog.setCancelable(false);
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.onPause();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) UploadVideoActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void loadCategoriesInBackground(final OnDone onDone) {

        (new AsyncTask<Void, Void, Void>() {

            @Override
            public void onPreExecute() {
                categories = new ArrayList<String>();
            }

            @Override
            public Void doInBackground(Void... params) {

                if(!isNetworkConnected()) {
                    categories = null;
                    return null;
                }

                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
                    query.orderByDescending("createdAt");
                    List<ParseObject> results = query.find();
                    categories.add("Select a category");
                    for(ParseObject p : results) {
                        categories.add(((Category) p).getName());
                        categoryObjects.add((Category) p);
                    }
                } catch (ParseException e) {
                    categories = null;
                } return null;
            }

            @Override
            public void onPostExecute(Void result) {
                if(categories == null) {
                    onDone.done(false);
                    return;
                }

                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(UploadVideoActivity.this, R.layout.category_option_view, categories) {
                    @Override
                    public boolean isEnabled(int position) {
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
                };
                /*
                 spinnerArrayAdapter.setDropDownViewResource(R.layout.category_option_view);
                 */
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
                                    (getApplicationContext(), getResources().getString(R.string.selected) + selectedItemText, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                onDone.done(true);
            }
        }).execute();

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

    public void popupQuestion(int currentPosition, long intendedPosition, SimulationHandler mSimulationHandler, QuestionAnswer question) {
        if (currentPosition > intendedPosition && currentPosition < intendedPosition + 500) {
            mPlayer.pause();

        /*questionAnswer.setIsCompulsory(false);*/
            QuestionAnswerDialog adapter = new SingleOptionQuestion(mSimulationHandler.getActivity(), question);
            adapter.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mPlayer.start();
                    Log.d("popped time", "" + questionLists.remove(0));

                }
            });
            adapter.show();
        }
    }

}
