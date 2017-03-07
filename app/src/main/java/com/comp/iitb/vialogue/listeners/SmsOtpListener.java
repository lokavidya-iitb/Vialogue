package com.comp.iitb.vialogue.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.comp.iitb.vialogue.coordinators.OnOtpReceived;

/**
 * Created by ironstein on 08/03/17.
 */

public class SmsOtpListener extends BroadcastReceiver {

    private String SMS_FORMAT_REGEX = "Hello from Lokavidya. Your OTP is (\\d{4})$";

    private static OnOtpReceived mOnOtpReceived;

    public SmsOtpListener() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();

        // The intent contains PDU objects (Protocol Data Unit),
        // which is a protocol for transfer of SMS messages in
        // telecoms.

        // We obtain an array of these messages that were sent
        // to to our receiver by system.
        Object[] pdus = (Object[]) data.get("pdus");
        String format = data.getString("format");

        for(Object object: pdus) {

            SmsMessage smsMessage;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                smsMessage = SmsMessage.createFromPdu((byte[]) object, format);
            } else {
                smsMessage = SmsMessage.createFromPdu((byte[]) object);
            }

            if(matchOtpMessage(smsMessage.getDisplayOriginatingAddress(), smsMessage.getMessageBody())) {
                mOnOtpReceived.onDone(getOtpFromMessageBody(smsMessage.getMessageBody()));
            }
        }
    }

    public boolean matchOtpMessage(String sender, String messageBody) {
        if (messageBody.matches(SMS_FORMAT_REGEX)) {
            return true;
        } return false;
    }

    public Integer getOtpFromMessageBody(String messageBody) {
        return Integer.parseInt(messageBody.substring(34));
    }

    public static void bindListener(OnOtpReceived onOtpReceived) {
        mOnOtpReceived = onOtpReceived;
    }

}
