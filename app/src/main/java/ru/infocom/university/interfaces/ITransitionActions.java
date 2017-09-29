package ru.infocom.university.interfaces;

import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.LessonPlan;

public interface ITransitionActions {
    void goToDescribeAcademicPlan(LessonPlan lessonPlan);
    void goToDescribeLessons(Lesson lesson);
}
