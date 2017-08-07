package ru.infocom.university.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.infocom.university.data.Lesson;

public class LessonCursorWrapper extends CursorWrapper {

    public LessonCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Lesson getLesson() {
        int id = getInt(getColumnIndex(AppDbSchema.ID));
        String date = getString(getColumnIndex(AppDbSchema.LessonsTable.Cols.DATE));
        String timeStart = getString(getColumnIndex(AppDbSchema.LessonsTable.Cols.TIME_START));
        String timeEnd = getString(getColumnIndex(AppDbSchema.LessonsTable.Cols.TIME_END));
        String name = getString(getColumnIndex(AppDbSchema.LessonsTable.Cols.NAME));
        String audience = getString(getColumnIndex(AppDbSchema.LessonsTable.Cols.AUDIENCE));
        String type = getString(getColumnIndex(AppDbSchema.LessonsTable.Cols.TYPE));
        String teachersFio = getString(getColumnIndex(AppDbSchema.LessonsTable.Cols.TEACHERS_FIO));
        String group = getString(getColumnIndex(AppDbSchema.LessonsTable.Cols.GROUP_NAME));
        boolean isEmpty = getInt(getColumnIndex(AppDbSchema.LessonsTable.Cols.IS_EMPTY)) == 1;

        Lesson lesson = new Lesson(isEmpty);
        lesson.setId(id);
        lesson.setDate(date);
        lesson.setTimeStart(timeStart);
        lesson.setTimeEnd(timeEnd);
        lesson.setAudience(audience);
        lesson.setName(name);
        lesson.setType(type);
        lesson.setGroup(group);
        lesson.setTeachers(teachersFio);

        return lesson;
    }
}
