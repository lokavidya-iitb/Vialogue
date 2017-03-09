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
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Slide} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SlideRecyclerViewAdapter extends RecyclerView.Adapter<SlideRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    public SlideRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
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
        holder.mItem = SharedRuntimeContent.getSlideAt(position);

        if(holder.mItem.getSlideType() == Slide.SlideType.IMAGE) {
            // IMAGE
            holder.mThumbnail.setImageBitmap(holder.mItem.getThumbnail());
            if (SharedRuntimeContent.isSelected) {
                holder.mAudioLayer.setVisibility(View.GONE);
            } else if(((Image) holder.mItem.getResource()).hasAudio()) {
                holder.mAudioLayer.setVisibility(View.GONE);
            } else {
                holder.mAudioLayer.setVisibility(View.VISIBLE);
            }
        } else if(holder.mItem.getSlideType() == Slide.SlideType.VIDEO) {
            // VIDEO
            holder.mThumbnail.setImageBitmap(holder.mItem.getThumbnail());
            holder.mAudioLayer.setVisibility(View.GONE);
        } else if(holder.mItem.getSlideType() == Slide.SlideType.QUESTION) {
            // QUESTION
            holder.mThumbnail.setImageBitmap(holder.mItem.getThumbnail());
            holder.mAudioLayer.setVisibility(View.GONE);
        }

        if (SharedRuntimeContent.selectedPosition != position && SharedRuntimeContent.isSelected) {
            holder.mUnselectedLayer.setVisibility(View.VISIBLE);
        } else if (!SharedRuntimeContent.isSelected) {
            holder.mUnselectedLayer.setVisibility(View.GONE);
        }

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

    @Override
    public int getItemCount() {
        return SharedRuntimeContent.getNumberOfSlides();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final View mView;
        public final ImageView mThumbnail;
        public final View mAudioLayer;
        public final View mVideoLayer;
        public final View mUnselectedLayer;
        public Slide mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            mView.setOnLongClickListener(this);
            mAudioLayer = view.findViewById(R.id.audio_layer);
            mVideoLayer = view.findViewById(R.id.play_video);
            mUnselectedLayer = view.findViewById(R.id.unselected_layer);
        }

        @Override
        public boolean onLongClick(View v) {
            Activity activity = (Activity) mView.getContext();

            // TODO change this
            DeleteActionMode actionMode = new DeleteActionMode(
                    activity,
                    SharedRuntimeContent.getSlidePosition(mItem),
                    SharedRuntimeContent.projectAdapter);
            activity.startActionMode(actionMode);
            //SharedRuntimeContent.mainActivity.onContextDeleteMenuRequired(3);
            return false;
        }
    }
}
