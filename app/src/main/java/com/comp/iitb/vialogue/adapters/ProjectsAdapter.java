package com.comp.iitb.vialogue.adapters;

/**
 * Created by jeffrey on 17/1/17.
 */

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.models.ProjectsShowcase;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProjectsShowcase> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, AudioCount;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            AudioCount = (TextView) view.findViewById(R.id.AudioCount);
        }
    }


    public ProjectsAdapter(Context mContext, List<ProjectsShowcase> albumList) {
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
        holder.count.setText(album.getImagesCount() + " Images");
        holder.AudioCount.setText(album.getAudioCount() +" Audios");
        Glide.with(mContext).load(album.getImageFile())/*.placeholder(R.drawable.ic_computer_black_24dp)*/.into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.something1:
                    Toast.makeText(mContext, "Something1", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.something2:
                    Toast.makeText(mContext, "Something2", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
