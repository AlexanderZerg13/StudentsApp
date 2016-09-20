package com.example.pilipenko.studentsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pilipenko.studentsapp.database.AppBaseHelper;
import com.example.pilipenko.studentsapp.database.AppDbSchema;
import com.example.pilipenko.studentsapp.database.AppDbSchema.LessonsProgress;
import com.example.pilipenko.studentsapp.database.AppDbSchema.LessonsProgress.Cols;
import com.example.pilipenko.studentsapp.database.LessonCursorWrapper;
import com.example.pilipenko.studentsapp.database.LessonProgressCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LessonProgressLab {

    private static final String TAG = "LessonProgressLab";

    private static LessonProgressLab sLessonProgressLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LessonProgressLab get(Context context) {
        if (sLessonProgressLab == null) {
            sLessonProgressLab = new LessonProgressLab(context);
        }
        return sLessonProgressLab;
    }

    private LessonProgressLab(Context context) {
        mContext = context;
        mDatabase = new AppBaseHelper(mContext).getWritableDatabase();
    }

    public long addLessonProgress(LessonProgress lessonProgress) {
        ContentValues contentValues = getContentValue(lessonProgress);

        return mDatabase.insert(LessonsProgress.NAME, null, contentValues);
    }

    public long addLessonProgress(List<LessonProgress> list) {
        clearLessonProgress();

        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("List can not be null or have zero size");
        }

        long count = 0;

        for (int i = 0; i < list.size(); i++) {
            LessonProgress lessonProgress = list.get(i);
            count += addLessonProgress(lessonProgress);
        }
        return count;
    }

    public Map<String, List<LessonProgress>> getGroupLessonsProgress() {
        List<LessonProgress> list = getLessonsProgress();
        Map<String, List<LessonProgress>> map = new TreeMap<>();

        if (list == null || list.size() == 0) {
            return map;
        }

        for (LessonProgress lesson: list) {
            String key = lesson.getSemester();
            if (map.containsKey(key)) {
                map.get(key).add(lesson);
            } else {
                List<LessonProgress>  mapList = new ArrayList<>();
                mapList.add(lesson);
                map.put(key, mapList);
            }
        }

        return map;
    }

    public List<LessonProgress> getLessonsProgress() {
        List<LessonProgress> lessonsProgress = new ArrayList<>();
        LessonProgressCursorWrapper cursor = queryLesson(null, null);
        try {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                return null;
            }
            while (!cursor.isAfterLast()) {
                lessonsProgress.add(cursor.getLessonProgress());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return lessonsProgress;
    }

    public LessonProgress getLessonProgress(int id) {
        LessonProgress lessonProgress = null;
        LessonProgressCursorWrapper cursor = queryLesson(AppDbSchema.ID + " = ?", new String[]{Integer.toString(id)});
        try {
            cursor.moveToFirst();
            lessonProgress = cursor.getLessonProgress();
        } finally {
            cursor.close();
        }
        return lessonProgress;
    }

    public int clearLessonProgress() {
        return mDatabase.delete(LessonsProgress.NAME, null, null);
    }

    private LessonProgressCursorWrapper queryLesson(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                LessonsProgress.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new LessonProgressCursorWrapper(cursor);
    }

    private ContentValues getContentValue(LessonProgress lessonProgress) {
        ContentValues values = new ContentValues();
        values.put(Cols.DATE, lessonProgress.getDate());
        values.put(Cols.NAME, lessonProgress.getLessonName());
        values.put(Cols.MARK, lessonProgress.getMark().getText());
        values.put(Cols.SEMESTER, lessonProgress.getSemester());
        return values;
    }
}
