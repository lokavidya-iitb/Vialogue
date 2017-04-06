package com.comp.iitb.vialogue.adapters;

/**
 * Created by jeffrey on 17/1/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.OfflineVideoPlayer;
import com.comp.iitb.vialogue.activity.VideoPlayer;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ProjectsShowcase;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProjectsVideoAdapter extends RecyclerView.Adapter<ProjectsVideoAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProjectsShowcase> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public ProjectsVideoAdapter(Context mContext, List<ProjectsShowcase> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_showcase_cardview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProjectsShowcase album = albumList.get(position);
        holder.title.setText(album.getName());
        File Video = new File(Master.getSavedVideosPath()+"/"+album.getName()+"/"+album.getName()+".mp4");
        holder.thumbnail.setImageBitmap(new Storage(mContext).getVideoThumbnail(Environment.getExternalStorageDirectory()+Video.getAbsolutePath()));

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Just open an existing video player
                /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Video.getAbsolutePath()));
                intent.setDataAndType(Uri.parse(Video.getAbsolutePath()), "video/mp4");
                mContext.startActivity(intent);*/

                Intent viewVid = new Intent(mContext, OfflineVideoPlayer.class);
                viewVid.putExtra("url",Environment.getExternalStorageDirectory()+Video.getPath());
                viewVid.putExtra("name",""+album.getName());
                mContext.startActivity(viewVid);

            }
        });
        }



    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
