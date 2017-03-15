package com.comp.iitb.vialogue.coordinators;

import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;

/**
 * Created by ironstein on 14/03/17.
 */

public interface OnSlideThumbnailClicked {
    public void onClicked(Slide slide, int slidePosition);
}
