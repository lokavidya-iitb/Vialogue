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

//        SignUp.signUpInBackground(SplashIt.this, "ironstein", SignUp.RegistrationType.EMAIL_ID, "ironstein1994.tech@gmail.com", "helloWorld", new OnDoneSignIn() {
//            @Override
//            public void done(SignUp.SignUpResponse signUpResponse) {
//                System.out.println(signUpResponse.getResponseString());
//            }
//        });
//
//        LogIn.logInInBackground(SplashIt.this, LogIn.RegistrationType.EMAIL_ID, "ironstein1994.tech@gmail.com", "helloWorld", new OnDoneLogIn() {
//            @Override
//            public void done(LogIn.LogInResponse logInResponse) {
//                System.out.println(logInResponse.getResponseString());
//            }
//        });
//
//        LogOut.logOutInBackground(SplashIt.this, new OnDoneLogOut() {
//            @Override
//            public void done(LogOut.LogOutResponse logOutResponse) {
//                System.out.println(logOutResponse.getResponseString());
//            }
//        });

//        ForgotPassword.forgotPasswordInBackground(SplashIt.this, ForgotPassword.RegistrationType.PHONE_NUMBER, "+919920579150", new OnDoneForgotPassword() {
//            @Override
//            public void done(ForgotPassword.ForgotPasswordResponse response) {
//                System.out.println(response.getResponseString());
//            }
//        });

//        ResetPassword.resetPasswordInBackground(SplashIt.this, ResetPassword.RegistrationType.PHONE_NUMBER, "+919920579150", "009260", "newPassword", new OnDoneResetPassword() {
//            @Override
//            public void done(ResetPassword.ResetPasswordResponse resetPasswordResponse) {
//                System.out.println(resetPasswordResponse.getResponseString());
//            }
//        });


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

        startActivity(intent);
        finish();

    }

}
