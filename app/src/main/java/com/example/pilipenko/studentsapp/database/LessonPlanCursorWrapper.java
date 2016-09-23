package com.example.pilipenko.studentsapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.pilipenko.studentsapp.data.LessonPlan;
import com.example.pilipenko.studentsapp.database.AppDbSchema.PlanTable.Cols;

public class LessonPlanCursorWrapper extends CursorWrapper {

    public LessonPlanCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LessonPlan getLessonPlan() {
        String name = getString(getColumnIndex(Cols.NAME));
        int id = getInt(getColumnIndex(AppDbSchema.ID));
        int semester = getInt(getColumnIndex(Cols.SEMESTER));
        int lectureHours = getInt(getColumnIndex(Cols.LECTURE_HOUR));
        int laboratoryHours = getInt(getColumnIndex(Cols.LABORATORY_HOUR));
        int practiceHours = getInt(getColumnIndex(Cols.PRACTICE_HOUR));
        int selfWorkHours = getInt(getColumnIndex(Cols.SELF_WORK_HOUR));
        boolean exam = getInt(getColumnIndex(Cols.EXAM)) == 1? true : false;
        boolean set = getInt(getColumnIndex(Cols.SET)) == 1? true : false;
        boolean course = getInt(getColumnIndex(Cols.COURSE)) == 1? true : false;

        LessonPlan lessonPlan = new LessonPlan();
        lessonPlan.setId(id);
        lessonPlan.setName(name);
        lessonPlan.setSemester(semester);
        lessonPlan.setLectureHours(lectureHours);
        lessonPlan.setLaboratoryHours(laboratoryHours);
        lessonPlan.setPracticeHours(practiceHours);
        lessonPlan.setSelfWorkHours(selfWorkHours);
        lessonPlan.setExam(exam);
        lessonPlan.setSet(set);
        lessonPlan.setCourse(course);

        return lessonPlan;
    }
}
