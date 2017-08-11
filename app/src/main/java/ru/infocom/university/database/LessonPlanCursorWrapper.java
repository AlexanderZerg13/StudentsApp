package ru.infocom.university.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.infocom.university.data.LessonPlan;
import ru.infocom.university.data.LessonPlanLab;

public class LessonPlanCursorWrapper extends CursorWrapper {

    public LessonPlanCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LessonPlan getLessonPlan() {
        String name = getString(getColumnIndex(AppDbSchema.PlanTable.Cols.NAME));
        int id = getInt(getColumnIndex(AppDbSchema.ID));
        int semester = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.SEMESTER));
        boolean exam = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.EXAM)) == 1;
        boolean set = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.SET)) == 1;
        boolean course = getInt(getColumnIndex(AppDbSchema.PlanTable.Cols.COURSE)) == 1;
        String load = getString(getColumnIndex(AppDbSchema.PlanTable.Cols.LOAD));

        LessonPlan lessonPlan = new LessonPlan();
        lessonPlan.setId(id);
        lessonPlan.setName(name);
        lessonPlan.setSemester(semester);
        lessonPlan.setExam(exam);
        lessonPlan.setSet(set);
        lessonPlan.setCourse(course);
        lessonPlan.setLoadMap(LessonPlanLab.convertStringToLoadMap(load));

        return lessonPlan;
    }
}
