package com.comp.iitb.vialogue.library;

import android.content.Context;

import com.comp.iitb.vialogue.coordinators.OnOtpSent;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ironstein on 08/03/17.
 */

public class SendOtpAsync {

    private Context mContext;
    private OnOtpSent mOnOtpSent;

    public SendOtpAsync(Context context, OnOtpSent onOtpSent) {
        mContext = context;
        mOnOtpSent = onOtpSent;
    }

    public void execute(String phoneNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("phone_number", phoneNumber);
        ParseCloud.callFunctionInBackground("sendOtp", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if(e == null) {
                    // success
                    mOnOtpSent.onDone(object, e);
                } else {
                    // fail
                    mOnOtpSent.onCouldNotSend();
                }
            }
        });
    }
}
