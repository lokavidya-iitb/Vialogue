package com.comp.iitb.vialogue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.helpers.SharedPreferenceHelper;

public class SplashIt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferenceHelper help = new SharedPreferenceHelper(getApplicationContext());
        try {

                Intent intent = new Intent(this, SignIn.class);
                startActivity(intent);
                finish();

        } catch (NullPointerException e) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish();
            e.printStackTrace();
        } catch (Exception e) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish();
            e.printStackTrace();
        }

    }

}
