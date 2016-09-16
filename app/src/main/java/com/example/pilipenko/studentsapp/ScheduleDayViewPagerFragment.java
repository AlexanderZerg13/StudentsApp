package com.example.pilipenko.studentsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.MainContentActivity.IFragmentReceiver;
import com.example.pilipenko.studentsapp.data.Lesson;
import com.example.pilipenko.studentsapp.data.LessonLab;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;
import com.example.pilipenko.studentsapp.service.FetchDataIntentService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ScheduleDayViewPagerFragment extends Fragment implements IFragmentReceiver {

    private static final String TAG = "SDViewPagerFragment";

    private static final String DIALOG_DATE = "DialogDate";
    private static final String KEY_LAST_INDEX = "LAST_INDEX";

    private static final int REQUEST_DATE = 0;

    private static final int VIEW_PAGER_PAGE_COUNT = 211;

    private IToolbar mToolbarActivity;
    private ITransitionActions mITransitionActions;

    private ImageView mNavigatorPriorImageButton;
    private ImageView mNavigatorNextImageButton;
    private TextView mNavigatorSubTitle;
    private TextView mNavigatorTitle;

    private ViewPager mScheduleViewPager;
    private ScheduleDayFragmentsAdapter mScheduleDayFragmentsAdapter;
    private ScheduleOnPageChangeListener mScheduleOnPageChangeListener;

    private static SimpleDateFormat mSimpleDateFormatTitle = new SimpleDateFormat("EEEE", new Locale("ru"));

    private static SimpleDateFormat mSimpleDateFormatSubTitle = new SimpleDateFormat("dd.MM", new Locale("ru"));
    private static SimpleDateFormat mSimpleDateFormatRequest = new SimpleDateFormat("yyyyMMdd", new Locale("ru"));
    private Date mCurrentDate;
    private String mStudentGroupIdentifier;
    private int mLastPosition;

    public static ScheduleDayViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleDayViewPagerFragment fragment = new ScheduleDayViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i(TAG, "onCreate: ");
        

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(2013, 9, 7);
        mCurrentDate = calendar.getTime();

        mLastPosition = VIEW_PAGER_PAGE_COUNT / 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_schedule_view_pager_day, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_schedule_view_pager_day_toolbar);
        mToolbarActivity.useToolbar(toolbar, 0);

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton = (ImageView) view.findViewById(R.id.toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageView) view.findViewById(R.id.toolbar_navigator_btn_next);
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_title);
        mNavigatorSubTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_sub_title);

        updateToolbar();

        mScheduleViewPager = (ViewPager) view.findViewById(R.id.fragment_schedule_view_pager_day_view_pager);
        mScheduleDayFragmentsAdapter = new ScheduleDayFragmentsAdapter(getChildFragmentManager(), VIEW_PAGER_PAGE_COUNT);
        mScheduleOnPageChangeListener = new ScheduleOnPageChangeListener();
        
        mScheduleViewPager.setAdapter(mScheduleDayFragmentsAdapter);
        mScheduleViewPager.setCurrentItem(VIEW_PAGER_PAGE_COUNT / 2);
        mScheduleViewPager.addOnPageChangeListener(mScheduleOnPageChangeListener);


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
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
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
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCurrentDate);
                dialog.setTargetFragment(ScheduleDayViewPagerFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            mCurrentDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mNavigatorTitle.setText(Utils.capitalizeFirstLetter(mSimpleDateFormatTitle.format(mCurrentDate)));
            mNavigatorSubTitle.setText(mSimpleDateFormatSubTitle.format(mCurrentDate) + ", чётная неделя");

//            getLoaderManager().restartLoader(0, null, ScheduleDayViewPagerFragment.this).forceLoad();
        }
    }

    //callback
    /*
    @Override
    public Loader<List<Lesson>> onCreateLoader(int id, Bundle args) {
        return new ScheduleDayCursorLoader(getActivity(), mCurrentDate);
    }

    @Override
    public void onLoadFinished(Loader<List<Lesson>> loader, List<Lesson> list) {

        Log.i(TAG, "onLoadFinished: ");
        
        if (list == null || list.size() == 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);

            Intent intent = FetchDataIntentService.newIntentFetchSchedule(
                    this.getContext(),
                    mSimpleDateFormatRequest.format(mCurrentDate),
                    StudentGroupLab.get(this.getContext()).getStudentGroups().get(0).getIdentifier());
            this.getContext().startService(intent);

            return;
        }

        if (!LessonLab.scheduleIsAbsent(list)) {
            mScheduleLessonsViewGroup.addLessons(list, new CardClickListener(), Utils.isToday(mCurrentDate));
        } else {
            mScheduleLessonsViewGroup.setIsInformation(true, getString(R.string.absentLessons), null, null);
        }

        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Lesson>> loader) {

    }
    */
    //--------


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        if (!intent.getAction().equals(FetchDataIntentService.BROADCAST_ACTION)) {
            return;
        }
        String date = intent.getStringExtra(FetchDataIntentService.KEY_EXTRA_DATE);

        Fragment fragment = mScheduleDayFragmentsAdapter.getMap().get(date);
        if (fragment != null && fragment.isAdded()) {
            if (!(fragment instanceof IFragmentReceiver)) {
                throw new IllegalStateException("Fragment must implement IFragmentReceiver");
            }

            IFragmentReceiver receiver = (IFragmentReceiver) fragment;
            receiver.onReceive(context, intent);
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

    /*
        private class FetchScheduleDay extends AsyncTask<String, Void, Boolean> {

            private final int STATE_INTERNET_IS_AVAILABLE = 1;
            private final int STATE_INTERNET_IS_NOT_AVAILABLE = 1 << 1;
            private final int STATE_INTERNET_ERROR = 1 << 2;
            private final int STATE_SET_OLD_DATA = 1 << 3;
            private final int STATE_NO_OLD_DATA = 1 << 4;

            private int currentState;

            private String mDate;
            private List<Lesson> mOldList;
            private List<Lesson> mNewList;

            public FetchScheduleDay(Date date) {
                mDate = mSimpleDateFormatRequest.format(date);
            }

            @Override
            protected void onPreExecute() {
                if (FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                    currentState = STATE_INTERNET_IS_AVAILABLE;
                } else {
                    currentState = STATE_INTERNET_IS_NOT_AVAILABLE;
                }

                mOldList = LessonLab.get(getActivity()).getLessons(mDate);

                if (mOldList != null && mOldList.size() > 0) {
                    if (!LessonLab.scheduleIsAbsent(mOldList)) {
                        mScheduleLessonsViewGroup.addLessons(mOldList, new CardClickListener(), Utils.isToday(mCurrentDate));
                    } else {
                        mScheduleLessonsViewGroup.setIsInformation(true, getString(R.string.absentLessons), null, null);
                    }
                    currentState |= STATE_SET_OLD_DATA;
                } else {
                    currentState |= STATE_NO_OLD_DATA;
                }

                if (currentState == (STATE_INTERNET_IS_AVAILABLE | STATE_NO_OLD_DATA)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                }
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                Log.i(TAG, "doInBackground: " + mDate);
                String objectId = strings[0];


                if ((currentState & STATE_INTERNET_IS_NOT_AVAILABLE) == STATE_INTERNET_IS_NOT_AVAILABLE) {
                    return false;
                }

                try {
                    List<Pair<String, String>> params = new ArrayList<>();
                    params.add(new Pair<>("objectType", "group"));
                    params.add(new Pair<>("objectId", objectId));
                    params.add(new Pair<>("scheduleType", "day"));
                    params.add(new Pair<>("scheduleStartDate", mDate));
                    params.add(new Pair<>("scheduleEndDate", mDate));

                    byte[] bytes = FetchUtils.doPostRequest(LoginAuthFragment.LOGIN, LoginAuthFragment.PASS, ADDRESS_TIMETABLE, params);
                    Log.i(TAG, "doInBackground: " + new String(bytes));

                    mNewList = Utils.parseLessons(new ByteArrayInputStream(bytes), mDate);
                    for (Lesson lesson : mNewList) {
                        Log.i(TAG, "doInBackground: " + lesson);
                    }
                    if (LessonLab.isEqualsList(mNewList, mOldList)) {
                        return false;
                    }

                    LessonLab lessonLab = LessonLab.get(getActivity());
                    lessonLab.addLesson(mNewList, mDate);
                    mNewList = lessonLab.getLessons(mDate);

                    return true;
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                    currentState |= STATE_INTERNET_ERROR;
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);

    //            Toast.makeText(getActivity(), "Result: " + result, Toast.LENGTH_SHORT).show();

                if (result) {
                    if (!LessonLab.scheduleIsAbsent(mNewList)) {
                        mScheduleLessonsViewGroup.addLessons(mNewList, new CardClickListener(), Utils.isToday(mCurrentDate));
                    } else {
                        mScheduleLessonsViewGroup.setIsInformation(true, getString(R.string.absentLessons), null, null);
                    }
                } else if ((currentState & STATE_SET_OLD_DATA) != STATE_SET_OLD_DATA) {
                    mScheduleLessonsViewGroup.setIsInformation(true, "Ошибка", getString(R.string.errorLessons),
                            getString(R.string.errorLessonsRefresh),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                                        Toast.makeText(getActivity(), "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (!TextUtils.isEmpty(mStudentGroupIdentifier)) {
                                        new FetchScheduleDay(mCurrentDate).execute(mStudentGroupIdentifier);
                                    }
                                }
                            });
                }

            }
        }
    */
    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int move = 0;
            switch (view.getId()) {
                case R.id.toolbar_navigator_btn_prior:
                    move = -1;
                    break;
                case R.id.toolbar_navigator_btn_next:
                    move = 1;
                    break;
            }

            int position = mScheduleViewPager.getCurrentItem() + move;
            mScheduleOnPageChangeListener.onPageSelected(position);
            mScheduleViewPager.setCurrentItem(position, true);
        }

    }

    private void updateToolbar() {
        mNavigatorSubTitle.setText(mSimpleDateFormatSubTitle.format(mCurrentDate) + ", чётная неделя");
        mNavigatorTitle.setText(Utils.capitalizeFirstLetter(mSimpleDateFormatTitle.format(mCurrentDate)));
    }

    private class ScheduleDayFragmentsAdapter extends FragmentStatePagerAdapter {

        private int count;
        private Map<String, Fragment> map;

        public ScheduleDayFragmentsAdapter(FragmentManager fm, int k) {
            super(fm);
            count = k;
            map = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem: " + position + " CurrentItem: " + mScheduleViewPager.getCurrentItem());


            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(mCurrentDate);

            calendar.add(Calendar.DAY_OF_MONTH, position - mScheduleViewPager.getCurrentItem());
            Fragment scheduleDayFragment = ScheduleDayFragment.newInstance(calendar.getTime());
//              зачем???
//            scheduleDayFragment.setFragmentDate(calendar.getTime());
            map.put(mSimpleDateFormatRequest.format(calendar.getTime()), scheduleDayFragment);

            return scheduleDayFragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            Calendar cal = Calendar.getInstance();
            cal.setTime(mCurrentDate);
            cal.add(Calendar.DAY_OF_MONTH, position - mScheduleViewPager.getCurrentItem());
            map.remove(mSimpleDateFormatRequest.format(cal.getTime()));
        }

        public Map<String, Fragment> getMap() {
            return map;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    private class ScheduleOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected: " + position + " " + mLastPosition);
            if (position == mLastPosition) {
                return;
            }
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(mCurrentDate);

            calendar.add(Calendar.DAY_OF_MONTH, position - mLastPosition);
            mLastPosition = position;
//            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(KEY_LAST_INDEX, lastPosition).apply();
            mCurrentDate = calendar.getTime();
            updateToolbar();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private List<Lesson> getTestListLessons() {
        int count = 4;
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
