package ru.infocom.university;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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

import ru.infocom.university.MainContentActivity.IFragmentReceiver;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.StaticData;
import ru.infocom.university.interfaces.IToolbar;
import ru.infocom.university.service.FetchDataIntentService;

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
    private static final String TAG1 = "SDViewPagerFragmentTab";

    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private static final int VIEW_PAGER_PAGE_COUNT = 1660;

    private IToolbar mToolbarActivity;

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
    private int mLastPosition;

    public static ScheduleDayViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleDayViewPagerFragment fragment = new ScheduleDayViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i(TAG, "onCreate: ");


        Calendar calendar = GregorianCalendar.getInstance();
        //calendar.clear();
        //calendar.set(2013, 9, 7);
        mCurrentDate = calendar.getTime();

        mLastPosition = VIEW_PAGER_PAGE_COUNT / 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_schedule_day_view_pager, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_schedule_view_pager_day_toolbar);
        mToolbarActivity.useToolbar(toolbar, 0);

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton = (ImageView) view.findViewById(R.id.layout_toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageView) view.findViewById(R.id.toolbar_navigator_btn_next);
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorTitle = (TextView) view.findViewById(R.id.layout_toolbar_navigator_tv_title);
        mNavigatorSubTitle = (TextView) view.findViewById(R.id.layout_toolbar_navigator_tv_sub_title);

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
            Date returnDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            Log.i(TAG1, returnDate.toString());
            int move = Utils.differenceDays(returnDate, mCurrentDate);
            Log.i(TAG1, "date1: " + returnDate + "\ndate2: " + mCurrentDate + "\nonActivityResult: " + move);

            int position = mScheduleViewPager.getCurrentItem() + move;
            mScheduleOnPageChangeListener.onPageSelected(position);
            mScheduleViewPager.setCurrentItem(position, false);
            updateToolbar();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive: " + intent.getStringExtra(FetchDataIntentService.KEY_EXTRA_ACTION));
        if (!intent.getStringExtra(FetchDataIntentService.KEY_EXTRA_ACTION).equals(FetchDataIntentService.ACTION_SCHEDULE_DAY_STUDENT)
                && !intent.getStringExtra(FetchDataIntentService.KEY_EXTRA_ACTION).equals(FetchDataIntentService.ACTION_SCHEDULE_DAY_TEACHER)) {
            return;
        }
        String date = intent.getStringExtra(FetchDataIntentService.KEY_EXTRA_DATE);

        Log.i(TAG, "onReceive: ");
        Fragment fragment = mScheduleDayFragmentsAdapter.getMap().get(date);
        if (fragment != null && fragment.isAdded()) {
            if (!(fragment instanceof IFragmentReceiver)) {
                throw new IllegalStateException("Fragment must implement IFragmentReceiver");
            }

            IFragmentReceiver receiver = (IFragmentReceiver) fragment;
            receiver.onReceive(context, intent);
        }
    }

    private void updateToolbar() {
        mNavigatorSubTitle.setText(mSimpleDateFormatSubTitle.format(mCurrentDate));
        mNavigatorTitle.setText(Utils.capitalizeFirstLetter(mSimpleDateFormatTitle.format(mCurrentDate)));
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

            int position = mScheduleViewPager.getCurrentItem() + move;
            mScheduleOnPageChangeListener.onPageSelected(position);
            mScheduleViewPager.setCurrentItem(position, true);
        }
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
            Log.i(TAG1, "onPageSelected: " + position + " " + mLastPosition);
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
}
