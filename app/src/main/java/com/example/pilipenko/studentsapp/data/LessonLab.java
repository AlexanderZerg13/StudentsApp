package com.example.pilipenko.studentsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pilipenko.studentsapp.database.AppBaseHelper;
import com.example.pilipenko.studentsapp.database.AppDbSchema;
import com.example.pilipenko.studentsapp.database.AppDbSchema.Lessons;
import com.example.pilipenko.studentsapp.database.AppDbSchema.Lessons.Cols;
import com.example.pilipenko.studentsapp.database.LessonCursorWrapper;

import java.util.ArrayList;
import java.util.List;

public class LessonLab {

    private static final String TAG = "LessonLab";

    private static LessonLab sLessonLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LessonLab get(Context context) {
        if (sLessonLab == null) {
            sLessonLab = new LessonLab(context);
        }
        return sLessonLab;
    }

    public static boolean scheduleIsAbsent(List<Lesson> list) {
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("List can not be null or have zero size");
        }
        for (Lesson lesson : list) {
            if (!lesson.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private LessonLab(Context context) {
        mContext = context;
        mDatabase = new AppBaseHelper(mContext).getWritableDatabase();
    }

    public long addLesson(Lesson lesson) {
        ContentValues contentValues = getContentValue(lesson);

        return mDatabase.insert(Lessons.NAME, null, contentValues);
    }

    public long addLesson(List<Lesson> listNew, String date) {

        clearLessonByDay(date);
        if (listNew == null || listNew.size() == 0) {
            throw new IllegalArgumentException("List can not be null or have zero size");
        }

        long count = 0;

        for (int i = 0; i < listNew.size(); i++) {
            Lesson lesson = listNew.get(i);
            count += addLesson(lesson);
        }
        return count;
    }

    public List<Lesson> getLessons(String date) {
        List<Lesson> lessons = new ArrayList<>();
        LessonCursorWrapper cursor = queryLesson(Cols.DATE + " = ?", new String[]{date});
        try {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                return null;
            }
            while (!cursor.isAfterLast()) {
                lessons.add(cursor.getLesson());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return lessons;
    }

    public Lesson getLesson(int id) {
        Lesson lesson = null;
        LessonCursorWrapper cursor = queryLesson(AppDbSchema.ID + " = ?", new String[]{Integer.toString(id)});
        try {
            cursor.moveToFirst();
            lesson = cursor.getLesson();
        } finally {
            cursor.close();
        }
        return lesson;
    }

    public int clearLessonByDay(String day) {
        return mDatabase.delete(Lessons.NAME, Cols.DATE + " = ?", new String[]{day});
    }

    public int clearLesson() {
        return mDatabase.delete(Lessons.NAME, null, null);
    }

    public static boolean isEqualsList(List<Lesson> l1, List<Lesson> l2) {
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        if (l1.size() != l2.size()) return false;
        for (int i = 0; i < l1.size(); i++) {
//            Log.i(TAG, "l1[" + i + "]: " + l1.get(i));
//            Log.i(TAG, "l2[" + i + "]: " + l2.get(i));
            if (!l1.get(i).equals(l2.get(i))) {
                return false;
            }
        }

        return true;
    }

    private LessonCursorWrapper queryLesson(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                Lessons.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new LessonCursorWrapper(cursor);
    }

    private ContentValues getContentValue(Lesson lesson) {
        ContentValues values = new ContentValues();
        values.put(Cols.DATE, lesson.getDate());
        values.put(Cols.TIME_START, lesson.getTimeStart());
        values.put(Cols.TIME_END, lesson.getTimeEnd());
        values.put(Cols.NAME, lesson.getName());
        values.put(Cols.TYPE, lesson.getType());
        values.put(Cols.TEACHERS_FIO, lesson.getTeachersString());
        values.put(Cols.AUDIENCE, lesson.getAudience());
        values.put(Cols.IS_EMPTY, lesson.isEmpty() ? 1 : 0);
        return values;
    }
}
