package com.comp.iitb.vialogue.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.comp.iitb.vialogue.R;

/**
 * Created by shubham on 27/6/17.
 */

public class RegenerateOtp extends Dialog implements View.OnClickListener {

    private Bundle info;
    private Context mContext;
    private EditText TimeoutPhoneNoEditText;
    private Button TimeoutRegenerateOtpButton;


    private String mPhoneNumber = null;
    String PHONE_NUMBER = "User_mobile_number";
    private boolean mCanSendOtp = true;




    RegenerateOtp(Context context , Bundle info)
    {
        super(context);
        mContext=context;
        this.info=info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regenerate_otp_on_timeout);


        mPhoneNumber = info.getString(PHONE_NUMBER);
        Log.d("RegenerateOtp",mPhoneNumber);

        TimeoutPhoneNoEditText = (EditText)findViewById(R.id.timeout_phone_no_edit_text);
        TimeoutRegenerateOtpButton= (Button)findViewById(R.id.timeout_generate_otp);

        TimeoutPhoneNoEditText.setText(mPhoneNumber);
        TimeoutRegenerateOtpButton.setOnClickListener(this);

        RegenerateOtp.this.setCanceledOnTouchOutside(false);
        RegenerateOtp.this.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("RegenerateOtp","inside on cancel");
            }
        });
        RegenerateOtp.this.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("RegenerateOtp","inside on dismiss");
            }
        });


    }

    @Override
    public void onClick(View v) {
        VerifyOtp verifyOtpOnTimeout= new VerifyOtp(mContext,info);
        RegenerateOtp.this.dismiss();
        verifyOtpOnTimeout.show();
    }
}
