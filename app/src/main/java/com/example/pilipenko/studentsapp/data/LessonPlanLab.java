package com.example.pilipenko.studentsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pilipenko.studentsapp.database.AppBaseHelper;
import com.example.pilipenko.studentsapp.database.AppDbSchema.PlanTable;
import com.example.pilipenko.studentsapp.database.AppDbSchema.PlanTable.Cols;
import com.example.pilipenko.studentsapp.database.LessonPlanCursorWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonPlanLab {

    private static LessonPlanLab sLessonPlanLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LessonPlanLab get(Context context) {
        if (sLessonPlanLab == null) {
            sLessonPlanLab = new LessonPlanLab(context);
        }
        return sLessonPlanLab;
    }

    private LessonPlanLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AppBaseHelper(context).getWritableDatabase();
    }

    public long addLessonPlan(LessonPlan lessonPlan) {
        ContentValues contentValues = getContentValues(lessonPlan);

        return mDatabase.insert(PlanTable.NAME, null, contentValues);
    }

    public long addLessonPlan(List<LessonPlan> lessonPlans) {
        if (lessonPlans == null || lessonPlans.size() == 0) {
            return 0;
        }
        long count = 0;
        clearLessonsPlan();
        for (LessonPlan plan : lessonPlans) {
            count += addLessonPlan(plan);
        }
        return count;
    }

    public List<LessonPlan> getLessonsPlan() {
        List<LessonPlan> lessonPlans = new ArrayList<>();
        LessonPlanCursorWrapper cursor = queryLessonPlan(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                lessonPlans.add(cursor.getLessonPlan());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return lessonPlans;
    }

    public Map<Integer, List<LessonPlan>> getGroupLessonsPlan() {
        List<LessonPlan> list = getLessonsPlan();
        Map<Integer, List<LessonPlan>> map = new HashMap<>();

        if (list == null || list.size() == 0) {
            return map;
        }

        for (LessonPlan lesson: list) {
            int key = lesson.getSemester();
            if (map.containsKey(key)) {
                map.get(key).add(lesson);
            } else {
                List<LessonPlan>  mapList = new ArrayList<>();
                mapList.add(lesson);
                map.put(key, mapList);
            }
        }

        return map;
    }

    public int clearLessonsPlan() {
        return mDatabase.delete(PlanTable.NAME, null, null);
    }

    private LessonPlanCursorWrapper queryLessonPlan(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PlanTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new LessonPlanCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(LessonPlan lessonPlan) {
        ContentValues values = new ContentValues();
        values.put(Cols.NAME, lessonPlan.getName());
        values.put(Cols.SEMESTER, lessonPlan.getSemester());
        values.put(Cols.LECTURE_HOUR, lessonPlan.getLectureHours());
        values.put(Cols.LABORATORY_HOUR, lessonPlan.getLaboratoryHours());
        values.put(Cols.PRACTICE_HOUR, lessonPlan.getPracticeHours());
        values.put(Cols.EXAM, lessonPlan.isExam());
        values.put(Cols.SET, lessonPlan.isSet());
        values.put(Cols.COURSE, lessonPlan.isCourse());

        return values;
    }

}
