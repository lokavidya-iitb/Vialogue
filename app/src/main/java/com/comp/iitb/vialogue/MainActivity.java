package com.comp.iitb.vialogue;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.comp.iitb.vialogue.activity.AudioRecordActivity;
import com.comp.iitb.vialogue.adapters.FragmentPageAdapter;
import com.comp.iitb.vialogue.coordinators.OnFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.OnListFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.OnProgressUpdateListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.listeners.OnTabSelectedListener;
import com.comp.iitb.vialogue.models.DummyContent;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.comp.iitb.vialogue.activity.AudioRecordActivity.FOLDER_PATH;
import static com.comp.iitb.vialogue.activity.AudioRecordActivity.IMAGE_PATH;
import static com.comp.iitb.vialogue.activity.AudioRecordActivity.RECORD_NAME;
import static com.comp.iitb.vialogue.activity.AudioRecordActivity.RECORD_PATH;
@RuntimePermissions
public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnListFragmentInteractionListener,
        OnProgressUpdateListener {

    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private Storage mStorage;
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(),
                MainActivity.this));
        mStorage = new Storage(this);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        // Give the TabLayout the ViewPager
        mTabLayout.setupWithViewPager(mViewPager);

        setUpTabs();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AudioRecordActivity.class);
                Bundle bundle = new Bundle();


                bundle.putString(FOLDER_PATH, mStorage.getStorageDir("New Project", true).getAbsolutePath());
                bundle.putString(RECORD_PATH, SharedRuntimeContent.AUDIO_FOLDER_NAME);
                bundle.putString(RECORD_NAME, "hello.wav");
                bundle.putString(IMAGE_PATH, mStorage.getStorageDir("New Project", true).getAbsolutePath()+"/"+SharedRuntimeContent.IMAGE_FOLDER_NAME + "/" + SharedRuntimeContent.imagePathList.get(0));

                intent.putExtras(bundle);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setUpTabs() {
        String[] tabNames = getResources().getStringArray(R.array.tab_titles);
        OnTabSelectedListener tabSelectedListener = new OnTabSelectedListener(this,
                tabNames,
                ContextCompat.getColor(getApplicationContext(), R.color.tabSelected),
                ContextCompat.getColor(getApplicationContext(), R.color.tabUnselected));
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
            //tab.select();
        }
        mTabLayout.getTabAt(0).select();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onListFragmentInteraction(DummyContent.Slide item) {

    }

    @Override
    public void onProgressUpdate(int progress) {
        Log.d("Progress Main Activity", "___________ ___ _" + progress);
    }
}
