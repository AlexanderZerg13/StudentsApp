package ru.infocom.university.modules.scheduleV1;

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

import ru.infocom.university.DataPreferenceManager;
import ru.infocom.university.DatePickerFragment;
import ru.infocom.university.R;
import ru.infocom.university.Utils;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.StaticData;
import ru.infocom.university.interfaces.IToolbar;

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

public class ScheduleDayViewPagerFragment extends Fragment {

    private static final String TAG = "SDViewPagerFragment";
    private static final String TAG1 = "SDViewPagerFragmentTab";

    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private static final int VIEW_PAGER_PAGE_COUNT = 6640;

    private IToolbar mToolbarActivity;

    private ImageView mNavigatorPriorImageButton;
    private ImageView mNavigatorNextImageButton;
    private TextView mNavigatorSubTitle;
    private TextView mNavigatorTitle;

    /*TODO Need to viewPager endless*/
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
        AuthorizationObject object = DataPreferenceManager.provideUserPreferences().getUser(getActivity());
        if (object.getId().equals("000000032")) {
            calendar.clear();
            calendar.set(2015, 8, 10);
        }
        mCurrentDate = calendar.getTime();

        mLastPosition = VIEW_PAGER_PAGE_COUNT / 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_schedule_day_view_pager, container, false);

        Toolbar toolbar = view.findViewById(R.id.fragment_schedule_view_pager_day_toolbar);
        mToolbarActivity.useToolbar(toolbar, 0);

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton = view.findViewById(R.id.layout_toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = view.findViewById(R.id.toolbar_navigator_btn_next);
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorTitle = view.findViewById(R.id.layout_toolbar_navigator_tv_title);
        mNavigatorSubTitle = view.findViewById(R.id.layout_toolbar_navigator_tv_sub_title);

        updateToolbar();

        mScheduleViewPager = view.findViewById(R.id.fragment_schedule_view_pager_day_view_pager);
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
        if (menu.findItem(R.id.schedule_day_menu_item_today) == null) {
            inflater.inflate(R.menu.schedule_day, menu);
        }
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

    protected Fragment newInstanceFragment(Date date) {
        return ScheduleDayFragment.newInstance(date);
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
            Fragment scheduleDayFragment = newInstanceFragment(calendar.getTime());
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
//            DataPreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(KEY_LAST_INDEX, lastPosition).apply();
            mCurrentDate = calendar.getTime();
            updateToolbar();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
