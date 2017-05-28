package com.comp.iitb.vialogue.coordinators;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.ResetPassword;

/**
 * Created by ironstein on 26/05/17.
 */

public interface OnDoneResetPassword {
    public void done(ResetPassword.ResetPasswordResponse resetPasswordResponse);
}
