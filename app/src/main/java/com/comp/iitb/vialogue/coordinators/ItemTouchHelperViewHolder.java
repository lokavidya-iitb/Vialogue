package com.comp.iitb.vialogue.coordinators;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by ironstein on 18/03/17.
 */

public interface ItemTouchHelperViewHolder {
    /**
     * Called when the {@link ItemTouchHelper} first registers an
     * item as being moved or swiped.
     * Implementations should update the item view to indicate
     * it's active state.
     */
    void onItemSelected();


    /**
     * Called when the {@link ItemTouchHelper} has completed the
     * move or swipe, and the active item state should be cleared.
     */
    void onItemClear();

    /**
     * Called when the view is long Clicked (but not dragged)
     */
    void onLongClick();

    void onDragEnabled();

    void onDragDisabled();
}
