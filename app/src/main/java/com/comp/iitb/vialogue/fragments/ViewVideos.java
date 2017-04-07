package com.comp.iitb.vialogue.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.ViewCategoryAdapter;
import com.comp.iitb.vialogue.coordinators.OnFragmentInteractionListener;
import com.comp.iitb.vialogue.models.Category;
import com.comp.iitb.vialogue.models.ParseObjects.models.CategoryType;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewVideos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewVideos extends Fragment {

    private String mParam1;
    private String mParam2;
    private View mView;
    private JSONArray vidArray;
    private JSONArray catArray;
    private static String catOrVids;
    private OnFragmentInteractionListener mListener;
    List<Category> categoryList = new ArrayList<Category>();
    List<String> groupList = new ArrayList<String>();
    List<String> childList = new ArrayList<String>();
    Map<String, Category> videoCollection;
    ExpandableListView expListView;
    RecyclerView recyclerView;
    ViewCategoryAdapter mAdapter;
    private int limit = 5;
    List<ParseObject> receiveEM;
    private List<CategoryType> categoryTypeList = new ArrayList<>();

    private AVLoadingIndicatorView mLoadingAnimation;
    private TextView mNoInternetConnectionTextView;

    // default constructor required
    public ViewVideos() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewVideos.
     */
    public static ViewVideos newInstance(String catOrVid) {
        catOrVids = catOrVid;
        ViewVideos fragment = new ViewVideos();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_view_videos, container, false);

        // Instantiate UI Components
        expListView = (ExpandableListView) mView.findViewById(R.id.video_list);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mLoadingAnimation = (AVLoadingIndicatorView) mView.findViewById(R.id.loading_animation);
        mNoInternetConnectionTextView = (TextView) mView.findViewById(R.id.check_internet_connection_text_view);

        new GetCategoryType().execute("Ok");

//        if(catOrVids.equals("")) {
//            recyclerView.setVisibility(View.VISIBLE);
//            if(isNetworkConnected()) {
//                new GetCategoryType().execute("Ok");
//            } else {
//                Toast.makeText(getContext(),R.string.networkConnect,Toast.LENGTH_LONG);
//            }
//        } else {
//            expListView.setVisibility(View.VISIBLE);
//            if(!isNetworkConnected()) {
//                Toast.makeText(getContext(),R.string.networkConnect,Toast.LENGTH_LONG);
//            }
//        }


        //setGroupIndicatorToRight();

        return mView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void createGroupList() {
        for(Category i :categoryList)
        groupList.add(i.getName());
    }

    private void createCollection() {
        videoCollection = new LinkedHashMap<String, Category>();
        for (Category i : categoryList) {
            childList.add(i.getDesc());

            videoCollection.put(i.getName(), i);
        }

    }
    private void loadChild(String[] videoModels) {
        childList = new ArrayList<String>();
        for (String model : videoModels)
            childList.add(model);
    }

    private class GetCategoryType extends AsyncTask<String, Void, String> {
        private boolean mFetchedDataSuccessfully;

        @Override
        protected void onPreExecute() {
            mFetchedDataSuccessfully = true;
            mLoadingAnimation.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            if(isNetworkConnected()) {
                try {
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Category");
                    query.orderByAscending("createdAt");
                    query.setLimit(limit += 5);
                    receiveEM = query.find();
                    for (ParseObject num : receiveEM) {
                        String id= num.getObjectId();
                        String name=((String) num.get("name"));
                        String desc=((String) num.get("desc"));
                        String imageURL=((String) num.get("imageURL"));

                        CategoryType map = new CategoryType(id, name, desc, imageURL);
                        categoryTypeList.add(map);
                    }
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                    mFetchedDataSuccessfully = false;
                }
            } else {
                mFetchedDataSuccessfully = false;
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if(!mFetchedDataSuccessfully) {
                mNoInternetConnectionTextView.setVisibility(View.VISIBLE);
            } else {
                mNoInternetConnectionTextView.setVisibility(View.GONE);
                mAdapter = new ViewCategoryAdapter(categoryTypeList,getActivity());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mView.getContext(), 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            mLoadingAnimation.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("ViewVideos", "onDestroyView : called");
//        RefWatcher refWatcher = App.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }


}
