package com.example.pilipenko.studentsapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.database.AppDbSchema.LessonsProgressTable.Cols;

public class LessonProgressCursorWrapper extends CursorWrapper {

    public LessonProgressCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LessonProgress getLessonProgress() {
        String date = getString(getColumnIndex(Cols.DATE));
        String name = getString(getColumnIndex(Cols.NAME));
        String semester = getString(getColumnIndex(Cols.SEMESTER));
        LessonProgress.Mark mark = LessonProgress.Mark.fromString(getString(getColumnIndex(Cols.MARK)));

        LessonProgress lessonProgress = new LessonProgress();
        lessonProgress.setDate(date);
        lessonProgress.setLessonName(name);
        lessonProgress.setSemester(semester);
        lessonProgress.setMark(mark);

        return lessonProgress;
    }
}
