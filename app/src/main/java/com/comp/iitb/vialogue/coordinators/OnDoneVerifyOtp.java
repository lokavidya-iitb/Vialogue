package com.comp.iitb.vialogue.coordinators;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.VerifyOtp;

/**
 * Created by omkar on 22/7/17.
 */

public interface OnDoneVerifyOtp {
    public void done(VerifyOtp.VerifyOtpResponse verifyOtpResponse);
}
