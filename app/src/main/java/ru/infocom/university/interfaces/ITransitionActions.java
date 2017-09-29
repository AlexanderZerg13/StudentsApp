package ru.infocom.university.interfaces;

import ru.infocom.university.data.Lesson;

public interface ITransitionActions {
    void goToDescribeAcademicPlan(int idSemester, int idDiscipline);
    void goToDescribeLessons(Lesson lesson);
    void goToSession();
}
