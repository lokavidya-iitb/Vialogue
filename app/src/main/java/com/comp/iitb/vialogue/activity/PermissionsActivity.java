package com.comp.iitb.vialogue.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.comp.iitb.vialogue.R;

public class PermissionsActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_ACCESS_PERMISSION = 1234;
    Intent intent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                onAllPermissionsGranted();
            } else {
                // permission not granted
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_ACCESS_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case REQUEST_STORAGE_ACCESS_PERMISSION :
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    // Launch MainActivity
                    onAllPermissionsGranted();
                } else {
//                    Toast.makeText(PermissionsActivity.this, R.string.gimmeStorage, Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }

    public void onAllPermissionsGranted() {
            intent = new Intent(this, WhoAreYou.class);
        intent.putExtra("context",0);
        startActivity(intent);
        finish();
    }
}
