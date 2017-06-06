package com.comp.iitb.vialogue.coordinators;

import com.comp.iitb.vialogue.Network.LokavidyaSso.Apis.LogOut;

/**
 * Created by ironstein on 25/05/17.
 */

public interface OnDoneLogOut {
    public void done(LogOut.LogOutResponse logOutResponse);
}
