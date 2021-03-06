package ru.infocom.university.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.infocom.university.Utils;
import ru.infocom.university.database.AppBaseHelper;
import ru.infocom.university.database.AppDbSchema;
import ru.infocom.university.database.LessonProgressCursorWrapper;

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

        return mDatabase.insert(AppDbSchema.LessonsProgressTable.NAME, null, contentValues);
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

    public Map<Integer, List<LessonProgress>> getGroupLessonsProgress() {
        List<LessonProgress> list = getLessonsProgress();
        Map<Integer, List<LessonProgress>> map = new TreeMap<>();

        if (list == null || list.size() == 0) {
            return map;
        }

        for (LessonProgress lesson: list) {
            int key = Utils.getSemesterFromString(lesson.getSemester());
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
        return mDatabase.delete(AppDbSchema.LessonsProgressTable.NAME, null, null);
    }

    private LessonProgressCursorWrapper queryLesson(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                AppDbSchema.LessonsProgressTable.NAME,
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
        values.put(AppDbSchema.LessonsProgressTable.Cols.DATE, lessonProgress.getDate());
        values.put(AppDbSchema.LessonsProgressTable.Cols.NAME, lessonProgress.getLessonName());
        values.put(AppDbSchema.LessonsProgressTable.Cols.MARK, lessonProgress.getMark().getText());
        values.put(AppDbSchema.LessonsProgressTable.Cols.SEMESTER, lessonProgress.getSemester());
        return values;
    }
}
