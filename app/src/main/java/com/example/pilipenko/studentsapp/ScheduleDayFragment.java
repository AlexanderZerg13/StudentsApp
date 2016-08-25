package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilipenko.studentsapp.com.example.pilipenko.custom.ScheduleViewGroup;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Lesson;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScheduleDayFragment extends Fragment {

    private static final String TAG = "ScheduleDayFragment";

    private IToolbar mToolbarActivity;
    private ITransitionActions mITransitionActions;

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
        mToolbarActivity.useToolbar(toolbar, 0);

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
//        mScheduleViewGroup.addLessons(StaticData.sLessons, new CardClickListener());
        mScheduleViewGroup.setIsSession(true, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITransitionActions.goToSession();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (IToolbar) context;
        mITransitionActions = (ITransitionActions) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
        mITransitionActions = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.schedule_day, menu);
    }

    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            List<Lesson> lessons = getTestListLessons();
            switch (view.getId()) {
                case R.id.toolbar_navigator_btn_prior:
                    mScheduleViewGroup.addLessons(lessons, new CardClickListener());
                    break;
                case R.id.toolbar_navigator_btn_next:
                    mScheduleViewGroup.addLessons(lessons, new CardClickListener());
                    break;
            }
        }

        private List<Lesson> getTestListLessons() {
            int count = 5;
            List<Lesson> returned = new ArrayList<>();
            Random random = new Random();
            Lesson les;
            while (count > 0) {
                les = StaticData.sLessons.get(random.nextInt(StaticData.sLessons.size()));
                if (les.isTwoPair()) {
                    if (count >= 2) {
                        returned.add(les);
                        count -= 2;
                    }
                } else {
                    returned.add(les);
                    count--;
                }
            }

            return returned;
        }
    }

    private class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getTag() instanceof Lesson) {
                Lesson lesson = (Lesson) view.getTag();
                for (int i = 0; i < StaticData.sLessons.size(); i++) {
                    if (lesson.equals(StaticData.sLessons.get(i))) {
                        mITransitionActions.goToDescribeLessons(i);
                        break;
                    }
                }
            }
        }
    }
}
