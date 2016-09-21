package com.example.pilipenko.studentsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.pilipenko.studentsapp.database.AppBaseHelper;

public class Plan {

    private static Plan sPlanLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Plan get(Context context) {
        if (sPlanLab == null) {
            sPlanLab = new Plan(context);
        }
        return sPlanLab;
    }

    private Plan(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AppBaseHelper(context).getWritableDatabase();
    }



}
