package ru.infocom.university.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.infocom.university.data.LessonPlan;

public class LessonPlanCursorWrapper extends CursorWrapper {

    public LessonPlanCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LessonPlan getLessonPlan() {
        String name = getString(getColumnIndex(AppDbSchema.PlanTable.Cols.NAME));
        int id = getInt(getColumnIndex(AppDbSchema.ID));
        int semester = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.SEMESTER));
        int lectureHours = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.LECTURE_HOUR));
        int laboratoryHours = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.LABORATORY_HOUR));
        int practiceHours = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.PRACTICE_HOUR));
        int selfWorkHours = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.SELF_WORK_HOUR));
        boolean exam = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.EXAM)) == 1? true : false;
        boolean set = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.SET)) == 1? true : false;
        boolean course = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.COURSE)) == 1? true : false;

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
