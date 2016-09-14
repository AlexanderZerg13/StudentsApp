package com.example.pilipenko.studentsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilipenko.studentsapp.custom.ScheduleLessonsViewGroup;
import com.example.pilipenko.studentsapp.data.Lesson;
import com.example.pilipenko.studentsapp.data.LessonLab;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.data.StudentGroup;
import com.example.pilipenko.studentsapp.data.StudentGroupLab;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;
import com.example.pilipenko.studentsapp.service.FetchDataIntentService;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ScheduleDayFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Lesson>>, MainContentActivity.IFragmentReceiver{

    private static final String TAG = "ScheduleDayFragment";

    private static final String ADDRESS_TIMETABLE = "http://web-03:8080/InfoBase-Stud/hs/Students/TimeTable";

    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private IToolbar mToolbarActivity;
    private ITransitionActions mITransitionActions;

    private ImageView mNavigatorPriorImageButton;
    private ImageView mNavigatorNextImageButton;
    private TextView mNavigatorSubTitle;
    private TextView mNavigatorTitle;

    private ScheduleLessonsViewGroup mScheduleLessonsViewGroup;
    private ScrollView mScrollView;
    private ProgressBar mProgressBar;

    private static SimpleDateFormat mSimpleDateFormatTitle = new SimpleDateFormat("EEEE", new Locale("ru"));;
    private static SimpleDateFormat mSimpleDateFormatSubTitle = new SimpleDateFormat("dd.MM", new Locale("ru"));
    private static SimpleDateFormat mSimpleDateFormatRequest = new SimpleDateFormat("yyyyMMdd", new Locale("ru"));
    private Date mCurrentDate;
    private String mStudentGroupIdentifier;

    public static ScheduleDayFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleDayFragment fragment = new ScheduleDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(2013, 9, 7);
        mCurrentDate = calendar.getTime();

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_day, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_schedule_day_toolbar);
        mToolbarActivity.useToolbar(toolbar, 0);

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton = (ImageView) view.findViewById(R.id.toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageView) view.findViewById(R.id.toolbar_navigator_btn_next);
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_title);
        mNavigatorSubTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_sub_title);

        mNavigatorTitle.setText(Utils.capitalizeFirstLetter(mSimpleDateFormatTitle.format(mCurrentDate)));
        mNavigatorSubTitle.setText(mSimpleDateFormatSubTitle.format(mCurrentDate) + ", чётная неделя");

        mScheduleLessonsViewGroup = (ScheduleLessonsViewGroup) view.findViewById(R.id.fragment_schedule_day_schedule_view_group);
        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_schedule_day_progress_bar);
        mScrollView = (ScrollView) view.findViewById(R.id.fragment_schedule_day_scroll_view);

        getLoaderManager().getLoader(0).forceLoad();

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
                dialog.setTargetFragment(ScheduleDayFragment.this, REQUEST_DATE);
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

            getLoaderManager().restartLoader(0, null, ScheduleDayFragment.this).forceLoad();
        }
    }

    //callback
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
    //--------


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(FetchDataIntentService.BROADCAST_ACTION)) {
            return;
        }

        boolean result = intent.getBooleanExtra(FetchDataIntentService.KEY_EXTRA_STATUS, false);
        if (result) {
            getLoaderManager().getLoader(0).forceLoad();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            mScheduleLessonsViewGroup.setIsInformation(true, "Ошибка", getString(R.string.errorLessons),
                    getString(R.string.errorLessonsRefresh),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                                Toast.makeText(getActivity(), "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            getLoaderManager().restartLoader(0, null, ScheduleDayFragment.this).forceLoad();
                        }
                    });
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

    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(mCurrentDate);
            switch (view.getId()) {
                case R.id.toolbar_navigator_btn_prior:
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    break;
                case R.id.toolbar_navigator_btn_next:
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    break;
            }
            mCurrentDate = calendar.getTime();
            mNavigatorSubTitle.setText(mSimpleDateFormatSubTitle.format(mCurrentDate) + ", чётная неделя");
            mNavigatorTitle.setText(Utils.capitalizeFirstLetter(mSimpleDateFormatTitle.format(mCurrentDate)));

            getLoaderManager().restartLoader(0, null, ScheduleDayFragment.this).forceLoad();
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

    private class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getTag() instanceof Lesson) {
                Lesson lesson = (Lesson) view.getTag();
                Log.i(TAG, "onClick: " + lesson.getId());
                mITransitionActions.goToDescribeLessons(lesson.getId());
            }
        }
    }
}
