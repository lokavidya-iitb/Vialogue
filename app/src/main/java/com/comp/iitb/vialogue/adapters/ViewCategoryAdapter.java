package com.comp.iitb.vialogue.adapters;

/**
 * Created by jeffrey on 13/2/17.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.fragments.ViewVideosCategory;
import com.comp.iitb.vialogue.models.ParseObjects.models.CategoryType;

import java.util.List;

public class ViewCategoryAdapter extends RecyclerView.Adapter<ViewCategoryAdapter.MyViewHolder> {

    private List<CategoryType> moviesList;
    private Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desc;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            desc = (TextView) view.findViewById(R.id.desc);
            image = (ImageView) view.findViewById(R.id.imageView);
        }
    }


    public ViewCategoryAdapter(List<CategoryType> moviesList, Activity context) {
        this.moviesList = moviesList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_category_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CategoryType category = moviesList.get(position);
        holder.name.setText(category.getName());
        holder.desc.setText(category.getDesc());
        Glide.with(context).load(category.getImageURL()).placeholder(R.drawable.ic_computer_black_24dp).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                ViewVideosCategory newFragment = ViewVideosCategory.newInstance(category.getId()+"");
                Bundle args = new Bundle();
                newFragment.setArguments(args);
                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.bodyFragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }

        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}