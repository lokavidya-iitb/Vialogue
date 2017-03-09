package com.comp.iitb.vialogue.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by ironstein on 09/03/17.
 */

public class SlideThumbnailsRecyclerViewAdapter extends RecyclerView.Adapter<SlideThumbnailsRecyclerViewAdapter.SlideViewHolder> {

    private Context mContext = null;
    private int mCurrentSlidePosition;
    private ArrayList<byte []> mByteArrayList;

    public SlideThumbnailsRecyclerViewAdapter(Context context, int currentSlidePosition) {
        mContext = context;
        mCurrentSlidePosition = currentSlidePosition;

        mByteArrayList = new ArrayList<byte[]>();
        for(Slide slide : SharedRuntimeContent.getAllSlides()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            slide.getThumbnail().compress(Bitmap.CompressFormat.PNG, 100, stream);
            mByteArrayList.add(stream.toByteArray());
        }
    }

    public SlideThumbnailsRecyclerViewAdapter() {}

    public class SlideViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public SlideViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    @Override
    public SlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View slideView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_thumbnail, parent, false);
        return new SlideViewHolder(slideView);
    }

    @Override
    public void onBindViewHolder(SlideViewHolder slideVieHolder, int position) {
        Slide slide = SharedRuntimeContent.getSlideAt(position);

        if(mContext == null) {
            // use only dummy images everywhere
            slideVieHolder.thumbnail.setImageResource(R.drawable.app_logo);
        } else {
            // use actual thumbnails from the slides
            Glide.with(mContext)
                    .fromBytes()
                    .load(mByteArrayList.get(position))
                    .placeholder(R.drawable.app_logo)
                    .into(slideVieHolder.thumbnail);

            if(mCurrentSlidePosition == position) {
                slideVieHolder.thumbnail.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                int padding = mContext.getResources().getDimensionPixelOffset(R.dimen.padding_slide_thumbnails);
                slideVieHolder.thumbnail.setPadding(padding, padding, padding, padding);
            }
        }
    }

    @Override
    public int getItemCount() {
        return SharedRuntimeContent.getNumberOfSlides();
    }

}
