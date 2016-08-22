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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilipenko.studentsapp.com.example.pilipenko.custom.ScheduleViewGroup;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;

public class ScheduleDayFragment extends Fragment {

    private IToolbar mToolbarActivity;

    private ImageButton mNavigatorPriorImageButton;
    private ImageButton mNavigatorNextImageButton;
    private TextView mNavigatorSubTitle;
    private TextView mNavigatorTitle;

    private ScheduleViewGroup mScheduleViewGroup;

    public static ScheduleDayFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleDayFragment fragment = new ScheduleDayFragment();
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
        View view = inflater.inflate(R.layout.fragment_schedule_day, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_schedule_day_toolbar);
        mToolbarActivity.useToolbar(toolbar);

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton = (ImageButton) view.findViewById(R.id.toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageButton) view.findViewById(R.id.toolbar_navigator_btn_next);
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_title);
        mNavigatorSubTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_sub_title);

        mNavigatorTitle.setText("Понедельник");
        mNavigatorSubTitle.setText("16.06, чётная неделя");

        mScheduleViewGroup = (ScheduleViewGroup) view.findViewById(R.id.fragment_schedule_day_schedule_view_group);

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

    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.toolbar_navigator_btn_prior:
                    Toast.makeText(getActivity(), "PRIOR", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.toolbar_navigator_btn_next:
                    Toast.makeText(getActivity(), "NEXT", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
