package com.comp.iitb.vialogue.coordinators;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.LogIn;

/**
 * Created by ironstein on 25/05/17.
 */

public interface OnDoneLogIn {
    public void done(LogIn.LogInResponse logInResponse);
}
