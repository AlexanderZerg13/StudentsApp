package com.example.pilipenko.studentsapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;

public class SettingsFragment extends Fragment{

    private IToolbar mToolbarActivity;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_settings, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.activity_settings_toolbar);
        mToolbarActivity.useToolbar(toolbar, R.string.settings);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (IToolbar) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
    }
}
