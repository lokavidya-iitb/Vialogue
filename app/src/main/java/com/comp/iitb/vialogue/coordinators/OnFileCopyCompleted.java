package com.comp.iitb.vialogue.coordinators;

import java.io.File;

/**
 * Created by shubh on 17-01-2017.
 */

public interface OnFileCopyCompleted {
    public void done(File file,boolean isSuccessful);
}
