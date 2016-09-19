package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pilipenko.studentsapp.custom.ScheduleSessionViewGroup;
import com.example.pilipenko.studentsapp.data.SessionLesson;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.schedule_day_menu_item_today:
                mSessionViewGroup.addSession(getSessionListLesson(7));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<SessionLesson> getSessionListLesson(int count) {
        List<SessionLesson> lessons = new ArrayList<>();
        Random random = new Random();
        int lessonsCount = 3 + random.nextInt(count - 3);
        Calendar cal = GregorianCalendar.getInstance();
        for (int i = 0; i < lessonsCount; i++) {
            cal.clear();
            cal.set(2016, 6, 5 + random.nextInt(5), 11 + random.nextInt(8), random.nextBoolean() ? 30 : 0);
            SessionLesson lesson = new SessionLesson(
                    "Математический анализ",
                    "Затонская К.К.",
                    SessionLesson.Type.values()[random.nextInt(SessionLesson.Type.values().length)],
                    "405 каб, 9к",
                    cal.getTime());
            lessons.add(lesson);
        }

        return lessons;
    }
}
