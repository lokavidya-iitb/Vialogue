package com.comp.iitb.vialogue.adapters;

/**
 * Created by jeffrey on 17/1/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
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
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
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
        Glide.with(mContext).load(album.getImageFile()).placeholder(R.drawable.ic_computer_black_24dp).into(holder.thumbnail);


        holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((Activity) mContext).startActionMode(new ActionBarCallBack(holder.title.getText().toString(), holder.getAdapterPosition()));

                return false;
            }
        });

    }


    class ActionBarCallBack implements ActionMode.Callback {
        private String projectName;
        private int position;


        public ActionBarCallBack(String projectName, int position){
            this.projectName = projectName;
            this.position = position;

        }
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // TODO Auto-generated method stub
            albumList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, albumList.size());
            Storage.deleteThisFolder(Master.getSavedProjectsPath()+"/"+projectName);
            mode.finish();
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            mode.getMenuInflater().inflate(R.menu.delete_projects, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {


        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub

            mode.setTitle("Seriously, delete?");
            return false;
        }

    }

 /*   TODO use this later in GlobalStuff bud
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
*/

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
