package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.data.LessonProgressLab;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;
import com.example.pilipenko.studentsapp.service.FetchDataIntentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradesViewPagerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Map<String, List<LessonProgress>>>, MainContentActivity.IFragmentReceiver {

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
        getLoaderManager().initLoader(0, null, GradesViewPagerFragment.this);
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
        getLoaderManager().getLoader(0).forceLoad();

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

    @Override
    public Loader<Map<String, List<LessonProgress>>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader: ");
        return new GradesAsyncTaskLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Map<String, List<LessonProgress>>> loader, Map<String, List<LessonProgress>> data) {
        Log.i(TAG, "onLoadFinished: ");

        if (data == null && data.keySet().size() == 0) {

            if(!FetchUtils.isNetworkAvailableAndConnected(getContext())) {
                return;
            }

            mProgressBarViewPager.setVisibility(View.VISIBLE);
            mGradesViewPager.setVisibility(View.GONE);

            Intent intent = FetchDataIntentService.newIntentFetchLessonsProgress(
                    this.getContext(),
                    UserPreferences.getUser(this.getContext()).getId());
            this.getContext().startService(intent);

            return;
        }

        updateAdapter(data);

        mProgressBarViewPager.setVisibility(View.GONE);
        mGradesViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Map<String, List<LessonProgress>>> loader) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        if (!intent.getStringExtra(FetchDataIntentService.KEY_EXTRA_ACTION).equals(FetchDataIntentService.ACTION_LESSONS_PROGRESS)) {
            return;
        }

        boolean result = intent.getBooleanExtra(FetchDataIntentService.KEY_EXTRA_STATUS, false);

        if (result) {
            getLoaderManager().getLoader(0).forceLoad();
        }
    }

    private void updateToolbar() {
        mNavigatorSubTitle.setText(StaticData.sSemesters.get(mCurrentSemester).getSemesterName());
    }

    private void updateAdapter(Map<String, List<LessonProgress>> data) {
        int count = data.keySet().size();

        mGradesFragmentsAdapter = new GradesFragmentsAdapter(getChildFragmentManager(), count);
        mGradesViewPager.setAdapter(mGradesFragmentsAdapter);
        mGradesViewPager.setCurrentItem(count - 1);
    }

    private static class GradesAsyncTaskLoader extends AsyncTaskLoader<Map<String, List<LessonProgress>>> {

        public GradesAsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        public Map<String, List<LessonProgress>> loadInBackground() {
            Log.i(TAG, "loadInBackground: ");
            LessonProgressLab lessonProgressLab = LessonProgressLab.get(getContext());
            Map<String, List<LessonProgress>> map = lessonProgressLab.getGroupLessonsProgress();

            return map;
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
