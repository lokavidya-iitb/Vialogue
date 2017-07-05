package com.comp.iitb.vialogue.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;

public class WhoAreYou extends AppCompatActivity {



    private int havePressedSkip =0;
    private SharedPreferences sharedPref;
    private String SKIPPED= "Skipped";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WhoAreYOu",havePressedSkip+"");
        if(sharedPref!=null) {
            sharedPref = WhoAreYou.this.getSharedPreferences(SKIPPED,  Context.MODE_PRIVATE);
            havePressedSkip = sharedPref.getInt(SKIPPED,0);
            Log.d("WhoAreYOu",havePressedSkip+"");

            if (havePressedSkip == 1) ;
            {
                Toast.makeText(WhoAreYou.this, "jhjhabcj", Toast.LENGTH_LONG).show();
                Intent i = new Intent(WhoAreYou.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
        setContentView(R.layout.activity_who_are_you);


        // initialise the UI element
        Button newUser=(Button)findViewById(R.id.new_user_button);
        Button existingUser=(Button)findViewById(R.id.existing_user_button);
        TextView skip=(TextView)findViewById(R.id.skip_button);


        //set on click listeners

        newUser.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the new user button is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CreateYourAccount.class);
                startActivity(i);
            }


                   });


        existingUser.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the existing user button is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CreateYourAccount.class);
                startActivity(i);
            }

        });


        skip.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the existing user button is clicked on.
            @Override
            public void onClick(View view) {
                havePressedSkip=1;
                sharedPref = WhoAreYou.this.getSharedPreferences(SKIPPED,  Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(SKIPPED, havePressedSkip);
                Log.d("WhoAreYOu",havePressedSkip+"");
                editor.commit();
                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

        });


    }



}