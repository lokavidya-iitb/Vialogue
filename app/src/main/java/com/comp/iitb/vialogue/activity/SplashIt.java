package com.comp.iitb.vialogue.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashIt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // permission granted

                intent = new Intent(this, WhoAreYou.class);

            } else {
                // permission not granted
                // ask for permission
                intent = new Intent(this, PermissionsActivity.class);
            }
        } else {
                intent = new Intent(this, WhoAreYou.class);
        }
        intent.putExtra("context", 0);
        startActivity(intent);
        finish();

    }



}
