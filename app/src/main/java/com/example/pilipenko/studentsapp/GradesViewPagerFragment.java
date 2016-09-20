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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.data.LessonProgressLab;
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
    private FrameLayout mFrameLayout;

    private GradesFragmentsAdapter mGradesFragmentsAdapter;
    private GradesOnPageChangeListener mGradesOnPageChangeListener;

    private String[] mTitles;
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

        mNavigatorTitle = (TextView) view.findViewById(R.id.layout_toolbar_navigator_tv_title);
        mNavigatorSubTitle = (TextView) view.findViewById(R.id.layout_toolbar_navigator_tv_sub_title);
        mNavigatorPriorImageButton = (ImageView) view.findViewById(R.id.layout_toolbar_navigator_btn_prior);
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

        mProgressBarViewPager = (ProgressBar) view.findViewById(R.id.fragment_grades_view_pager_progress_bar);
        mGradesViewPager = (ViewPager) view.findViewById(R.id.fragment_grades_view_pager_view_pager);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.fragment_grades_view_pager_layout_error);
        mGradesOnPageChangeListener = new GradesOnPageChangeListener();
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

        if (data == null || data.keySet().size() == 0) {

            if(!FetchUtils.isNetworkAvailableAndConnected(getContext())) {
                showErrorNetwork();
                return;
            }

            mProgressBarViewPager.setVisibility(View.VISIBLE);
            mGradesViewPager.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.GONE);

            Intent intent = FetchDataIntentService.newIntentFetchLessonsProgress(
                    this.getContext(),
                    UserPreferences.getUser(this.getContext()).getId());
            this.getContext().startService(intent);

            return;
        }

        updateAdapter(data);

        mProgressBarViewPager.setVisibility(View.GONE);
        mGradesViewPager.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
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
        } else {
            showErrorNetwork();
        }
    }

    private void showErrorNetwork() {
        mProgressBarViewPager.setVisibility(View.GONE);
        mGradesViewPager.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);

        ((TextView) mFrameLayout.findViewById(R.id.layout_error_text_view_title)).setText(R.string.error);
        ((TextView) mFrameLayout.findViewById(R.id.layout_error_text_view_sub_title)).setText(R.string.errorLessonsProgress);
        mFrameLayout.findViewById(R.id.layout_error_button_go_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                    Toast.makeText(getActivity(), "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
                    return;
                }
                getLoaderManager().restartLoader(0, null, GradesViewPagerFragment.this).forceLoad();
            }
        });

    }

    private void updateToolbar(int position) {
        mNavigatorSubTitle.setText(mTitles[position]);
    }

    private void updateAdapter(Map<String, List<LessonProgress>> data) {
        mTitles = data.keySet().toArray(new String[1]);
        int count = mTitles.length;

        mGradesFragmentsAdapter = new GradesFragmentsAdapter(getChildFragmentManager(), data);
        mGradesViewPager.setAdapter(mGradesFragmentsAdapter);
        mGradesViewPager.addOnPageChangeListener(mGradesOnPageChangeListener);
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

        private String[] keys;
        private Map<String, List<LessonProgress>> mDataMap;
        private Map<String, Fragment> mMap;

        public GradesFragmentsAdapter(FragmentManager fm, Map<String, List<LessonProgress>> data) {
            super(fm);
            mDataMap = data;
            keys = mDataMap.keySet().toArray(new String[1]);

            mMap = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {

            Log.i(TAG, "getItem: " + position + " CurrentItem: " + keys[position]);

            Fragment gradesFragment = GradesFragment.newInstance(mDataMap.get(keys[position]));

//            mMap.put(mSimpleDateFormatRequest.format(calendar.getTime()), scheduleDayFragment);

            return gradesFragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

//            Calendar cal = Calendar.getInstance();
//            cal.setTime(mCurrentDate);
//            cal.add(Calendar.DAY_OF_MONTH, position - mScheduleViewPager.getCurrentItem());
//            mMap.remove(mSimpleDateFormatRequest.format(cal.getTime()));
        }

        public Map<String, Fragment> getMap() {
            return mMap;
        }

        @Override
        public int getCount() {
            return keys.length;
        }
    }

    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int move = 0;
            switch (view.getId()) {
                case R.id.layout_toolbar_navigator_btn_prior:
                    move = -1;
                    break;
                case R.id.toolbar_navigator_btn_next:
                    move = 1;
                    break;
            }
            int position = mGradesViewPager.getCurrentItem();
            if ((position + move != -1) && (position + move != mTitles.length)) {
                position += move;
            }
            mGradesViewPager.setCurrentItem(position, true);
        }
    }

    private class GradesOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected: " + position);
            updateToolbar(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
