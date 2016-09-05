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
        String date = getString(getColumnIndex(Cols.DATE));
        String timeStart = getString(getColumnIndex(Cols.TIME_START));
        String timeEnd = getString(getColumnIndex(Cols.TIME_END));
        String name = getString(getColumnIndex(Cols.NAME_LESSON));
        String type = "ЛЕК";
        String teacherFio = getString(getColumnIndex(Cols.TEACHER_FIO));
        boolean isEmpty = getInt(getColumnIndex(Cols.IS_EMPTY)) == 1;

        Lesson lesson = new Lesson(isEmpty);
        lesson.setDate(date);
        lesson.setTimeStart(timeStart);
        lesson.setTimeEnd(timeEnd);
        lesson.setName(name);
        lesson.setType(type);
        lesson.setTeacherName(teacherFio);

        return lesson;
    }
}
