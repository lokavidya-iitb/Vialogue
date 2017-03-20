package com.comp.iitb.vialogue.coordinators;

/**
 * Created by ironstein on 18/03/17.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
