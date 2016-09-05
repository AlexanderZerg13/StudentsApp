package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;

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

public class ScheduleDayFragment extends Fragment implements MainContentActivity.IGroupLoad {

    private static final String TAG = "ScheduleDayFragment";

    private static final String ADDRESS_TIMETABLE = "http://web-03:8080/InfoBase-Stud/hs/Students/TimeTable";

    private IToolbar mToolbarActivity;
    private ITransitionActions mITransitionActions;

    private ImageView mNavigatorPriorImageButton;
    private ImageView mNavigatorNextImageButton;
    private TextView mNavigatorSubTitle;
    private TextView mNavigatorTitle;

    private ScheduleLessonsViewGroup mScheduleLessonsViewGroup;
    private ScrollView mScrollView;
    private ProgressBar mProgressBar;

    private Date mCurrentDate;
    private SimpleDateFormat mSimpleDateFormatTitle;
    private SimpleDateFormat mSimpleDateFormatSubTitle;
    private SimpleDateFormat mSimpleDateFormatRequest;
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
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mSimpleDateFormatTitle = new SimpleDateFormat("EEEE", new Locale("ru"));
        mSimpleDateFormatSubTitle = new SimpleDateFormat("dd.MM", new Locale("ru"));
        mSimpleDateFormatRequest = new SimpleDateFormat("yyyyMMdd", new Locale("ru"));
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(2013, 9, 7);
        mCurrentDate = calendar.getTime();
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
//        mScheduleViewGroup.addLessons(StaticData.sLessons, new CardClickListener());
        mScheduleLessonsViewGroup.setIsSession(true, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITransitionActions.goToSession();
            }
        });

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
    public void onGroupLoad(StudentGroup studentGroup) {
        mStudentGroupIdentifier = studentGroup.getIdentifier();
        new FetchScheduleDay().execute(mStudentGroupIdentifier, mSimpleDateFormatRequest.format(mCurrentDate));
    }

    private class FetchScheduleDay extends AsyncTask<String, Void, Boolean> {

        private final int STATE_INTERNET_NOT_AVAILABLE = 0;
        private final int STATE_INTERNET_AVAILABLE = 1;
        private final int STATE_INTERNET_AVAILABLE_ERROR = 2;

        private int currentState;
        private String currentDate;

        @Override
        protected void onPreExecute() {
            currentState = STATE_INTERNET_NOT_AVAILABLE;
            if (FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                currentState = STATE_INTERNET_AVAILABLE;
                mProgressBar.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String objectId = strings[0];
            String date = strings[1];
            currentDate = date;

            Log.i(TAG, "doInBackground: " + date);

            if (currentState == STATE_INTERNET_NOT_AVAILABLE) {
                return false;
            }

            try{
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("objectType", "group"));
                params.add(new Pair<>("objectId", objectId));
                params.add(new Pair<>("scheduleType", "day"));
                params.add(new Pair<>("scheduleStartDate", date));
                params.add(new Pair<>("scheduleEndDate", date));

                byte[] bytes = FetchUtils.doPostRequest(LoginAuthFragment.LOGIN, LoginAuthFragment.PASS, ADDRESS_TIMETABLE, params);
                Log.i(TAG, "doInBackground: " + new String(bytes));

                List<Lesson> list = Utils.parseLessons(new ByteArrayInputStream(bytes), date);
                for (Lesson lesson: list) {
                    Log.i(TAG, "doInBackground: " + lesson);
                }

                long count = LessonLab.get(getActivity()).addLesson(list, date);

                if (count == 0) {
                    return false;
                }

                return true;
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                currentState = STATE_INTERNET_AVAILABLE_ERROR;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);

            switch (currentState) {
                case STATE_INTERNET_AVAILABLE:
                    Toast.makeText(getActivity(), "Internet available", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_INTERNET_NOT_AVAILABLE:
                    Toast.makeText(getActivity(), "Internet no available", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_INTERNET_AVAILABLE_ERROR:
                    Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
                    break;
            }
            if (result) {
                Toast.makeText(getActivity(), "Take new data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Take old data", Toast.LENGTH_SHORT).show();
            }

            List<Lesson> list = LessonLab.get(getActivity()).getLessons(currentDate);
            if (list != null && list.size() > 0) {
                mScheduleLessonsViewGroup.addLessons(list, new CardClickListener());
            }

        }
    }

    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            List<Lesson> lessons = getTestListLessons();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(mCurrentDate);
            switch (view.getId()) {
                case R.id.toolbar_navigator_btn_prior:
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
//                    mScheduleLessonsViewGroup.addLessons(lessons, new CardClickListener());
                    break;
                case R.id.toolbar_navigator_btn_next:
                    calendar.add(Calendar.DAY_OF_MONTH, 1);

//                    mScheduleLessonsViewGroup.addLessons(lessons, new CardClickListener());
                    break;
            }
            mCurrentDate = calendar.getTime();
            mNavigatorSubTitle.setText(mSimpleDateFormatSubTitle.format(mCurrentDate) + ", чётная неделя");
            mNavigatorTitle.setText(Utils.capitalizeFirstLetter(mSimpleDateFormatTitle.format(mCurrentDate)));
            if (!TextUtils.isEmpty(mStudentGroupIdentifier)) {
                new FetchScheduleDay().execute(mStudentGroupIdentifier, mSimpleDateFormatRequest.format(mCurrentDate));
            }
        }

        private List<Lesson> getTestListLessons() {
            int count = 5;
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

    private class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getTag() instanceof Lesson) {
                Lesson lesson = (Lesson) view.getTag();
                for (int i = 0; i < StaticData.sLessons.size(); i++) {
                    if (lesson.equals(StaticData.sLessons.get(i))) {
                        mITransitionActions.goToDescribeLessons(i);
                        break;
                    }
                }
            }
        }
    }
}
