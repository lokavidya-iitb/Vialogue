package com.comp.iitb.vialogue.helpers;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.comp.iitb.vialogue.adapters.SlideRecyclerViewAdapter;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by shubh on 12-01-2017.
 */

public class SlideRecyclerViewCallback extends ItemTouchHelper.Callback {

    private RecyclerView.Adapter<SlideRecyclerViewAdapter.ViewHolder> mAdapter;

    public SlideRecyclerViewCallback(RecyclerView.Adapter<SlideRecyclerViewAdapter.ViewHolder> adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        SharedRuntimeContent.changeSlidePosition(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        // and notify the adapter that its dataset has changed
        mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
