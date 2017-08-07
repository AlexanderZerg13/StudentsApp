package ru.infocom.university.interfaces;

public interface ITransitionActions {
    void goToDescribeAcademicPlan(int idSemester, int idDiscipline);
    void goToDescribeLessons(int idLesson);
    void goToSession();
}
