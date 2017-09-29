package ru.infocom.university.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import ru.infocom.university.FetchUtils;
import ru.infocom.university.LoginAuthFragment;
import ru.infocom.university.Utils;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.LessonLab;
import ru.infocom.university.data.LessonPlan;
import ru.infocom.university.data.LessonPlanLab;
import ru.infocom.university.data.LessonProgress;
import ru.infocom.university.data.LessonProgressLab;
import ru.infocom.university.data.University;
import ru.infocom.university.data.UniversityLab;
import ru.infocom.university.mock.LessonPlanMock;
import ru.infocom.university.mock.LessonProgressMock;
import ru.infocom.university.mock.ScheduleMock;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FetchDataIntentService extends IntentService {

    private static final String TAG = "FetchDataIntentService";

    public static final String BROADCAST_ACTION = "pilipenko.studentsapp.service.FetchDataIntentService.BROADCAST";

    private static final String ADDRESS_LESSONS_PROGRESS = "Students/EducationalPerformance";
    private static final String ADDRESS_LESSONS_PLAN = "StudentsPlan/PlanLoad/PlanLoad";
    private static final String ADDRESS_UNIVERSITIES_LIST = "university.xml";

    public static final String ACTION_LESSONS_PROGRESS = "pilipenko.studentsapp.service.LESSONS_PROGRESS";
    public static final String ACTION_LESSONS_PLAN = "pilipenko.studentsapp.service.LESSONS_PLAN";
    public static final String ACTION_UNIVERSITIES = "pilipenko.studentsapp.service.UNIVERSITIES";


    public static final String KEY_EXTRA_USER_ID = "extra_user_id";

    public static final String KEY_EXTRA_ACADEMIC_PLAN_ID = "extra_academic_plan_id";

    public static final String KEY_EXTRA_STATUS = "extra_status";
    public static final String KEY_EXTRA_ACTION = "extra_action";

    private Uri mHost;

    public static Intent newIntentFetchLessonsProgress(Context context, String userId) {
        Intent intent = new Intent(context, FetchDataIntentService.class);
        intent.setAction(ACTION_LESSONS_PROGRESS);
        intent.putExtra(KEY_EXTRA_USER_ID, userId);

        return intent;
    }

    public static Intent newIntentFetchUniversityList(Context context) {
        Intent intent = new Intent(context, FetchDataIntentService.class);
        intent.setAction(ACTION_UNIVERSITIES);

        return intent;
    }

    public FetchDataIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        if (!intent.getAction().equals(ACTION_UNIVERSITIES)) {
            mHost = Uri.parse(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString(getString(ru.infocom.university.R.string.settings_key_host), getString(ru.infocom.university.R.string.settings_default_host)));
        } else {
            mHost = Uri.parse(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString(getString(ru.infocom.university.R.string.settings_key_host_university), getString(ru.infocom.university.R.string.settings_default_host_university)));
        }
        System.out.println(mHost);

        Intent resultIntent;
        switch (intent.getAction()) {
            case ACTION_LESSONS_PROGRESS:
                resultIntent = performFetchLessonsProgress(intent);
                resultIntent.putExtra(KEY_EXTRA_ACTION, ACTION_LESSONS_PROGRESS);
                break;
            case ACTION_LESSONS_PLAN:
                resultIntent = performFetchLessonPlan(intent);
                resultIntent.putExtra(KEY_EXTRA_ACTION, ACTION_LESSONS_PLAN);
                break;
            case ACTION_UNIVERSITIES:
                resultIntent = performFetchUniversities(intent);
                resultIntent.putExtra(KEY_EXTRA_ACTION, ACTION_UNIVERSITIES);
                break;
            default:
                throw new IllegalStateException("Unknown action " + intent.getAction());
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    private Intent performFetchLessonsProgress(Intent intent) {
        boolean hasInternet = true;
        Log.i(TAG, "performFetchLessonsProgress start ");

        String userId = intent.getStringExtra(KEY_EXTRA_USER_ID);

        List<LessonProgress> newList;
        Intent resultIntent = new Intent(BROADCAST_ACTION);

        if (!FetchUtils.isNetworkAvailableAndConnected(getApplicationContext())) {
            hasInternet = false;
        }

        resultIntent.putExtra(KEY_EXTRA_STATUS, false);
        if (hasInternet) {
            try {
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("userId", userId));

                byte[] bytes = FetchUtils.doPostRequest(LoginAuthFragment.LOGIN, LoginAuthFragment.PASS, Uri.withAppendedPath(mHost, ADDRESS_LESSONS_PROGRESS).toString(), params);
                Log.i(TAG, "performFetchLessonsProgress Bytes: " + new String(bytes));

                newList = Utils.parseLessonsProgress(new ByteArrayInputStream(bytes));
//                newList = LessonProgressMock.get();
                for (LessonProgress lessonProgress : newList) {
                    Log.i(TAG, "performFetchLessonsProgress List: " + lessonProgress);
                }

                LessonProgressLab lessonProgressLab = LessonProgressLab.get(this);
                lessonProgressLab.addLessonProgress(newList);

                resultIntent.putExtra(KEY_EXTRA_STATUS, true);
            } catch (IllegalArgumentException e) {
                resultIntent.putExtra(KEY_EXTRA_STATUS, false);
            } catch (IOException | XmlPullParserException | ParseException e) {
                e.printStackTrace();
            }
        }

        return resultIntent;

    }

    private Intent performFetchLessonPlan(Intent intent) {
        boolean hasInternet = true;
        Log.i(TAG, "performFetchLessonPlan: ");

        String academicPlanId = intent.getStringExtra(KEY_EXTRA_ACADEMIC_PLAN_ID);

        List<LessonPlan> newList;
        Intent resultIntent = new Intent(BROADCAST_ACTION);

        if (!FetchUtils.isNetworkAvailableAndConnected(getApplicationContext())) {
            hasInternet = false;
        }

        resultIntent.putExtra(KEY_EXTRA_STATUS, false);
        if (hasInternet) {
            try {
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("academic_plan_id", academicPlanId));

                byte[] bytes = FetchUtils.doPostRequest(LoginAuthFragment.LOGIN, LoginAuthFragment.PASS, Uri.withAppendedPath(mHost, ADDRESS_LESSONS_PLAN).toString(), params);
                Log.i(TAG, "performFetchLessonPlan: " + new String(bytes));

                newList = Utils.parseLessonsPlan(new ByteArrayInputStream(bytes));
//                newList = LessonPlanMock.get();
                for (LessonPlan plan : newList) {
                    Log.i(TAG, "performFetchLessonPlan: " + plan);
                }

                LessonPlanLab lessonPlanLab = LessonPlanLab.get(this);
                lessonPlanLab.addLessonPlan(newList);

                resultIntent.putExtra(KEY_EXTRA_STATUS, true);
            } catch (IOException | XmlPullParserException | ParseException e) {
                e.printStackTrace();
            }
        }

        return resultIntent;
    }

    private Intent performFetchUniversities(Intent intent) {
        boolean hasInternet = true;
        Log.i(TAG, "performFetchUniversities: ");

        List<University> newList;
        Intent resultIntent = new Intent(BROADCAST_ACTION);

        if (!FetchUtils.isNetworkAvailableAndConnected(getApplicationContext())) {
            hasInternet = false;
        }

        resultIntent.putExtra(KEY_EXTRA_STATUS, false);
        if (hasInternet) {
            try {
                byte[] bytes = FetchUtils.doGetRequest(Uri.withAppendedPath(mHost, ADDRESS_UNIVERSITIES_LIST).toString());
                Log.i(TAG, "performFetchUniversities: " + new String(bytes));

                newList = Utils.parseUniversityList(new ByteArrayInputStream(bytes));
                for (University university: newList) {
                    Log.i(TAG, "performFetchUniversities: " + university);
                }

                if (newList.get(0) != null) {
                    newList.remove(0);
                }
                UniversityLab universityLab = UniversityLab.get(this);
                universityLab.addUniversity(newList);

                resultIntent.putExtra(KEY_EXTRA_STATUS, true);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        return resultIntent;
    }
}
