package com.comp.iitb.vialogue.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.CreateYourAccount2;
import com.comp.iitb.vialogue.activity.ResetPasswordActivity;
import com.comp.iitb.vialogue.coordinators.OnOtpReceived;
import com.comp.iitb.vialogue.coordinators.OnOtpSent;
import com.comp.iitb.vialogue.library.SendOtpAsync;
import com.comp.iitb.vialogue.listeners.SmsOtpListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;

public class VerifyOtp extends Dialog {
    Bundle info;

    //variables
    private String mPhoneNumber = null;
    private String mPersonName = null;
    private String mProfilePictureUrl = null;
    private String mEmail = null;
    private Integer mOtp;
    private static final int DELAY_BETWEEN_OTP_REQUESTS_MILLIS = 60000; // 1 minute


    //others
    String PHONE_NUMBER = "User_mobile_number";
    public static GoogleApiClient mGoogleApiClient;
    private static ProgressDialog mProgressDialog;
    private Context mContext;
    public static  CountDownTimer TimeoutTimer;
    private int resetActivityCode = 0;

    RegenerateOtp regenerateOtpOnTimeout;
    // UI
    Button verifyOtp;
    EditText OtpEditText;
    TextView mTextField;

    public VerifyOtp(Context context, Bundle info)//context of the activity from which dialog will open
    {
        super(context);
        mContext = context;
        this.info = info;
        System.out.println(info.getString(mContext.getResources().getString(R.string.registrationData)));
        verifyOtp(info.getString(mContext.getResources().getString(R.string.registrationData)));
    }

    public VerifyOtp(Context context, Bundle info, int resetActivityCode) {
        super(context);
        mContext = context;
        this.info = info;
        this.resetActivityCode = resetActivityCode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verify_otp);


        verifyOtp = (Button) findViewById(R.id.verify_otp_next);
        OtpEditText = (EditText) findViewById(R.id.verify_otp_edit_text);
        mTextField = (TextView) findViewById(R.id.timeout_text);

        VerifyOtp.this.setCanceledOnTouchOutside(false);

        VerifyOtp.this.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("verifyOtp","inside on dismiss");
            }
        });
        VerifyOtp.this.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("verifyOtp","inside on cancel");
                VerifyOtp.TimeoutTimer.cancel();
            }
        });

        TimeoutTimer = new CountDownTimer(DELAY_BETWEEN_OTP_REQUESTS_MILLIS, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("done!");
                Toast.makeText(mContext, "Timeout!!! Please Regenerate OTP", Toast.LENGTH_LONG).show();
                if(resetActivityCode == 1) {
                    regenerateOtpOnTimeout = new RegenerateOtp(mContext, info, 1);
                } else {
                    regenerateOtpOnTimeout = new RegenerateOtp(mContext, info);
                }

                VerifyOtp.this.dismiss();
                regenerateOtpOnTimeout.show();

            }
        }.start();

        verifyOtp.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             Log.d("VerifyOtp", "next button clicked....................");
                                             VerifyOtpNext();
                                         }
                                     }

        );
        mPhoneNumber = info.getString(mContext.getResources().getString(R.string.registrationData));
        Log.d("VerifyOtp", mContext.getResources().getString(R.string.registrationData));

    }

    private void VerifyOtpNext() {
        Log.d("VerifyOtp", "inside verfy otp next");
        String otp = OtpEditText.getText().toString();
        if(otp.length() == 0) return;
        if (otp.length() != 0) {
            if(resetActivityCode == 1) {
                //startresetactivity
                VerifyOtp.TimeoutTimer.cancel();
                VerifyOtp.this.dismiss();
                info.putString(mContext.getResources().getString(R.string.otp), otp);
                Intent intent = new Intent(mContext, ResetPasswordActivity.class);
                intent.putExtras(info);
                mContext.startActivity(intent);
                resetActivityCode = 0;
            } else {
                //startCreateyouraccount2
                if((Integer.valueOf(otp)).equals(mOtp)) {
                    onOtpVerified();
                }
            }
        }
    }

    private boolean mCanSendOtp = true;
    private void verifyOtp(String phoneNumber) {
        if(mCanSendOtp) {

            mCanSendOtp = false;
            new SendOtpAsync(getContext(), new OnOtpSent() {
                @Override
                public void onDone(Object object, ParseException e) {

                    // otp generated successfully
                    mOtp = (Integer) object;

                    Toast.makeText(mContext, R.string.otpVerification, Toast.LENGTH_SHORT).show();

                    // add sms listener
                    SmsOtpListener.bindListener(new OnOtpReceived() {
                        @Override
                        public void onDone(Integer otp) {
                            System.out.println(".................inside sms otp listner...listner is working............................ ");
                            if(otp.equals(mOtp)) {
                                onOtpVerified();
                            }
                        }
                    });

                    // wait until new otp can be generated
                    (new AsyncTask<Void, Void, Void>() {
                        @Override
                        public void onPreExecute() {
                            mCanSendOtp = false;
                        }

                        @Override
                        public Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(DELAY_BETWEEN_OTP_REQUESTS_MILLIS);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public void onPostExecute(Void result) {
                            System.out.println("onPostExecute : called");
                            mCanSendOtp = true;
                        }
                    }).execute();

                }

                @Override
                public void onCouldNotSend() {
                    // otp could not be generated
                    mCanSendOtp = true;
                    Toast.makeText(mContext, R.string.cannotGenOTP, Toast.LENGTH_LONG).show();
                    SmsOtpListener.unbindListener();
                    VerifyOtp.this.dismiss();
                    VerifyOtp.TimeoutTimer.cancel();
                }
            }).execute(phoneNumber);

        } else {
            // do nothing, but fool the user
            // make him think that he is at least doing something
            Toast.makeText(mContext, "OTP Has already been sent to your mobile number", Toast.LENGTH_LONG).show();
        }
    }

    private void onOtpVerified() {
        Toast.makeText(getContext(), R.string.otpVerified, Toast.LENGTH_SHORT).show();
        SmsOtpListener.unbindListener();

        VerifyOtp.TimeoutTimer.cancel();
        VerifyOtp.this.dismiss();
        Intent intent = new Intent(mContext, CreateYourAccount2.class);
        intent.putExtras(info);
        mContext.startActivity(intent);
    }


}
