package com.comp.iitb.vialogue.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.comp.iitb.vialogue.R;

public class SignUpOrSignIn extends AppCompatActivity {

    private Button mSignIn;
    private Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_or_sign_in);

        // instantiate UI Elements
        mSignIn = (Button) findViewById(R.id.existing_user_button);
        mSignUp = (Button) findViewById(R.id.new_user_button);

        // Add Listeners
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOrSignIn.this, CreateNewAccountScreen1.class);
                startActivity(intent);
            }
        });
    }
}
