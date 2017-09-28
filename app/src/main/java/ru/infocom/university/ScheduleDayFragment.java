package ru.infocom.university;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import ru.infocom.university.custom.ScheduleLessonsViewGroup;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.LessonLab;
import ru.infocom.university.data.StudentGroupLab;
import ru.infocom.university.interfaces.ITransitionActions;
import ru.infocom.university.service.FetchDataIntentService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleDayFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Lesson>>, MainContentActivity.IFragmentReceiver {

    private ScrollView mScrollView;
    private ProgressBar mProgressBar;
    private ScheduleLessonsViewGroup mScheduleLessonsViewGroup;

    private ITransitionActions mITransitionActions;

    private static final String TAG = "ScheduleDayFragment";

    private static SimpleDateFormat mSimpleDateFormatRequest = new SimpleDateFormat("yyyyMMdd", new Locale("ru"));

    private static final String KEY_EXTRA_DATE = "EXTRA_DATE";

    private Date mFragmentDate;

    private boolean oneTimeRefreshData = false;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        getLoaderManager().initLoader(0, null, ScheduleDayFragment.this);
        getLoaderManager().getLoader(0).forceLoad();
//        Handler handler = getActivity().getWindow().getDecorView().getHandler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                // initialize the loader here!
//                getLoaderManager().initLoader(0, null, ScheduleDayFragment.this);
//                getLoaderManager().getLoader(0).forceLoad();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_schedule_day, container, false);

        mScrollView = (ScrollView) view.findViewById(R.id.fragment_schedule_day_scroll_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_schedule_day_progress_bar);
        mScheduleLessonsViewGroup = (ScheduleLessonsViewGroup) view.findViewById(R.id.fragment_schedule_day_schedule_view_group);
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
        mITransitionActions = null;
    }

    @Override
    public Loader<List<Lesson>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader: ");
        return new ScheduleDayAsyncTaskLoader(getActivity(), mFragmentDate);
    }

    @Override
    public void onLoadFinished(Loader<List<Lesson>> loader, List<Lesson> list) {
        Log.i(TAG, "onLoadFinished: " + loader.hashCode());

        //17.02.2014
        Log.i(TAG, "onLoadFinished: " + (list == null || list.size() == 0));

        Intent intent;
        AuthorizationObject object = DataPreferenceManager.getUser(this.getContext());
        System.out.println(object);
        switch (object.getRole()) {
            case STUDENT:
                intent = FetchDataIntentService.newIntentFetchScheduleStudent(
                        this.getContext(),
                        mSimpleDateFormatRequest.format(mFragmentDate),
                        StudentGroupLab.get(this.getContext()).getStudentGroups().get(0).getIdentifier());
                break;
            case TEACHER:
                intent = FetchDataIntentService.newIntentFetchScheduleTeacher(
                        this.getContext(),
                        mSimpleDateFormatRequest.format(mFragmentDate),
                        object.getId());
                break;
            default:
                throw new IllegalStateException("Role must be a student or a teacher");
        }

        if (list == null || list.size() == 0) {

            if(!FetchUtils.isNetworkAvailableAndConnected(getContext())) {
                showErrorNetwork();
                return;
            }

            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);

            this.getContext().startService(intent);

            return;
        } else if(FetchUtils.isNetworkAvailableAndConnected(getContext()) && !oneTimeRefreshData) {
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);

            this.getContext().startService(intent);
            return;
        }

        if (!LessonLab.scheduleIsAbsent(list)) {
            boolean isTeacher = DataPreferenceManager.getUser(getContext()).getRole().equals(AuthorizationObject.Role.TEACHER);
            mScheduleLessonsViewGroup.addLessons(list, new CardClickListener(), Utils.isToday(mFragmentDate), isTeacher);
        } else {
            mScheduleLessonsViewGroup.setIsInformation(true, getString(R.string.absentLessons), null, null);
        }

        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Lesson>> loader) {

    }


    private static class ScheduleDayAsyncTaskLoader extends AsyncTaskLoader<List<Lesson>> {

        private String mLoaderDate;


        public ScheduleDayAsyncTaskLoader(Context context, Date loadDate) {
            super(context);
            mLoaderDate = mSimpleDateFormatRequest.format(loadDate);
        }

        @Override
        public List<Lesson> loadInBackground() {
            Log.i(TAG, "loadInBackground: ");
            List<Lesson> list;
            LessonLab lessonLab = LessonLab.get(getContext());
            list = lessonLab.getLessons(mLoaderDate);

            return list;
        }
    }

    private class CardClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getTag() instanceof Lesson) {
                Lesson lesson = (Lesson) view.getTag();
                mITransitionActions.goToDescribeLessons(lesson.getId());
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        boolean result = intent.getBooleanExtra(FetchDataIntentService.KEY_EXTRA_STATUS, false);

        if (result) {
            getLoaderManager().getLoader(0).forceLoad();
            oneTimeRefreshData = true;
        } else {
            showErrorNetwork();
        }
    }

    private void showErrorNetwork() {
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
