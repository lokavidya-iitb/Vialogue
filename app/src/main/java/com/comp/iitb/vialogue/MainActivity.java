package com.comp.iitb.vialogue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.LogOut;
import com.comp.iitb.vialogue.Network.LokavidyaSso.SharedPreferencesDetails;
import com.comp.iitb.vialogue.activity.AudioRecordActivity;
import com.comp.iitb.vialogue.activity.CreateYourAccount;
import com.comp.iitb.vialogue.activity.SignIn;
import com.comp.iitb.vialogue.activity.UploadVideoActivity;
import com.comp.iitb.vialogue.activity.WhoAreYou;
import com.comp.iitb.vialogue.adapters.FragmentPageAdapter;
import com.comp.iitb.vialogue.coordinators.OnDoneLogOut;
import com.comp.iitb.vialogue.coordinators.OnFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.OnListFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;
import com.comp.iitb.vialogue.coordinators.OnProjectSaved;
import com.comp.iitb.vialogue.coordinators.OnSignedOut;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.fragments.SingleChoiceQuestionDialog;
import com.comp.iitb.vialogue.helpers.TabSelectedHelper;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.listeners.OnTabSelectedListener;
import com.comp.iitb.vialogue.listeners.QuestionDoneListener;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;
import com.parse.ParseUser;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnListFragmentInteractionListener,
        OnProgressUpdateListener, TabSelectedHelper, GoogleApiClient.OnConnectionFailedListener {

    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private Storage mStorage;
    private Menu mMenu;
    private FloatingActionButton mPreviewFab;
    private ViewPager mViewPager;
    private Integer mStartFragmentPosition;
    private Context mContext;
    SharedPreferences mLokavidyaSsoSharedPreferences;
    public static boolean mIsLoggedIn = false;
    String mResponseString;

    public static final String startFragmentPositionKey = "start_fragment_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        mLokavidyaSsoSharedPreferences = mContext.getSharedPreferences(SharedPreferencesDetails.SHARED_PREFERENCES_NAME, 0);
        if(!mIsLoggedIn) {
            SharedPreferences.Editor editor = mLokavidyaSsoSharedPreferences.edit();
            editor.putString(SharedPreferencesDetails.SESSION_TOKEN_KEY, "");
            editor.putString(SharedPreferencesDetails.SESSION_UUID_KEY, "");
            editor.putBoolean(SharedPreferencesDetails.IS_LOGGED_IN_KEY, false);
            editor.apply();
        } else {

        }

        Bundle data = getIntent().getExtras();
        if(data != null) {
            Integer startFragmentPosition = data.getInt(startFragmentPositionKey);
            if(startFragmentPosition != null && startFragmentPosition < 3) {
                mStartFragmentPosition = startFragmentPosition;
            }
        } if(mStartFragmentPosition == null) {
            mStartFragmentPosition = 0;
        }

        System.out.println("mStartFragmentPosition : " + mStartFragmentPosition);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = ((ViewPager) findViewById(R.id.viewpager));
        mViewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(), MainActivity.this));
        //mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOffscreenPageLimit(2);

        mStorage = new Storage(getBaseContext());
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        // Give the TabLayout the ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        mPreviewFab = (FloatingActionButton) findViewById(R.id.preview_fab);
        mPreviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem()) {
                    case 1:
                        SharedRuntimeContent.questionsList= SharedRuntimeContent.getQuestions();
                        SharedRuntimeContent.blankImages= SharedRuntimeContent.getBlankSlides();
                        Intent intent = new Intent(getBaseContext(), UploadVideoActivity.class);
//                        intent.putStringArrayListExtra()
                        startActivity(intent);
                        break;
                    case 2:
                        SharedRuntimeContent.createEmptyProject(MainActivity.this);
                        SharedRuntimeContent.questionsList.clear();
                        mViewPager.setCurrentItem(FragmentPageAdapter.CREATE_PROJECT, true);
                        break;
                }
            }
        });
        setUpTabs();
        SharedRuntimeContent.previewFab = mPreviewFab;
        SharedRuntimeContent.previewFab.setVisibility(View.GONE);
        refreshSignInOutOptions();
        mViewPager.setCurrentItem(mStartFragmentPosition);
    }

    // Back Button logic
    private boolean shouldExitOnBack = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && mViewPager.getCurrentItem()!=1) {
            if(shouldExitOnBack) {
                finish();
                return true;
            } else {
                Toast.makeText(MainActivity.this, "Press back again to exit the application.", Toast.LENGTH_LONG).show();
                shouldExitOnBack = true;
                (new AsyncTask<Void, Void, Void>() {
                    @Override
                    public Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        shouldExitOnBack = false;
                        return null;
                    }
                }).execute();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshSignInOutOptions();
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
                case FragmentPageAdapter.HOME:
                    tab.setIcon(R.drawable.home);
                    break;
                case FragmentPageAdapter.CREATE_PROJECT:
                    tab.setIcon(R.drawable.create_videos);
                    break;
                /*case FragmentPageAdapter.VIEW_VIDEOS:
                    tab.setIcon(R.drawable.view_videos);
                    break;*/
                case FragmentPageAdapter.INCEPTIONMYPROJECTS:
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
        System.out.println("onCreateOptionsMenu : called");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        refreshSignInOutOptions();

        SharedRuntimeContent.saveMenuItem = mMenu.findItem(R.id.save_project);
        try {
            mMenu.findItem(R.id.save_project).setVisible(false);
        } catch (Exception e) {}
        SharedRuntimeContent.saveMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "The project will be saved in background. You may continue your work.", Toast.LENGTH_LONG).show();
                // save existing project
                SharedRuntimeContent.pinProjectInBackground(MainActivity.this, new OnProjectSaved() {
                    @Override
                    public void done(boolean isSaved) {
                        if(!SharedRuntimeContent.getProject().doesItExistInLocalDatastore()) {
                            SharedRuntimeContent.myProjectsAdapter.addProject(SharedRuntimeContent.getProject());
                            SharedRuntimeContent.getProject().existsInLocalDatastore();
                            Toast.makeText(MainActivity.this, "Project Saved Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedRuntimeContent.myProjectsAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Project Saved Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                return true;
            }
        });
        return true;
    }

    /*public void refreshSignInOutOptions() {
        try {
            if(ParseUser.getCurrentUser() == null) {
                // signed out
                mMenu.findItem(R.id.action_settings).setTitle(R.string.sign_in);
            } else {
                // signed in
                mMenu.findItem(R.id.action_settings).setTitle(R.string.sign_out);
            }
        } catch (Exception e) {}
    }*/

    public void refreshSignInOutOptions() {
        //mLokavidyaSsoSharedPreferences = mContext.getSharedPreferences(SharedPreferencesDetails.SHARED_PREFERENCES_NAME, 0);
        //mIsLoggedIn = mLokavidyaSsoSharedPreferences.getBoolean(SharedPreferencesDetails.IS_LOGGED_IN_KEY, true);
        System.out.println("aaaaaa "+mIsLoggedIn);
        System.out.println("bbbbbb "+mLokavidyaSsoSharedPreferences.getBoolean(SharedPreferencesDetails.IS_LOGGED_IN_KEY, false));
        try {
            if (!mLokavidyaSsoSharedPreferences.getBoolean(SharedPreferencesDetails.IS_LOGGED_IN_KEY, false)) {
                System.out.println("loggedin: "+mIsLoggedIn);
                mMenu.findItem(R.id.action_settings).setTitle(R.string.sign_in);
            } else {
                System.out.println("loggedin: "+mIsLoggedIn);
                mMenu.findItem(R.id.action_settings).setTitle(R.string.sign_out);
                SharedPreferences.Editor editor = mLokavidyaSsoSharedPreferences.edit();
                editor.putBoolean(SharedPreferencesDetails.IS_LOGGED_IN_KEY, false);
                editor.apply();
            }
        } catch (Exception e) {}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_settings) {
            System.out.println("loggedin: "+mIsLoggedIn);
            if(mIsLoggedIn) {
                //ParseUser.getCurrentUser() != null
                // already Signed in, Sign out
                System.out.println("logout");
                LogOut.logOutInBackground(mContext, new OnDoneLogOut() {
                    @Override
                    public void done(LogOut.LogOutResponse logOutResponse) {
                        switch (logOutResponse.getResponseType()) {
                            case LOGGED_OUT:
                                mResponseString = logOutResponse.getResponseString();
                                break;
                            default:
                                mResponseString = logOutResponse.getResponseString();
                        }
                        Toast.makeText(mContext, mResponseString, Toast.LENGTH_SHORT).show();
                        mIsLoggedIn = false;
                        refreshSignInOutOptions();
                    }
                });

                /*SignIn.signOut(
                        MainActivity.this,
                        new OnSignedOut() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null) {}
                                refreshSignInOutOptions();
                        }
                });*/
            } else {
                // already Signed out, Sign in
                System.out.println("open whoareyeou");
                Intent intent = new Intent(getApplicationContext(), WhoAreYou.class);
                intent.putExtra("context",1);
                startActivity(intent);
            }

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
            SingleChoiceQuestionDialog qaDialog = new SingleChoiceQuestionDialog(MainActivity.this, new QuestionDoneListener(MainActivity.this, MainActivity.this), (Question) item.getResource(), SharedRuntimeContent.getSlidePosition(item));
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

    public void onTabSelected(int tabNumber) {
        switch (tabNumber) {
            case FragmentPageAdapter.HOME:
                mPreviewFab.hide();
                try {
                    mMenu.findItem(R.id.save_project).setVisible(false);
                } catch (Exception e) {}
                break;
            /*case FragmentPageAdapter.VIEW_VIDEOS:
                mPreviewFab.hide();
                try {
                    mMenu.findItem(R.id.save_project).setVisible(false);
                } catch (Exception e) {}
                break;*/
            case FragmentPageAdapter.CREATE_PROJECT:
                SharedRuntimeContent.calculatePreviewFabVisibility();
                try {
                    SharedRuntimeContent.calculateSaveMenuItemVisibility();
                } catch (Exception e) {}
                break;
            case FragmentPageAdapter.INCEPTIONMYPROJECTS:
                mPreviewFab.setImageResource(R.drawable.plus_png);
                try {
                    mMenu.findItem(R.id.save_project).setVisible(false);
                } catch (Exception e) {}
                mPreviewFab.show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
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
