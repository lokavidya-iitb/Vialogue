package com.comp.iitb.vialogue.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.ItemTouchHelperAdapter;
import com.comp.iitb.vialogue.coordinators.ItemTouchHelperViewHolder;
import com.comp.iitb.vialogue.coordinators.OnListFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.helpers.DeleteActionMode;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;

import java.util.ArrayList;

/**
 * Created by ironstein on 18/03/17.
 */

public class SlidesRecyclerViewAdapterMark2 extends RecyclerView.Adapter<SlidesRecyclerViewAdapterMark2.SlideViewHolder> implements ItemTouchHelperAdapter {

    public class SlideViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public final View view;
        public final ImageView thumbnail;
        public final View audioLayer;
        public final View videoLayer;
        public final View unselectedLayer;
        public int slidePosition;
        public final ImageView videoPlayIcon;

        public SlideViewHolder(View view) {
            super(view);
            this.view = view;
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            audioLayer = view.findViewById(R.id.audio_layer);
            videoLayer = view.findViewById(R.id.play_video);
            unselectedLayer = view.findViewById(R.id.unselected_layer);
            videoPlayIcon = (ImageView) view.findViewById(R.id.video_play_icon);
        }

        @Override
        public void onItemSelected() {
            thumbnail.setColorFilter(Color.argb(200, 0, 0, 0)); // dark gray tint
        }

        @Override
        public void onItemClear() {
            thumbnail.setColorFilter(Color.argb(0, 0, 0, 0)); // no tint
        }

        @Override
        public void onLongClick() {
            System.out.println("onLongClick : called");
            Activity activity = (Activity) view.getContext();

            // TODO change this
            DeleteActionMode actionMode = new DeleteActionMode(
                    activity,
                    slidePosition,
                    SharedRuntimeContent.projectAdapter);
            activity.startActionMode(actionMode);
        }

        @Override
        public void onDragEnabled() {
            mRootView.getParent().requestDisallowInterceptTouchEvent(true);
//            view.getParent().requestDisallowInterceptTouchEvent(true);
        }

        @Override
        public void onDragDisabled() {
            (new AsyncTask<Void, Void, Void>() {
                @Override
                public Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void onPostExecute(Void result) {
                    mRootView.getParent().requestDisallowInterceptTouchEvent(false);
                }

            }).execute();
        }


    }

    private Context mContext;
    private ArrayList<Boolean> mItems;
    private OnListFragmentInteractionListener mOnListFragmentInteractionListener;
    private View mRootView;

    public SlidesRecyclerViewAdapterMark2(Context context, OnListFragmentInteractionListener onListFragmentInteractionListener, View rootView) {
        mContext = context;
        mOnListFragmentInteractionListener = onListFragmentInteractionListener;
        mRootView = rootView;
    }

    public void setItems(ArrayList<Boolean> items) {
        mItems = items;
    }

    @Override
    public SlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SlideViewHolder slideViewHolder, int position) {

        final Slide slide = SharedRuntimeContent.getSlideAt(position);
        slideViewHolder.slidePosition = position;
        if(slide.getSlideType() == Slide.SlideType.IMAGE) {
            // IMAGE
            Glide
                    .with(mContext)
                    .load(slide.getResource().getResourceFile())
                    .centerCrop()
                    .into(slideViewHolder.thumbnail);
            if (SharedRuntimeContent.isSelected) {
                slideViewHolder.audioLayer.setVisibility(View.GONE);
            } else if(((Image) slide.getResource()).hasAudio()) {
                slideViewHolder.audioLayer.setVisibility(View.GONE);
            } else {
                slideViewHolder.audioLayer.setVisibility(View.VISIBLE);
            }
            slideViewHolder.videoPlayIcon.setVisibility(View.GONE);
        } else if(slide.getSlideType() == Slide.SlideType.VIDEO) {
            // VIDEO
            Glide
                    .with(mContext)
                    .load(slide.getResource().getResourceFile())
                    .centerCrop()
                    .into(slideViewHolder.thumbnail);
            slideViewHolder.audioLayer.setVisibility(View.GONE);
            slideViewHolder.videoPlayIcon.setVisibility(View.VISIBLE);
        } else if(slide.getSlideType() == Slide.SlideType.QUESTION) {
            // QUESTION
            Glide
                    .with(mContext)
                    .load(R.drawable.ic_question)
                    .centerCrop()
                    .into(slideViewHolder.thumbnail);
            slideViewHolder.audioLayer.setVisibility(View.GONE);
            slideViewHolder.videoPlayIcon.setVisibility(View.GONE);
        }

        if (SharedRuntimeContent.selectedPosition != position && SharedRuntimeContent.isSelected) {
            slideViewHolder.unselectedLayer.setVisibility(View.VISIBLE);
        } else if (!SharedRuntimeContent.isSelected) {
            slideViewHolder.unselectedLayer.setVisibility(View.GONE);
        }

        slideViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnListFragmentInteractionListener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mOnListFragmentInteractionListener.onListFragmentInteraction(slide);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return SharedRuntimeContent.getNumberOfSlides();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        SharedRuntimeContent.changeSlidePosition(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
}
