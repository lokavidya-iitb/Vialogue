package com.comp.iitb.vialogue.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comp.iitb.vialogue.MainActivity;
import com.comp.iitb.vialogue.R;


public class WhoAreYou extends AppCompatActivity {


    static final String KEY_IS_FIRST_TIME = "com.LvCreate.first_time";
    static final String KEY = "com.LvCreate";

    private final int splashNpermission = 0;
    private final int signInMenuOption = 1;

    TextView skip;
    Button existingUser;
    Button newUser;


    int WhereAreYouComingFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_are_you);

        // initialise the UI element
        newUser = (Button) findViewById(R.id.new_user_button);
        existingUser = (Button) findViewById(R.id.existing_user_button);
        skip = (TextView) findViewById(R.id.skip_button);

        //coming from where
        Intent intent = getIntent();
        WhereAreYouComingFrom = intent.getExtras().getInt("context");
        if (WhereAreYouComingFrom == signInMenuOption)
            skip.setVisibility(View.GONE);
        else if (WhereAreYouComingFrom == splashNpermission)
            if (!isFirstTime()) {
                System.out.println("inside is first time if");
                startActivity(new Intent(WhoAreYou.this, MainActivity.class));
                finish();
            }


        //set on click listeners

        newUser.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the new user button is clicked on.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CreateYourAccount.class);
                startActivity(i);
                finish();
            }


        });


        existingUser.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the existing user button is clicked on.
            @Override
            public void onClick(View view) {
//            Intent i = new Intent(view.getContext(), CreateYourAccount.class);      todo: intent to login page
//                startActivity(i);
                finish();
            }

        });


        skip.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the existing user button is clicked on.
            @Override
            public void onClick(View view) {
                getSharedPreferences(KEY, Context.MODE_PRIVATE).edit().putBoolean(KEY_IS_FIRST_TIME, false).commit();
                startActivity(new Intent(WhoAreYou.this, MainActivity.class));
                finish();
            }

        });
    }

    public boolean isFirstTime() {
        return getSharedPreferences(KEY, Context.MODE_PRIVATE).getBoolean(KEY_IS_FIRST_TIME, true);
    }


}
