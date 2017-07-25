package com.comp.iitb.vialogue.library;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.ForgotPassword;
import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.SignUp;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnDoneForgotPassword;
import com.comp.iitb.vialogue.coordinators.OnDoneSignIn;
import com.comp.iitb.vialogue.coordinators.OnDoneCallingSsoApiResult;
import com.comp.iitb.vialogue.dialogs.VerifyOtpDialogue;

/**
 * Created by omkar on 22/7/17.
 */

public class SsoMethods {

    String mUniqueId = null;
    OnDoneCallingSsoApiResult mOnDoneCallingSsoApiResult;
    String mResponseString;
    Bundle info = new Bundle();
    boolean user_exists = false;
    boolean otp_sent = false;

    public SsoMethods(OnDoneCallingSsoApiResult onDoneCallingSsoApiResult){
        mOnDoneCallingSsoApiResult = onDoneCallingSsoApiResult;
    }

    public void signUp(Context context, String userName, SignUp.RegistrationType registrationType, String registrationData, String password, String uniqueId) {
        SignUp.signUpInBackground(
                context,
                userName,
                registrationType,
                registrationData,
                password,
                uniqueId,
                new OnDoneSignIn() {
                    @Override
                    public void done(SignUp.SignUpResponse signUpResponse) {
                        switch (signUpResponse.getResponseType()) {
                            case USER_SIGNED_UP:
                                mResponseString = signUpResponse.getResponseString();
                                mUniqueId = signUpResponse.getUniqueId();
                            case USER_ALREADY_EXISTS:
                                mResponseString = signUpResponse.getResponseString();
                                user_exists = true;
                            case COULD_NOT_SIGN_UP:
                                mResponseString = signUpResponse.getResponseString();
                            case NETWORK_ERROR:
                                mResponseString = signUpResponse.getResponseString();
                        }
                        Toast.makeText(context, mResponseString, Toast.LENGTH_SHORT).show();
                        System.out.println("signUpResponse:" + signUpResponse);
                        info.putString(context.getResources().getString(R.string.uniqueId), mUniqueId);
                        info.putString(context.getResources().getString(R.string.registrationType), context.getResources().getString(R.string.phoneNo));
                        info.putString("responseString", mResponseString);
                        info.putBoolean("user_exists", user_exists);
                        mOnDoneCallingSsoApiResult.onDone(info);
                    }
                });
    }

    public void forgotPassword(Context context, ForgotPassword.RegistrationType registrationType, String registrationData) {
        ForgotPassword.forgotPasswordInBackground(
                context,
                registrationType,
                registrationData,
                new OnDoneForgotPassword() {
                    @Override
                    public void done(ForgotPassword.ForgotPasswordResponse response) {
                        switch (response.getResponseType()) {
                            case OTP_SENT:
                                mResponseString = response.getResponseString();
                                otp_sent = true;
                                //new VerifyOtpDialogue(context, args, 1).show();
                            case USER_NOT_REGISTERED:
                                mResponseString = response.getResponseString();
                            case NETWORK_ERROR:
                                mResponseString = response.getResponseString();
                            case SOMETHING_WENT_WRONG:
                                mResponseString = response.getResponseString();
                        }
                        Toast.makeText(context, mResponseString, Toast.LENGTH_SHORT).show();
                        info.putBoolean("otp_sent", otp_sent);
                        mOnDoneCallingSsoApiResult.onDone(info);
                    }
                }
        );
    }

}
