package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.example.pilipenko.studentsapp.custom.ScheduleLessonsViewGroup;
import com.example.pilipenko.studentsapp.data.Lesson;
import com.example.pilipenko.studentsapp.data.LessonLab;
import com.example.pilipenko.studentsapp.data.StudentGroupLab;
import com.example.pilipenko.studentsapp.service.FetchDataIntentService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ScheduleDayFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Lesson>> {

    private ScrollView mScrollView;
    private ProgressBar mProgressBar;
    private ScheduleLessonsViewGroup mScheduleLessonsViewGroup;

    private static final String TAG = "ScheduleDayFragment";

    private static SimpleDateFormat mSimpleDateFormatRequest = new SimpleDateFormat("yyyyMMdd", new Locale("ru"));

    private static final String KEY_EXTRA_DATE = "EXTRA_DATE";

    private Date mFragmentDate;

    public static ScheduleDayFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_EXTRA_DATE, date);

        ScheduleDayFragment fragment = new ScheduleDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_schedule_day, container, false);

        mScrollView = (ScrollView) view.findViewById(R.id.fragment_schedule_day_scroll_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_schedule_day_progress_bar);
        mScheduleLessonsViewGroup = (ScheduleLessonsViewGroup) view.findViewById(R.id.fragment_schedule_day_schedule_view_group);

        mFragmentDate = (Date) getArguments().getSerializable(KEY_EXTRA_DATE);
        getLoaderManager().restartLoader(0, null, this).forceLoad();

        return view;
    }

    @Override
    public Loader<List<Lesson>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader: ");
        return new ScheduleDayCursorLoader(getActivity(), mFragmentDate);
    }

    @Override
    public void onLoadFinished(Loader<List<Lesson>> loader, List<Lesson> list) {
        Log.i(TAG, "onLoadFinished: ");

        if (list == null || list.size() == 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);

            Intent intent = FetchDataIntentService.newIntentFetchSchedule(
                    this.getContext(),
                    mSimpleDateFormatRequest.format(mFragmentDate),
                    StudentGroupLab.get(this.getContext()).getStudentGroups().get(0).getIdentifier());
            this.getContext().startService(intent);

            return;
        }

        if (!LessonLab.scheduleIsAbsent(list)) {
            mScheduleLessonsViewGroup.addLessons(list, null, Utils.isToday(mFragmentDate));
        } else {
            mScheduleLessonsViewGroup.setIsInformation(true, getString(R.string.absentLessons), null, null);
        }

        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Lesson>> loader) {

    }

    public Date getFragmentDate() {
        return mFragmentDate;
    }

    public void setFragmentDate(Date fragmentDate) {
        if (!fragmentDate.equals(mFragmentDate)) {
            mFragmentDate = fragmentDate;
            Bundle bundle = getArguments();
            bundle.putSerializable(KEY_EXTRA_DATE, mFragmentDate);
            setArguments(bundle);
        }
    }

    private static class ScheduleDayCursorLoader extends AsyncTaskLoader<List<Lesson>> {

        private String mLoaderDate;


        public ScheduleDayCursorLoader(Context context, Date loadDate) {
            super(context);
            mLoaderDate = mSimpleDateFormatRequest.format(loadDate);
        }

        @Override
        public List<Lesson> loadInBackground() {

            List<Lesson> list;
            LessonLab lessonLab = LessonLab.get(this.getContext());
            list = lessonLab.getLessons(mLoaderDate);

            return list;
        }
    }
}
