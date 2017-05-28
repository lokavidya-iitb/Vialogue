package com.comp.iitb.vialogue.coordinators;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.SignUp;

/**
 * Created by ironstein on 25/05/17.
 */

public interface OnDoneSignIn {
    public void done(SignUp.SignUpResponse signUpResponse);
}
