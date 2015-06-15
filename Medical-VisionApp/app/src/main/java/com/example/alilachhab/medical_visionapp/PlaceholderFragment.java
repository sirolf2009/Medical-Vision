package com.example.alilachhab.medical_visionapp;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlaceholderFragment extends Fragment
{

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PlaceholderFragment newInstance(int sectionNumber)
    {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView;
        switch(getArguments().getInt(ARG_SECTION_NUMBER))
        {
            case 1:
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.rooster, container, false);
                break;
            case 3:
                rootView = inflater.inflate(R.layout.planning, container, false);
                break;
            case 4:
                rootView = inflater.inflate(R.layout.passent, container, false);
                break;
            case 5:
                rootView = inflater.inflate(R.layout.personeel, container, false);
                break;
            default:
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
