package com.comp.iitb.vialogue.adapters;

/**
 * Created by jeffrey on 17/1/17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.comp.iitb.vialogue.GlobalStuff.Master;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ProjectsShowcase;

import java.io.File;
import java.util.List;

public class SavedProjectsAdapter extends RecyclerView.Adapter<SavedProjectsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProjectsShowcase> albumList;
    private int listItemPositionForPopupMenu;

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


    public SavedProjectsAdapter(Context mContext, List<ProjectsShowcase> albumList) {
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
        final ProjectsShowcase album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getImagesCount() + " Images");
        holder.AudioCount.setText(album.getAudioCount() +" Audios");
        Glide.with(mContext).load(album.getImageFile()).placeholder(R.drawable.ic_computer_black_24dp).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow,holder.getAdapterPosition(), album.getName());
            }
        });
    }

    private void showPopupMenu(View view, int listItemPosition, String projectName) {
        listItemPositionForPopupMenu = listItemPosition;
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(listItemPosition,projectName));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int listItemPosition;
        private String projectName;

        public MyMenuItemClickListener(int listItemPosition, String projectName) {
            this.listItemPosition = listItemPosition;
            this.projectName = projectName;

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.deleteThis:
                    int newPosition = listItemPosition;
                    albumList.remove(newPosition);
                    notifyItemRemoved(newPosition);
                    notifyItemRangeChanged(newPosition, albumList.size());
                    Storage.deleteThisFolder(Master.getSavedProjectsPath() + "/" + projectName);
                    return true;
                case R.id.renameThis:
                    showChangeLangDialog(projectName, listItemPosition);
                    return true;
                default:
            }
            return false;
        }
    }
    public void showChangeLangDialog(final String projectName, final int listItemPosition) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View dialogView = inflater.inflate(R.layout.dialog_rename, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.rename);

        dialogBuilder.setTitle("Rename the project?");
        dialogBuilder.setMessage("Enter the new project name:");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                File oldName = new File(Environment.getExternalStorageDirectory(),Master.getSavedProjectsPath()+"/"+projectName);
                File newName = new File(Environment.getExternalStorageDirectory(),Master.getSavedProjectsPath()+"/"+edt.getText().toString());
                boolean success = oldName.renameTo(newName);
                ProjectsShowcase renamingStub = albumList.get(listItemPosition);
                albumList.remove(listItemPosition);
                renamingStub.setName(edt.getText().toString());
                albumList.add(renamingStub);
                Log.d("Renaming stub working","---------Yea");
                notifyDataSetChanged();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Just close the dialog
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
