package com.comp.iitb.vialogue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.helpers.SharedPreferenceHelper;
import com.comp.iitb.vialogue.helpers.TabSelectedHelper;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.listeners.OnTabSelectedListener;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;


import java.io.File;

import static android.content.ContentValues.TAG;
import static com.comp.iitb.vialogue.activity.AudioRecordActivity.SLIDE_NO;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnListFragmentInteractionListener,
        OnProgressUpdateListener, TabSelectedHelper {

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

        mViewPager.setOffscreenPageLimit(4);

        //mViewPager.setOffscreenPageLimit(0);

        mStorage = new Storage(this);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        // Give the TabLayout the ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        SharedRuntimeContent.mainActivity = this;
        mPreviewFab = (FloatingActionButton) findViewById(R.id.preview_fab);
        mPreviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem())
                {
                    case 1:
                        Log.d("---CreateWorking?","Yeah");
                        Intent intent = new Intent(getBaseContext(), UploadVideoActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        Log.d("---UserWorking?","Yeah");
                        Master.projectName="New Project";
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");

            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }

        SharedRuntimeContent.previewFab = mPreviewFab;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferenceHelper help = new SharedPreferenceHelper(getApplicationContext());
            try {
                if(help.loadBooleanFromSharedPref(Master.signedOrNot)) {
                    SignIn.signOut();
                    help.saveToSharedPref(Master.personName, "");
                    help.saveToSharedPref(Master.email, "");
                    help.saveToSharedPref(Master.personPhotoUrl, "");
                    help.saveToSharedPref(Master.signedOrNot, false);
                    item.setTitle("Sign In");
                    Toast.makeText(MainActivity.this,
                            "Signed Out", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(intent);

                }
            } catch (Exception e) {
                e.printStackTrace();
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
    }

    @Override
    public void onFragmentInteraction(int page) {

    }

    @Override
    public void onListFragmentInteraction(Slide item) {
        Log.d(getClass().getName(), item.getSlideType().toString());
        // TODO check if any problems here
//        if (item.getSlideType() == DummyContent.SlideType.IMAGE || item.slideType == DummyContent.SlideType.IMAGE_AUDIO) {
        if (item.getSlideType() == Slide.SlideType.IMAGE) {
            System.out.println("showing image");
            // CLICKED AN IMAGE SLIDE
            Intent intent = new Intent(getApplicationContext(), AudioRecordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(AudioRecordActivity.SLIDE_NO, SharedRuntimeContent.getSlidePosition(item));
            /*bundle.putString(FOLDER_PATH, SharedRuntimeContent.projectFolder.getAbsolutePath());
            bundle.putString(SLIDE_NO, SharedRuntimeContent.AUDIO_FOLDER_NAME);
            bundle.putString(RECORD_NAME, new File(item.path).getName() + ".wav");
            bundle.putString(IMAGE_PATH, item.path);
*/
            intent.putExtras(bundle);
            startActivity(intent);
        } else if(item.getSlideType() == Slide.SlideType.VIDEO){
            System.out.println("playing video");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile((item.getResource().getResourceFile())), "video/*");
            startActivity(intent);
        }

        // TODO add question
        onContextDeleteMenuNotRequired();
    }

    @Override
    public void onProgressUpdate(int progress) {
        Log.d("Progress Main Activity", "___________ ___ _" + progress);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted");

                } else {
                    Log.v(TAG, "Permission is revoked");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }
            }
        }
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
                mPreviewFab.show();
                break;
            case 2:
                mPreviewFab.hide();
                break;
            case 3:
                mPreviewFab.show();

        }
    }
}
