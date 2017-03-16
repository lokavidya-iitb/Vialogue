package com.comp.iitb.vialogue.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserAccount extends Fragment {

    public static final String TAG = UserAccount.class.getName();

    private FragmentTabHost fragmentTabHost;

    private TabLayout mTabLayout;
    private Toolbar mToolbar;

    private OnFragmentInteractionListener mListener;

    public UserAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserAccount.
     */
    public static UserAccount newInstance() {
        UserAccount fragment = new UserAccount();
        Bundle args = new Bundle();/*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentTabHost = new FragmentTabHost(getActivity());

        /** Important: Must use Child FragmentManager */
        fragmentTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_user_account);

        Bundle arg1 = new Bundle();
        arg1.putInt(ChildFragment.POSITION_KEY, 1);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("ChildTag1").setIndicator(getResources().getString(R.string.my_projects)),
                InceptionMyProjects.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putInt(ChildFragment.POSITION_KEY, 2);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("ChildTag2").setIndicator(getResources().getString(R.string.saved_videos)),
                DummyFragment.class, arg2);


        Bundle arg3 = new Bundle();
        arg3.putInt(ChildFragment.POSITION_KEY, 3);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("ChildTag3").setIndicator(getResources().getString(R.string.saved_projects)),
                DummyFragment.class, arg3);

        return fragmentTabHost;
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

}
