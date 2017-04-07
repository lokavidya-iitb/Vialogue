package com.comp.iitb.vialogue.adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.activity.UploadVideoActivity;
import com.comp.iitb.vialogue.activity.VideoPlayer;
import com.comp.iitb.vialogue.models.Category;

import java.util.List;

public class CategoriesExpandableAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<Category> categoryList;

    public CategoriesExpandableAdapter(Activity context,
                                 List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;

    }

    public Object getChild(int groupPosition, int childPosition) {
        return categoryList.get(groupPosition).getDesc();
    }

    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String OneCategory = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_categories_desc, null);
        }
        Button button = (Button) convertView.findViewById(R.id.button);
        final View finalConvertView = convertView;
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent viewVid = new Intent(context, VideoPlayer.class);
                /*viewVid.putExtra("URL", ""+categoryList.get(groupPosition).getImageURL());*/
                viewVid.putExtra("id", ""+categoryList.get(groupPosition).getId());
                viewVid.putExtra("name",""+categoryList.get(groupPosition).getName());

                context.startActivity(viewVid);

            }
        });

        TextView item = (TextView) convertView.findViewById(R.id.description);


        item.setText("This will be a description placeholder! This will be a description placeholder! This will be a description placeholder! Are you watching closely?! This will be a description placeholder!"/*OneCategory*/);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    public Object getGroup(int groupPosition) {
        return categoryList.get(groupPosition).getName();
    }

    public int getGroupCount() {
        return categoryList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_categories_group,
                    null);
        }


        TextView item = (TextView) convertView.findViewById(R.id.category_name);


        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}