package ru.infocom.university.modules.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import ru.infocom.university.DataPreferenceManager;
import ru.infocom.university.FetchUtils;
import ru.infocom.university.R;
import ru.infocom.university.StudentApplication;
import ru.infocom.university.Utils;
import ru.infocom.university.custom.ScheduleLessonsViewGroup;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.LessonLab;
import ru.infocom.university.interfaces.ITransitionActions;
import ru.infocom.university.model.RecordBook;
import ru.infocom.university.network.DataRepository;
import ru.infocom.university.network.EmptyDataException;
import ru.infocom.university.network.ExceptionUtils;
import rx.Subscription;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleDayFragment extends Fragment {

    private ScrollView mScrollView;
    private ProgressBar mProgressBar;
    protected ScheduleLessonsViewGroup mScheduleLessonsViewGroup;

    private ITransitionActions mITransitionActions;

    private static final String TAG = "ScheduleDayFragment";

    protected static final String KEY_EXTRA_DATE = "EXTRA_DATE";

    protected Date mFragmentDate;
    protected DataRepository mDataRepository;
    protected Subscription mGetScheduleSubscription;

    public static ScheduleDayFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_EXTRA_DATE, date);

        ScheduleDayFragment fragment = new ScheduleDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.i(TAG, "onCreate: ");

        mFragmentDate = (Date) getArguments().getSerializable(KEY_EXTRA_DATE);
        mDataRepository = DataRepository.get(DataPreferenceManager.provideUserPreferences().getUniversityId(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onDestroy() {
        if (mGetScheduleSubscription != null) {
            mGetScheduleSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_schedule_day, container, false);

        mScrollView = view.findViewById(R.id.fragment_schedule_day_scroll_view);
        mProgressBar = view.findViewById(R.id.fragment_schedule_day_progress_bar);
        mScheduleLessonsViewGroup = view.findViewById(R.id.fragment_schedule_day_schedule_view_group);

        initLoading();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mITransitionActions = (ITransitionActions) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mGetScheduleSubscription != null) {
            mGetScheduleSubscription.unsubscribe();
        }
        mITransitionActions = null;
    }

    public void initLoading() {
        AuthorizationObject authorizationObject =
                DataPreferenceManager.provideUserPreferences().getUser(this.getContext());
        RecordBook recordBook = ((StudentApplication) getActivity().getApplication()).getRecordBookSelected();

        String scheduleObjectType = authorizationObject.getRole() == AuthorizationObject.Role.TEACHER ? "Teacher" : "AcademicGroup";
        String scheduleObjectId = authorizationObject.getRole() == AuthorizationObject.Role.TEACHER ?
                authorizationObject.getId() :
                recordBook.getGroupId();

        mGetScheduleSubscription = mDataRepository
                .getSchedule(scheduleObjectType, scheduleObjectId, mFragmentDate)
                .doOnSubscribe(this::showLoading)
                .doOnTerminate(this::hideLoading)
                .subscribe(lessons -> {
                    Log.i(TAG, "initLoading okHttp: " + SimpleDateFormat.getDateInstance().format(mFragmentDate) + " " + lessons);

                    boolean isTeacher = DataPreferenceManager.provideUserPreferences().getUser(getContext()).getRole().equals(AuthorizationObject.Role.TEACHER);
                    mScheduleLessonsViewGroup.addLessons(lessons, new CardClickListener(), Utils.isToday(mFragmentDate), isTeacher);
                }, throwable -> {
                    Log.i(TAG, "initLoading Error: " + throwable.getMessage());

                    if (throwable instanceof EmptyDataException) {
                        mScheduleLessonsViewGroup.setIsInformation(true, getString(R.string.schedule_no_data), null, null);
                    } else {
                        showErrorNetwork(ExceptionUtils.getErrorText(throwable, getResources()));

                        if (!FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                            Toast.makeText(getActivity(), "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
    }

    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    protected class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getTag() instanceof Lesson) {
                Lesson lesson = (Lesson) view.getTag();
                mITransitionActions.goToDescribeLessons(lesson);
            }
        }
    }

    protected void showErrorNetwork(String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
        mScheduleLessonsViewGroup.setIsInformation(true, "Ошибка", errorMessage,
                getString(R.string.errorLessonsRefresh),
                view -> initLoading());
    }
}
