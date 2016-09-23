package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.service.FetchDataIntentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractViewPagerFragment<T> extends Fragment implements LoaderManager.LoaderCallbacks<Map<String, List<T>>>, MainContentActivity.IFragmentReceiver {

    private static final String TAG = "AbstractVPFragment";

    private IToolbar mToolbarActivity;

    private RelativeLayout mNavigatorLayout;
    private TextView mNavigatorTitle;
    private TextView mNavigatorSubTitle;
    private ImageView mNavigatorPriorImageButton;
    private ImageView mNavigatorNextImageButton;

    private ProgressBar mProgressBarViewPager;
    private ViewPager mGradesViewPager;
    private FrameLayout mFrameLayout;

    private FragmentsAdapter mFragmentsAdapter;
    private GradesOnPageChangeListener mGradesOnPageChangeListener;

    private String[] mTitles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, AbstractViewPagerFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_abstract_view_pager, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_academic_plan_view_pager_toolbar);

        mNavigatorLayout = (RelativeLayout) view.findViewById(R.id.fragment_academic_plan_view_pager_toolbar_navigator);
        mNavigatorTitle = (TextView) view.findViewById(R.id.layout_toolbar_navigator_tv_title);
        mNavigatorSubTitle = (TextView) view.findViewById(R.id.layout_toolbar_navigator_tv_sub_title);
        mNavigatorPriorImageButton = (ImageView) view.findViewById(R.id.layout_toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageView) view.findViewById(R.id.toolbar_navigator_btn_next);

        mProgressBarViewPager = (ProgressBar) view.findViewById(R.id.fragment_academic_plan_view_pager_progress_bar);
        mGradesViewPager = (ViewPager) view.findViewById(R.id.fragment_academic_plan_view_pager_view_pager);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.fragment_academic_plan_view_pager_layout_error);

        mToolbarActivity.useToolbar(toolbar, getTitle());

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);
        mNavigatorTitle.setText(getTitle());

        mGradesOnPageChangeListener = new GradesOnPageChangeListener();
        mGradesViewPager.addOnPageChangeListener(mGradesOnPageChangeListener);
        getLoaderManager().getLoader(0).forceLoad();

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
    public Loader<Map<String, List<T>>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader: ");
        return getAsyncTaskLoader();
    }

    @Override
    public void onLoadFinished(Loader<Map<String, List<T>>> loader, Map<String, List<T>> data) {
        Log.i(TAG, "onLoadFinished: ");

        if (data == null || data.keySet().size() == 0) {

            if(!FetchUtils.isNetworkAvailableAndConnected(getContext())) {
                showErrorNetwork();
                return;
            }

            mProgressBarViewPager.setVisibility(View.VISIBLE);
            mGradesViewPager.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.GONE);

            Intent intent = getIntentToLoad();
            this.getContext().startService(intent);

            return;
        }

        updateAdapter(data);
        showNavigatorLayout();

        mProgressBarViewPager.setVisibility(View.GONE);
        mGradesViewPager.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Map<String, List<T>>> loader) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        if (!intent.getStringExtra(FetchDataIntentService.KEY_EXTRA_ACTION).equals(getAction())) {
            return;
        }

        boolean result = intent.getBooleanExtra(FetchDataIntentService.KEY_EXTRA_STATUS, false);

        if (result) {
            getLoaderManager().getLoader(0).forceLoad();
        } else {
            showErrorNetwork();
        }
    }

    protected abstract int getTitle();

    protected abstract String getAction();

    protected abstract Loader<Map<String, List<T>>> getAsyncTaskLoader();

    protected abstract Intent getIntentToLoad();

    protected abstract Fragment getItemFragment(List<T> list);

    private void showNavigatorLayout() {
        mNavigatorLayout.setVisibility(View.VISIBLE);
        mToolbarActivity.setToolbarTitle(0);
    }

    private void showErrorNetwork() {
        mProgressBarViewPager.setVisibility(View.GONE);
        mGradesViewPager.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);

        ((TextView) mFrameLayout.findViewById(R.id.layout_error_text_view_title)).setText(R.string.error);
        ((TextView) mFrameLayout.findViewById(R.id.layout_error_text_view_sub_title)).setText(R.string.errorLessonsProgress);
        Button button = (Button) mFrameLayout.findViewById(R.id.layout_error_button_go_to);
        button.setText(R.string.errorLessonsRefresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                    Toast.makeText(getActivity(), "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
                    return;
                }
                getLoaderManager().restartLoader(0, null, AbstractViewPagerFragment.this).forceLoad();
            }
        });

    }

    private void updateToolbar(int position) {
        mNavigatorSubTitle.setText(mTitles[position]);
    }

    private void updateAdapter(Map<String, List<T>> data) {
        mTitles = data.keySet().toArray(new String[1]);

        mFragmentsAdapter = new FragmentsAdapter(getChildFragmentManager(), data);

        mGradesViewPager.setAdapter(mFragmentsAdapter);
        mGradesViewPager.setCurrentItem(0);
        updateToolbar(0);
    }

    private class FragmentsAdapter extends FragmentStatePagerAdapter {

        private String[] keys;
        private Map<String, List<T>> mDataMap;

        public FragmentsAdapter(FragmentManager fm, Map<String, List<T>> data) {
            super(fm);
            mDataMap = data;
            keys = mDataMap.keySet().toArray(new String[1]);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem: " + position + " CurrentItem: " + keys[position]);
            return getItemFragment(mDataMap.get(keys[position]));
        }

        @Override
        public int getCount() {
            return keys.length;
        }
    }

    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (mTitles == null || mTitles.length == 0) {
                return;
            }
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
