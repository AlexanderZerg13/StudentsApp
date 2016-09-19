package com.example.pilipenko.studentsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.data.Discipline;
import com.example.pilipenko.studentsapp.data.Semester;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradesViewPagerFragment extends Fragment {

    private static final String TAG = "GradesViewPagerFragment";

    private IToolbar mToolbarActivity;
    private ITransitionActions mITransitionActionsActivity;

    private TextView mNavigatorTitle;
    private TextView mNavigatorSubTitle;
    private ImageView mNavigatorPriorImageButton;
    private ImageView mNavigatorNextImageButton;

    private ProgressBar mProgressBarViewPager;
    private ViewPager mGradesViewPager;
    private GradesFragmentsAdapter mGradesFragmentsAdapter;


    private static final int VIEW_PAGER_PAGE_COUNT = 365;

    private int mCurrentSemester = 2;

    public static GradesViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        GradesViewPagerFragment fragment = new GradesViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grades_view_pager, container, false);

        mNavigatorTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_title);
        mNavigatorSubTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_sub_title);
        mNavigatorPriorImageButton = (ImageView) view.findViewById(R.id.toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageView) view.findViewById(R.id.toolbar_navigator_btn_next);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_grades_view_pager_toolbar);
        mToolbarActivity.useToolbar(toolbar, 0);


//        mRecyclerViewDiscipline = (RecyclerView) view.findViewById(R.id.fragment_discipline_rv_disciplines);
//        mRecyclerViewDiscipline.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerViewDiscipline.addItemDecoration(new Utils.SimpleDividerItemDecoration(getActivity()));

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorTitle.setText(R.string.grades_title);

        updateToolbar();

        mProgressBarViewPager = (ProgressBar) view.findViewById(R.id.fragment_grades_view_pager_progress_bar);
        mGradesViewPager = (ViewPager) view.findViewById(R.id.fragment_grades_view_pager_view_pager);
        mGradesFragmentsAdapter = new GradesFragmentsAdapter(getChildFragmentManager(), VIEW_PAGER_PAGE_COUNT);

        mGradesViewPager.setAdapter(mGradesFragmentsAdapter);
        mGradesViewPager.setCurrentItem(VIEW_PAGER_PAGE_COUNT / 2);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (IToolbar) context;
        mITransitionActionsActivity = (ITransitionActions) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
        mITransitionActionsActivity = null;
    }

    private void updateToolbar() {
        mNavigatorSubTitle.setText(StaticData.sSemesters.get(mCurrentSemester).getSemesterName());
    }

    private static class GradesAsyncTaskLoader extends AsyncTaskLoader<List<Semester>> {

        public GradesAsyncTaskLoader(Context context) {

            super(context);
        }

        @Override
        public List<Semester> loadInBackground() {
            return null;
        }
    }

    private class GradesFragmentsAdapter extends FragmentStatePagerAdapter {

        private int count;
        private Map<String, Fragment> map;

        public GradesFragmentsAdapter(FragmentManager fm, int k) {
            super(fm);
            count = k;
            map = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem: " + position + " CurrentItem: " + mGradesViewPager.getCurrentItem());

            Fragment gradesFragment = GradesFragment.newInstance();

//            map.put(mSimpleDateFormatRequest.format(calendar.getTime()), scheduleDayFragment);

            return gradesFragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

//            Calendar cal = Calendar.getInstance();
//            cal.setTime(mCurrentDate);
//            cal.add(Calendar.DAY_OF_MONTH, position - mScheduleViewPager.getCurrentItem());
//            map.remove(mSimpleDateFormatRequest.format(cal.getTime()));
        }

        public Map<String, Fragment> getMap() {
            return map;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.toolbar_navigator_btn_prior:
                    if (mCurrentSemester != 0) {
                        mCurrentSemester--;
//                        updateUI();
                    }
                    break;
                case R.id.toolbar_navigator_btn_next:
                    if (StaticData.sSemesters.size() - 1 != mCurrentSemester) {
                        mCurrentSemester++;
//                        updateUI();
                    }
                    break;
            }
        }
    }
}
