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
import android.util.Pair;

/**
 * Created by ironstein on 18/03/17.
 */

public class SlidesRecyclerViewAdapterMark2 extends RecyclerView.Adapter<SlidesRecyclerViewAdapterMark2.SlideViewHolder> implements ItemTouchHelperAdapter {

    public final ArrayList<Pair<Integer, Integer>> mMovementArray = new ArrayList<>();


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
            onDragDisabled();
        }

        @Override
        public void onLongClick() {
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
            mRecyclerView.getParent().requestDisallowInterceptTouchEvent(true);
            view.setOnClickListener(null);
        }

        @Override
        public void onDragDisabled() {
            if(mMovementArray.size() != 0) {
                int initialPosition = mMovementArray.get(0).first;
                int finalPosition = mMovementArray.get(mMovementArray.size()-1).second;
                // This has to be done because of the way in which the
                // ParseObjectsCollection.move function is implemented
                if(finalPosition > initialPosition) {
                    finalPosition += 1;
                }
                SharedRuntimeContent.changeSlidePosition(initialPosition, finalPosition);
            }
            mMovementArray.clear();
        }
    }

    private Context mContext;
    private ArrayList<Boolean> mItems;
    private OnListFragmentInteractionListener mOnListFragmentInteractionListener;
    private RecyclerView mRecyclerView;

    public SlidesRecyclerViewAdapterMark2(Context context, OnListFragmentInteractionListener onListFragmentInteractionListener, RecyclerView recyclerView) {
        mContext = context;
        mOnListFragmentInteractionListener = onListFragmentInteractionListener;
        mRecyclerView = recyclerView;

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
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
        mMovementArray.add(Pair.create(fromPosition, toPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
}
