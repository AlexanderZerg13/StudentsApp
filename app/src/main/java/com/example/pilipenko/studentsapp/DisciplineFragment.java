package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DisciplineFragment extends Fragment {

    private ToolbarI mToolbarActivity;

    public static DisciplineFragment newInstance() {

        Bundle args = new Bundle();

        DisciplineFragment fragment = new DisciplineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (ToolbarI) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discipline, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_discipline_toolbar);
        mToolbarActivity.useToolbar(toolbar);

        return view;
    }

    public interface ToolbarI {
        void useToolbar(Toolbar toolbar);
    }
}
