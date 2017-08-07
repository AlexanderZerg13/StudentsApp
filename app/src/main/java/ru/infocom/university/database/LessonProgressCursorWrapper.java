package ru.infocom.university.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.infocom.university.data.LessonProgress;

public class LessonProgressCursorWrapper extends CursorWrapper {

    public LessonProgressCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LessonProgress getLessonProgress() {
        String date = getString(getColumnIndex(AppDbSchema.LessonsProgressTable.Cols.DATE));
        String name = getString(getColumnIndex(AppDbSchema.LessonsProgressTable.Cols.NAME));
        String semester = getString(getColumnIndex(AppDbSchema.LessonsProgressTable.Cols.SEMESTER));
        LessonProgress.Mark mark = LessonProgress.Mark.fromString(getString(getColumnIndex(AppDbSchema.LessonsProgressTable.Cols.MARK)));

        LessonProgress lessonProgress = new LessonProgress();
        lessonProgress.setDate(date);
        lessonProgress.setLessonName(name);
        lessonProgress.setSemester(semester);
        lessonProgress.setMark(mark);

        return lessonProgress;
    }
}
