package com.comp.iitb.vialogue.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnListFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.helpers.DeleteActionMode;
import com.comp.iitb.vialogue.models.DummyContent;
import com.comp.iitb.vialogue.models.DummyContent.Slide;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Slide} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SlideRecyclerViewAdapter extends RecyclerView.Adapter<SlideRecyclerViewAdapter.ViewHolder> {

    private final List<Slide> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SlideRecyclerViewAdapter(List<Slide> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_slide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mThumbnail.setImageBitmap(mValues.get(position).thumbnail);
        if (holder.mItem.slideType == DummyContent.SlideType.IMAGE)
            holder.mAudioMissing.setVisibility(View.VISIBLE);
        else
            holder.mAudioMissing.setVisibility(View.GONE);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }
/*

    void toggleSelection(int pos){}
    void clearSelections(){}
    int getSelectedItemCount(){}
    List<Integer> getSelectedItems(){}
*/


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final View mView;
        public final ImageView mThumbnail;
        public final ImageView mAudioMissing;
        public Slide mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            mView.setOnLongClickListener(this);
            mAudioMissing = (ImageView) view.findViewById(R.id.audio_missing);
        }

        @Override
        public boolean onLongClick(View v) {
            Activity activity = (Activity) mView.getContext();
            DeleteActionMode actionMode = new DeleteActionMode(activity,
                    SharedRuntimeContent.ITEMS,
                    SharedRuntimeContent.getSlidePosition(mItem),
                    SharedRuntimeContent.projectAdapter);
            activity.startActionMode(actionMode);
            SharedRuntimeContent.mainActivity.onContextDeleteMenuRequired(3);
            return false;
        }
    }
}
