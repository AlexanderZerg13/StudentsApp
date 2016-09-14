package com.example.pilipenko.studentsapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;

import com.example.pilipenko.studentsapp.FetchUtils;
import com.example.pilipenko.studentsapp.LoginAuthFragment;
import com.example.pilipenko.studentsapp.Utils;
import com.example.pilipenko.studentsapp.data.Lesson;
import com.example.pilipenko.studentsapp.data.LessonLab;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchDataIntentService extends IntentService {

    private static final String TAG = "FetchDataIntentService";

    public static final String BROADCAST_ACTION = "pilipenko.studentsapp.service.FetchDataIntentService.BROADCAST";
    private static final String ADDRESS_TIMETABLE = "http://web-03:8080/InfoBase-Stud/hs/Students/TimeTable";
    private static final String ACTION_SCHEDULE_DAY = "pilipenko.studentsapp.service.SCHEDULE_DAY";

    public static final String KEY_EXTRA_DATE = "extra_date";
    public static final String KEY_EXTRA_GROUP = "extra_group";

    public static final String KEY_EXTRA_STATUS = "extra_status";


    public static Intent newIntentFetchSchedule(Context context, String date, String group) {
        Intent intent = new Intent(context, FetchDataIntentService.class);
        intent.setAction(ACTION_SCHEDULE_DAY);
        intent.putExtra(KEY_EXTRA_DATE, date);
        intent.putExtra(KEY_EXTRA_GROUP, group);

        return intent;
    }

    public FetchDataIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        switch (intent.getAction()) {
            case ACTION_SCHEDULE_DAY:
                Intent resultIntent = performFetchScheduleDay(intent);
                LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
                break;
            default:
                throw new IllegalStateException("Unknown action");
        }
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
        if (hasInternet) {
            try {
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("objectType", "group"));
                params.add(new Pair<>("objectId", group));
                params.add(new Pair<>("scheduleType", "day"));
                params.add(new Pair<>("scheduleStartDate", date));
                params.add(new Pair<>("scheduleEndDate", date));

                byte[] bytes = FetchUtils.doPostRequest(LoginAuthFragment.LOGIN, LoginAuthFragment.PASS, ADDRESS_TIMETABLE, params);
                Log.i(TAG, "doInBackground: " + new String(bytes));

                newList = Utils.parseLessons(new ByteArrayInputStream(bytes), date);
                for (Lesson lesson : newList) {
                    Log.i(TAG, "doInBackground: " + lesson);
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
}
