package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pilipenko.studentsapp.com.example.pilipenko.custom.ScheduleSessionViewGroup;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;

public class ScheduleSessionFragment extends Fragment {

    private IToolbar mToolbarActivity;
    private ScheduleSessionViewGroup mSessionViewGroup;

    public static ScheduleSessionFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleSessionFragment fragment = new ScheduleSessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_session, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_schedule_session_toolbar);
        mToolbarActivity.useToolbar(toolbar, R.string.nav_session_schedule);

        mSessionViewGroup = (ScheduleSessionViewGroup) view.findViewById(R.id.fragment_schedule_session_schedule_session_view_group);
        mSessionViewGroup.addSession(StaticData.sSessionLessons);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.schedule_day, menu);
    }

}
