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
import com.comp.iitb.vialogue.coordinators.OnDone;
import com.comp.iitb.vialogue.coordinators.OnListFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.helpers.DeleteActionMode;
import com.comp.iitb.vialogue.helpers.DeleteActionModeMark2;
import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Image;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;

import java.util.ArrayList;

import android.util.Pair;

/**
 * Created by ironstein on 18/03/17.
 */

public class SlidesRecyclerViewAdapterMark2 extends RecyclerView.Adapter<SlidesRecyclerViewAdapterMark2.SlideViewHolder> implements ItemTouchHelperAdapter {

    public class DeleteActionModeIndicator {

        private boolean isDeleteActionModeEnabled;

        public DeleteActionModeIndicator() {
            isDeleteActionModeEnabled = false;
        }

        public void setEnabled() {
            isDeleteActionModeEnabled = true;
        }

        public void setDisabled() {
            isDeleteActionModeEnabled = false;
        }

        public boolean isDeleteActionModeEnabled() {
            return isDeleteActionModeEnabled;
        }
    }

    public final ArrayList<Pair<Integer, Integer>> mMovementArray = new ArrayList<>();
    public final ArrayList<SlideViewHolder> mDeleteArray = new ArrayList<>();
    public final DeleteActionModeIndicator mDeleteActionModeIndicator = new DeleteActionModeIndicator();

    public void queueSlideForDelete(SlideViewHolder slideViewHolder) {
        for(int i=0; i<mDeleteArray.size(); i++) {
            if(mDeleteArray.get(i).slidePosition == slideViewHolder.slidePosition) {
                return;
            }
        }
        mDeleteArray.add(slideViewHolder);
        slideViewHolder.thumbnail.setColorFilter(Color.argb(200, 0, 0, 0)); // dark gray tint
        slideViewHolder.isQueuedForDelete = true;
    }

    public void unqueueSlideForDelete(SlideViewHolder slideViewHolder) {
        for(int i=0; i<mDeleteArray.size(); i++) {
            if(mDeleteArray.get(i).slidePosition == slideViewHolder.slidePosition) {
                mDeleteArray.remove(i);
                return;
            }
        }
        slideViewHolder.thumbnail.setColorFilter(Color.argb(0, 0, 0, 0)); // no tint
        slideViewHolder.isQueuedForDelete = false;
    }

    public void deleteSlides() {
        System.out.println("mDeleteArray : " + mDeleteArray);
        for(int i=0; i<mDeleteArray.size(); i++) {
            SharedRuntimeContent.justDeleteSlide(mDeleteArray.get(i).slidePosition);
            notifyItemRemoved(mDeleteArray.get(i).slidePosition);
        }
        mDeleteArray.clear();
//        mRecyclerView.getRecycledViewPool().clear();
        mRecyclerView.invalidate();
    }

    public void undeleteSlides() {
        for(int i=0; i<mDeleteArray.size(); i++) {
            mDeleteArray.get(i).isQueuedForDelete = false;
            mDeleteArray.get(i).thumbnail.setColorFilter(Color.argb(0, 0, 0, 0)); // no tint
        }
    }

    public class SlideViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public final View view;
        public final ImageView thumbnail;
        public final View audioLayer;
        public final View videoLayer;
        public final View unselectedLayer;
        public int slidePosition;
        public final ImageView videoPlayIcon;
        public boolean isQueuedForDelete;

        public SlideViewHolder(View view) {
            super(view);
            this.view = view;
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            audioLayer = view.findViewById(R.id.audio_layer);
            videoLayer = view.findViewById(R.id.play_video);
            unselectedLayer = view.findViewById(R.id.unselected_layer);
            videoPlayIcon = (ImageView) view.findViewById(R.id.video_play_icon);

            // initial values
            isQueuedForDelete = false;
            thumbnail.setColorFilter(Color.argb(0, 0, 0, 0)); // no tint
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
            mDeleteActionModeIndicator.setEnabled();
            isQueuedForDelete = true;
            queueSlideForDelete(this);
            DeleteActionModeMark2 actionMode = new DeleteActionModeMark2(
                    activity,
                    new OnDone() {
                        @Override
                        public void done(boolean isDone) {
                            if(isDone) {
                                // should delete
                                deleteSlides();
                            } else {
                                // should not delete
                                mDeleteActionModeIndicator.setDisabled();
                                undeleteSlides();
                            }
                        }
                    }
            );
            activity.startActionMode(actionMode);
        }

        @Override
        public void onDragEnabled() {}

        @Override
        public void onDragDisabled() {
            if (mMovementArray.size() != 0) {
                int initialPosition = mMovementArray.get(0).first;
                int finalPosition = mMovementArray.get(mMovementArray.size() - 1).second;

                // This has to be done because of the way in which the
                // ParseObjectsCollection.move function is implemented
                if ((finalPosition > initialPosition)) {
                    finalPosition += 1;
                }
                System.out.println("initialPosition : " + initialPosition);
                System.out.println("finalPosition : " + finalPosition);
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
    public void onBindViewHolder(final SlideViewHolder slideViewHolder, int position) {

        final Slide slide = SharedRuntimeContent.getSlideAt(position);
        slideViewHolder.slidePosition = position;
        if (slide.getSlideType() == Slide.SlideType.IMAGE) {
            // IMAGE
            Glide
                    .with(mContext)
                    .load(slide.getResource().getResourceFile())
                    .centerCrop()
                    .into(slideViewHolder.thumbnail);
            if (SharedRuntimeContent.isSelected) {
                slideViewHolder.audioLayer.setVisibility(View.GONE);
            } else if (((Image) slide.getResource()).hasAudio()) {
                slideViewHolder.audioLayer.setVisibility(View.GONE);
            } else {
                slideViewHolder.audioLayer.setVisibility(View.VISIBLE);
            }
            slideViewHolder.videoPlayIcon.setVisibility(View.GONE);
        } else if (slide.getSlideType() == Slide.SlideType.VIDEO) {
            // VIDEO
            Glide
                    .with(mContext)
                    .load(slide.getResource().getResourceFile())
                    .centerCrop()
                    .into(slideViewHolder.thumbnail);
            slideViewHolder.audioLayer.setVisibility(View.GONE);
            slideViewHolder.videoPlayIcon.setVisibility(View.VISIBLE);
        } else if (slide.getSlideType() == Slide.SlideType.QUESTION) {
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
                if(mDeleteActionModeIndicator.isDeleteActionModeEnabled()) {
                    // delete action mode enabled
                    if(slideViewHolder.isQueuedForDelete) {
                        unqueueSlideForDelete(slideViewHolder);
                    } else {
                        queueSlideForDelete(slideViewHolder);
                    }
                } else {
                    // delete action mode disabled
                    if (mOnListFragmentInteractionListener != null) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mOnListFragmentInteractionListener.onListFragmentInteraction(slide);
                    }
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
