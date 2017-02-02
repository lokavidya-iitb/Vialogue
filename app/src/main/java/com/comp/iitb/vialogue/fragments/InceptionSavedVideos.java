package com.comp.iitb.vialogue.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.ProjectsVideoAdapter;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ProjectsShowcase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InceptionSavedVideos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InceptionSavedVideos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InceptionSavedVideos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ProjectsVideoAdapter adapter;
    private List<ProjectsShowcase> projectList;

    private OnFragmentInteractionListener mListener;

    public InceptionSavedVideos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InceptionSavedVideos.
     */
    // TODO: Rename and change types and number of parameters
    public static InceptionSavedVideos newInstance(String param1, String param2) {
        InceptionSavedVideos fragment = new InceptionSavedVideos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_inception_saved_videos, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        /*Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);*/
        //initCollapsingToolbar();
        //Anytime, if you wanna incorporate a cool dev feature, uncomment it and make the toolbar and collapsing toolbar visible

        projectList = new ArrayList<>();
        adapter = new ProjectsVideoAdapter(getContext(), projectList);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareProjects();
        /*
        try {
            Glide.with(this).load("https://cdn0.vox-cdn.com/uploads/blog/sbnu_logo_minimal/213/large_hammerandrails.com.minimal.png").placeholder(R.drawable.ic_computer_black_24dp).into((ImageView) getActivity().findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        // Inflate the layout for this fragment

    }

    private void prepareProjects() {
        List<String> myStringArray = new ArrayList<String>();
        myStringArray= Storage.getMeAllTheFilesHere("/Lokavidya/Projects/SavedVideos");
        for(int i=0;i<myStringArray.size();i++)
        {
            ProjectsShowcase a = new ProjectsShowcase(myStringArray.get(i),Storage.getMeTheeseInThisProject(myStringArray.get(i),"MyProjects","images").get(0),Storage.getMeTheeseInThisProject(myStringArray.get(i),"SavedProjects","images").size(),Storage.getMeTheeseInThisProject(myStringArray.get(i),"SavedProjects","audios").size(),0,1);
            projectList.add(a);
        }


        adapter.notifyDataSetChanged();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
