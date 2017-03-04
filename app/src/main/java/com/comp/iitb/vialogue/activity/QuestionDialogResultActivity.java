package com.comp.iitb.vialogue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;

/**
 * Created by ironstein on 03/03/17.
 */

public class QuestionDialogResultActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
