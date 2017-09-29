package ru.infocom.university;

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

import ru.infocom.university.custom.EnabledViewPager;
import ru.infocom.university.interfaces.IToolbar;
import ru.infocom.university.service.FetchDataIntentService;
import ru.infocom.university.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractViewPagerFragment<T> extends Fragment {

    private static final String TAG = "AbstractVPFragment";

    private static final String KEY_POSITION = "POSITION";

    private IToolbar mToolbarActivity;

    private RelativeLayout mNavigatorLayout;
    private TextView mNavigatorTitle;
    private TextView mNavigatorSubTitle;
    private ImageView mNavigatorPriorImageButton;
    private ImageView mNavigatorNextImageButton;

    private ProgressBar mProgressBarViewPager;
    private EnabledViewPager mViewPager;
    private FrameLayout mFrameLayout;

    private FragmentsAdapter mFragmentsAdapter;
    private OnPageChangeListener mOnPageChangeListener;

    private Integer[] mTitles;
    private int mLastPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_abstract_view_pager, container, false);

        Toolbar toolbar = view.findViewById(R.id.fragment_academic_plan_view_pager_toolbar);

        mNavigatorLayout = view.findViewById(R.id.fragment_academic_plan_view_pager_toolbar_navigator);
        mNavigatorTitle = view.findViewById(R.id.layout_toolbar_navigator_tv_title);
        mNavigatorSubTitle = view.findViewById(R.id.layout_toolbar_navigator_tv_sub_title);
        mNavigatorPriorImageButton = view.findViewById(R.id.layout_toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = view.findViewById(R.id.toolbar_navigator_btn_next);

        mProgressBarViewPager = view.findViewById(R.id.fragment_academic_plan_view_pager_progress_bar);
        mViewPager = view.findViewById(R.id.fragment_academic_plan_view_pager_view_pager);
        mFrameLayout = view.findViewById(R.id.fragment_academic_plan_view_pager_layout_error);

        mToolbarActivity.useToolbar(toolbar, getTitle());

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);
        mNavigatorTitle.setText(getTitle());

        mOnPageChangeListener = new OnPageChangeListener();
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

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

    public void showLoading() {
        mProgressBarViewPager.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
    }

    public void hideLoading() {
        mProgressBarViewPager.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
    }


    protected abstract int getTitle();

    protected abstract Fragment getItemFragment(List<T> list);

    protected abstract void reloadData();

    protected Fragment getCurrentFragment() {
        int position = mViewPager.getCurrentItem();
        return mFragmentsAdapter.getFragment(position);
    }

    protected void setPagingEnabled(boolean enabled) {
        mViewPager.setPagingEnabled(enabled);
    }

    protected void showNavigatorLayout() {
        mNavigatorLayout.setVisibility(View.VISIBLE);
        mToolbarActivity.setToolbarTitle(0);
        getActivity().invalidateOptionsMenu();
    }

    public void showErrorNetwork() {
        mProgressBarViewPager.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);

        ((TextView) mFrameLayout.findViewById(R.id.layout_error_text_view_title)).setText(R.string.error);
        ((TextView) mFrameLayout.findViewById(R.id.layout_error_text_view_sub_title)).setText(R.string.errorLessonsProgress);
        Button button = mFrameLayout.findViewById(R.id.layout_error_button_go_to);
        button.setText(R.string.errorLessonsRefresh);
        button.setOnClickListener(view -> {
            if (!FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                Toast.makeText(getActivity(), "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
            }
            reloadData();
        });

    }

    private void updateToolbar(int position) {
        mNavigatorSubTitle.setText(mTitles[position] + " семестр");
    }

    protected void updateAdapter(Map<Integer, List<T>> data) {
        List<Integer> list = new ArrayList<>(data.keySet());
        Collections.sort(list);
        mTitles = list.toArray(new Integer[1]);

        mFragmentsAdapter = new FragmentsAdapter(getChildFragmentManager(), data);

        mViewPager.setAdapter(mFragmentsAdapter);
        int position = getArguments().getInt(KEY_POSITION);
        mViewPager.setCurrentItem(position);
        updateToolbar(position);
    }

    private class FragmentsAdapter extends FragmentStatePagerAdapter {

        private Integer[] keys;
        private Map<Integer, List<T>> mDataMap;
        private Map<Integer, Fragment> mMapFragments;

        public FragmentsAdapter(FragmentManager fm, Map<Integer, List<T>> data) {
            super(fm);
            mDataMap = data;
            keys = mTitles;
            mMapFragments = new HashMap<>();

            Log.i(TAG, "FragmentsAdapter: " + Arrays.toString(keys));
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem: " + position + " CurrentItem: " + keys[position]);
            Fragment fragment = getItemFragment(mDataMap.get(keys[position]));
            mMapFragments.put(position, fragment);

            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mMapFragments.remove(position);
        }

        @Override
        public int getCount() {
            return keys.length;
        }

        public Fragment getFragment(int position) {
            return mMapFragments.get(position);
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
            int position = mViewPager.getCurrentItem();
            if ((position + move != -1) && (position + move != mTitles.length)) {
                position += move;
            }
            mViewPager.setCurrentItem(position, true);
        }
    }

    private class OnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected: " + position);
            mLastPosition = position;
            getArguments().putInt(KEY_POSITION, mLastPosition);
            updateToolbar(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
