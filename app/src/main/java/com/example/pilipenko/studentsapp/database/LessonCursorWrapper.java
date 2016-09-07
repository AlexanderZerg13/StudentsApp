package com.example.pilipenko.studentsapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.pilipenko.studentsapp.data.Lesson;
import com.example.pilipenko.studentsapp.database.AppDbSchema.Lessons.Cols;

public class LessonCursorWrapper extends CursorWrapper {

    public LessonCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Lesson getLesson() {
        int id = getInt(getColumnIndex(AppDbSchema.ID));
        String date = getString(getColumnIndex(Cols.DATE));
        String timeStart = getString(getColumnIndex(Cols.TIME_START));
        String timeEnd = getString(getColumnIndex(Cols.TIME_END));
        String name = getString(getColumnIndex(Cols.NAME_LESSON));
        String audience = getString(getColumnIndex(Cols.AUDIENCE));
        String type = getString(getColumnIndex(Cols.TYPE_LESSON));
        String teacherFio = getString(getColumnIndex(Cols.TEACHER_FIO));
        boolean isEmpty = getInt(getColumnIndex(Cols.IS_EMPTY)) == 1;

        Lesson lesson = new Lesson(isEmpty);
        lesson.setId(id);
        lesson.setDate(date);
        lesson.setTimeStart(timeStart);
        lesson.setTimeEnd(timeEnd);
        lesson.setAudience(audience);
        lesson.setName(name);
        lesson.setType(type);
        lesson.setTeacherName(teacherFio);

        return lesson;
    }
}
