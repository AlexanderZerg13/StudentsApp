package com.example.pilipenko.studentsapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Date;

public class FetchDataIntentService extends IntentService {

    private static final String TAG = "FetchDataIntentService";

    public static final String BROADCAST_ACTION = "pilipenko.studentsapp.service.FetchDataIntentService.BROADCAST";
    private static final String ADDRESS_TIMETABLE = "http://web-03:8080/InfoBase-Stud/hs/Students/TimeTable";
    private static final String ACTION_SCHEDULE_DAY = "pilipenko.studentsapp.service.SCHEDULE_DAY";

    public static final String KEY_EXTRA_DATE = "extra_date";
    public static final String KEY_EXTRA_GROUP = "extra_group";


    public static Intent newIntentFetchSchedule(Context context, Date date, String group) {
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


        return null;
    }
}
