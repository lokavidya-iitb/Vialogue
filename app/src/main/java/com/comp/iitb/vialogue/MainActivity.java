package com.comp.iitb.vialogue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.activity.AudioRecordActivity;
import com.comp.iitb.vialogue.activity.SignIn;
import com.comp.iitb.vialogue.activity.UploadVideoActivity;
import com.comp.iitb.vialogue.adapters.FragmentPageAdapter;
import com.comp.iitb.vialogue.coordinators.OnFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.OnListFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;
import com.comp.iitb.vialogue.coordinators.OnSignedOut;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.fragments.QuestionAnswerDialog;
import com.comp.iitb.vialogue.helpers.SharedPreferenceHelper;
import com.comp.iitb.vialogue.helpers.TabSelectedHelper;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.listeners.OnTabSelectedListener;
import com.comp.iitb.vialogue.listeners.QuestionDoneListener;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.ParseObjectsCollection;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.comp.iitb.vialogue.activity.AudioRecordActivity.SLIDE_NO;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnListFragmentInteractionListener,
        OnProgressUpdateListener, TabSelectedHelper, GoogleApiClient.OnConnectionFailedListener {

    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    public ViewPager mViewPager;
    private Storage mStorage;
    private Menu mMenu;
    private FloatingActionButton mPreviewFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Storage.setupLokavidyaLegacy();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(),
                MainActivity.this));

        mViewPager.setOffscreenPageLimit(1);


        //mViewPager.setOffscreenPageLimit(0);

        mStorage = new Storage(getBaseContext());
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        // Give the TabLayout the ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        SharedRuntimeContent.mainActivity = this;
        mPreviewFab = (FloatingActionButton) findViewById(R.id.preview_fab);
        mPreviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem()) {
                    case 1:
                        Log.d("---CreateWorking?","Yeah");
                        Intent intent = new Intent(getBaseContext(), UploadVideoActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        SharedRuntimeContent.createEmptyProject(MainActivity.this);
                        mViewPager.setCurrentItem(1, true);
                        break;

                }
                /*Intent intent = new Intent(getApplicationContext(), AudioRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(FOLDER_PATH, mStorage.getStorageDir("New Project", true).getAbsolutePath());
                bundle.putString(SLIDE_NO, SharedRuntimeContent.AUDIO_FOLDER_NAME);
                bundle.putString(RECORD_NAME, "hello.wav");
                bundle.putString(IMAGE_PATH, SharedRuntimeContent.projectFolder.getAbsolutePath() + "/" + SharedRuntimeContent.IMAGE_FOLDER_NAME + "/" + SharedRuntimeContent.imagePathList.get(0));

                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });

        setUpTabs();
        SharedRuntimeContent.previewFab = mPreviewFab;
        SharedRuntimeContent.calculatePreviewFabVisibility();

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setUpTabs() {
        String[] tabNames = getResources().getStringArray(R.array.tab_titles);
        OnTabSelectedListener tabSelectedListener = new OnTabSelectedListener(this,
                tabNames,
                ContextCompat.getColor(getApplicationContext(), R.color.tabSelected),
                ContextCompat.getColor(getApplicationContext(), R.color.tabUnselected));
        tabSelectedListener.setTabSelectedHelper(this);
        mTabLayout.addOnTabSelectedListener(tabSelectedListener);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            switch (i) {
                case 0:
                    tab.setIcon(R.drawable.home);
                    break;
                case 1:
                    tab.setIcon(R.drawable.create_videos);
                    break;
                case 2:
                    tab.setIcon(R.drawable.view_videos);
                    break;
                case 3:
                    tab.setIcon(R.drawable.profile);
                    break;
            }
            /*
            *//*RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);

            ImageView tabTextView = (ImageView) relativeLayout.findViewById(R.id.tab_image);
            tabTextView.setText(tab.getText());*//*
            tab.setCustomView(relativeLayout);*/
            tab.select();
        }
        mTabLayout.getTabAt(0).select();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final MenuItem item_ = item;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(ParseUser.getCurrentUser() != null) {
                // already Signed in, Sign out
                SignIn.signOut(
                        MainActivity.this,
                        new OnSignedOut() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null) {
                                    item_.setTitle("Sign In");
                                }
                        }
                });
            } else {
                // already Signed out, Sign in
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }

           /* Auth.GoogleSignInApi.signOut(SignIn.mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            SharedPreferenceHelper help = new SharedPreferenceHelper(getApplicationContext());
                            try {
                                help.saveToSharedPref(Master.personName,"");
                                help.saveToSharedPref(Master.email,"");
                                help.saveToSharedPref(Master.personPhotoUrl,"");
                                help.saveToSharedPref(Master.signedOrNot,true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MainActivity.this,
                                    "Signed Out", Toast.LENGTH_SHORT).show();
                        }
                    });*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Main Activity", "resultCode " + resultCode + " request code " + requestCode);

        if(requestCode == SharedRuntimeContent.GET_QUESTION) {
            Bundle extras = data.getExtras();
            System.out.println("MainActivity : slideNumber : " + extras.getInt(QuestionDoneListener.SLIDE_NUMBER_FIELD));
            Question question = new Question(
                    extras.getString(Question.Fields.QUESTION_STRING_FIELD),
                    extras.getString(Question.Fields.QUESTION_TYPE_FIELD),
                    extras.getStringArrayList(Question.Fields.OPTIONS_FIELD),
                    extras.getIntegerArrayList(Question.Fields.CORRECT_OPTIONS_FIELD),
                    extras.getString(Question.Fields.SOLUTION_FIELD),
                    extras.getStringArrayList(Question.Fields.HINTS_FIELD),
                    extras.getBoolean(Question.Fields.IS_COMPULSORY_FIELD, true)
            );
            try {
                Slide slide = new Slide();
                slide.addResource(question, Slide.ResourceType.QUESTION);
                slide.setThumbnail(BitmapFactory.decodeResource(getResources(), R.drawable.ic_question));
                SharedRuntimeContent.changeSlideAtPosition(
                        extras.getInt(QuestionDoneListener.SLIDE_NUMBER_FIELD),
                        slide
                );
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), R.string.wrongBuddy, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onFragmentInteraction(int page) {

    }

    @Override
    public void onListFragmentInteraction(Slide item) {
        Log.d(getClass().getName(), item.getSlideType().toString());

        if (item.getSlideType() == Slide.SlideType.IMAGE) {
            System.out.println("showing image");
            // CLICKED AN IMAGE SLIDE
            Intent intent = new Intent(getApplicationContext(), AudioRecordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(AudioRecordActivity.SLIDE_NO, SharedRuntimeContent.getSlidePosition(item));
            intent.putExtras(bundle);
            startActivity(intent);

        } else if(item.getSlideType() == Slide.SlideType.VIDEO){
            System.out.println("playing video");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile((item.getResource().getResourceFile())), "video/*");
            startActivity(intent);

        } else if(item.getSlideType() == Slide.SlideType.QUESTION) {
            // TODO display question
            System.out.println("MainActivity : slideNumber : " + SharedRuntimeContent.getSlidePosition(item));
            QuestionAnswerDialog qaDialog = new QuestionAnswerDialog(MainActivity.this, new QuestionDoneListener(MainActivity.this, MainActivity.this), (Question) item.getResource(), SharedRuntimeContent.getSlidePosition(item));
            qaDialog.show();
        }

        onContextDeleteMenuNotRequired();
    }

    @Override
    public void onProgressUpdate(int progress) {
        Log.d("Progress Main Activity", "___________ ___ _" + progress);
    }


    public void onContextDeleteMenuRequired(int position) {
        mMenu.findItem(R.id.delete_option).setVisible(true);
    }

    public void onContextDeleteMenuNotRequired() {
        //mMenu.findItem(R.id.delete_option).setVisible(false);
    }

    @Override
    public void onTabSelected(int tabNumber) {
        switch (tabNumber) {
            case 0:
                mPreviewFab.hide();
                break;
            case 1:
                SharedRuntimeContent.calculatePreviewFabVisibility();
                break;
            case 2:
                mPreviewFab.hide();
                break;
            case 3:
                mPreviewFab.show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedRuntimeContent.pinProjectInBackground(MainActivity.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
