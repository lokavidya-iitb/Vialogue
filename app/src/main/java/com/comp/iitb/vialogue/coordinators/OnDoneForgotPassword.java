package com.comp.iitb.vialogue.coordinators;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.ForgotPassword;

/**
 * Created by ironstein on 26/05/17.
 */

public interface OnDoneForgotPassword {
    public void done(ForgotPassword.ForgotPasswordResponse response);
}
