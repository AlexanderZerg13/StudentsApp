package com.example.pilipenko.studentsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.pilipenko.studentsapp.database.AppBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class LessonLab {

    private static LessonLab sLessonLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private List<Lesson> mLessons;

    public static LessonLab get(Context context) {
        if (sLessonLab == null) {
            sLessonLab = new LessonLab(context);
        }
        return sLessonLab;
    }

    private LessonLab(Context context) {
        mContext = context;
        mDatabase = new AppBaseHelper(mContext).getWritableDatabase();
        mLessons = new ArrayList<>();
    }
}
