package com.comp.iitb.vialogue.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.adapters.FragmentPageAdapter;
import com.comp.iitb.vialogue.coordinators.OnFragmentInteractionListener;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.viewGroups.PagerContainer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewPager viewpager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout createProjectRelativeLayout;
    private Button createProject;
    private RelativeLayout viewVideosRelativeLayout;
    private Button viewVideos;
    private View mView;
    private PagerContainer mPagerContainer;
    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public Home() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance() {
        Home fragment = new Home();
        Bundle args = new Bundle();
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
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        createProject=(Button)mView.findViewById(R.id.createProject);
        viewVideos=(Button)mView.findViewById(R.id.viewVideos);
        createProjectRelativeLayout = (RelativeLayout) mView.findViewById(R.id.create_projects_relative_layout);
        viewVideosRelativeLayout = (RelativeLayout) mView.findViewById(R.id.view_videos_relative_layout);
        viewpager= (ViewPager) getActivity().findViewById(R.id.viewpager);
        createProjectRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedRuntimeContent.createEmptyProject(getActivity());
                viewpager.setCurrentItem(FragmentPageAdapter.CREATE_PROJECT,true);
            }
        });
        createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedRuntimeContent.createEmptyProject(getActivity());
                viewpager.setCurrentItem(FragmentPageAdapter.CREATE_PROJECT,true);
            }
        });
        
        viewVideosRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(FragmentPageAdapter.INCEPTIONMYPROJECTS,true);
            }
        });
        viewVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(FragmentPageAdapter.INCEPTIONMYPROJECTS,true);
            }
        });

        /*mPagerContainer = (PagerContainer) mView.findViewById(R.id.pager_container);

        ViewPager pager = mPagerContainer.getViewPager();
        PagerAdapter adapter = new HeroAdapter(getContext());
        pager.setAdapter(adapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(adapter.getCount());
        //A little space between pages
        pager.setPageMargin(15);
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        pager.setClipChildren(false);*/

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Home", "onDestroyView : called");
//        RefWatcher refWatcher = App.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

}
