package com.example.pilipenko.studentsapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import com.example.pilipenko.studentsapp.FetchUtils;
import com.example.pilipenko.studentsapp.LoginAuthFragment;
import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.Utils;
import com.example.pilipenko.studentsapp.data.Lesson;
import com.example.pilipenko.studentsapp.data.LessonLab;
import com.example.pilipenko.studentsapp.data.LessonPlan;
import com.example.pilipenko.studentsapp.data.LessonPlanLab;
import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.data.LessonProgressLab;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchDataIntentService extends IntentService {

    private static final String TAG = "FetchDataIntentService";

    public static final String BROADCAST_ACTION = "pilipenko.studentsapp.service.FetchDataIntentService.BROADCAST";

    private static final String ADDRESS_SCHEDULE_DAY = "/Students/TimeTable";
    private static final String ADDRESS_LESSONS_PROGRESS = "/Students/EducationalPerformance";
    private static final String ADDRESS_LESSONS_PLAN = "/StudentsPlan/PlanLoad/PlanLoad";

    public static final String ACTION_SCHEDULE_DAY = "pilipenko.studentsapp.service.SCHEDULE_DAY";
    public static final String ACTION_LESSONS_PROGRESS = "pilipenko.studentsapp.service.LESSONS_PROGRESS";
    public static final String ACTION_LESSONS_PLAN = "pilipenko.studentsapp.service.LESSONS_PLAN";

    public static final String KEY_EXTRA_DATE = "extra_date";
    public static final String KEY_EXTRA_GROUP = "extra_group";

    public static final String KEY_EXTRA_USER_ID = "extra_user_id";

    public static final String KEY_EXTRA_ACADEMIC_PLAN_ID = "extra_academic_plan_id";

    public static final String KEY_EXTRA_STATUS = "extra_status";
    public static final String KEY_EXTRA_ACTION = "extra_action";

    private Uri mHost;

    public static Intent newIntentFetchSchedule(Context context, String date, String group) {
        Intent intent = new Intent(context, FetchDataIntentService.class);
        intent.setAction(ACTION_SCHEDULE_DAY);
        intent.putExtra(KEY_EXTRA_DATE, date);
        intent.putExtra(KEY_EXTRA_GROUP, group);

        return intent;
    }

    public static Intent newIntentFetchLessonsProgress(Context context, String userId) {
        Intent intent = new Intent(context, FetchDataIntentService.class);
        intent.setAction(ACTION_LESSONS_PROGRESS);
        intent.putExtra(KEY_EXTRA_USER_ID, userId);

        return intent;
    }

    public static Intent newIntentFetchLessonsPlan(Context context, String academicPlanId) {
        Intent intent = new Intent(context, FetchDataIntentService.class);
        intent.setAction(ACTION_LESSONS_PLAN);
        intent.putExtra(KEY_EXTRA_ACADEMIC_PLAN_ID, academicPlanId);

        return intent;
    }

    public FetchDataIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        mHost = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.settings_key_host), getString(R.string.settings_default_host)));

        Intent resultIntent;
        switch (intent.getAction()) {
            case ACTION_SCHEDULE_DAY:
                resultIntent = performFetchScheduleDay(intent);
                resultIntent.putExtra(KEY_EXTRA_ACTION, ACTION_SCHEDULE_DAY);
                break;
            case ACTION_LESSONS_PROGRESS:
                resultIntent = performFetchLessonsProgress(intent);
                resultIntent.putExtra(KEY_EXTRA_ACTION, ACTION_LESSONS_PROGRESS);
                break;
            case ACTION_LESSONS_PLAN:
                resultIntent = performFetchLessonPlan(intent);
                resultIntent.putExtra(KEY_EXTRA_ACTION, ACTION_LESSONS_PLAN);
                break;
            default:
                throw new IllegalStateException("Unknown action");
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    private Intent performFetchScheduleDay(Intent intent) {
        boolean hasInternet = true;
        Log.i(TAG, "performFetchScheduleDay: ");

        String date = intent.getStringExtra(KEY_EXTRA_DATE);
        String group = intent.getStringExtra(KEY_EXTRA_GROUP);

        List<Lesson> newList;
        Intent resultIntent = new Intent(BROADCAST_ACTION);

        if (!FetchUtils.isNetworkAvailableAndConnected(getApplicationContext())) {
            hasInternet = false;
        }

        resultIntent.putExtra(KEY_EXTRA_STATUS, false);
        resultIntent.putExtra(KEY_EXTRA_DATE, date);
        if (hasInternet) {
            try {
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("objectType", "group"));
                params.add(new Pair<>("objectId", group));
                params.add(new Pair<>("scheduleType", "day"));
                params.add(new Pair<>("scheduleStartDate", date));
                params.add(new Pair<>("scheduleEndDate", date));

                byte[] bytes = FetchUtils.doPostRequest(LoginAuthFragment.LOGIN, LoginAuthFragment.PASS, Uri.withAppendedPath(mHost, ADDRESS_SCHEDULE_DAY).toString(), params);
                Log.i(TAG, "performFetchScheduleDay: " + new String(bytes));

                newList = Utils.parseLessons(new ByteArrayInputStream(bytes), date);
                for (Lesson lesson : newList) {
                    Log.i(TAG, "performFetchScheduleDay: " + lesson);
                }

                LessonLab lessonLab = LessonLab.get(this);
                lessonLab.addLesson(newList, date);

                resultIntent.putExtra(KEY_EXTRA_STATUS, true);

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }


        return resultIntent;
    }

    private Intent performFetchLessonsProgress(Intent intent) {
        boolean hasInternet = true;
        Log.i(TAG, "performFetchLessonsProgress: ");

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
                Log.i(TAG, "performFetchLessonsProgress: " + new String(bytes));

                newList = Utils.parseLessonsProgress(new ByteArrayInputStream(bytes));
                for (LessonProgress lessonProgress : newList) {
                    Log.i(TAG, "performFetchLessonsProgress: " + lessonProgress);
                }

                LessonProgressLab lessonProgressLab = LessonProgressLab.get(this);
                lessonProgressLab.addLessonProgress(newList);

                resultIntent.putExtra(KEY_EXTRA_STATUS, true);
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
}
