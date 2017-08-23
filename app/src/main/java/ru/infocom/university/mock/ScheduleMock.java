package ru.infocom.university.mock;

import java.util.ArrayList;
import java.util.List;

import ru.infocom.university.data.Lesson;

/**
 * Created by pilipenko on 23.08.2017.
 */

public class ScheduleMock {
    public static List<Lesson> get(String date) {
        List<Lesson> lessons = new ArrayList<>();

//        list.add(new LessonProgress("26.12.2017", "Численные методы", "первый", LessonProgress.Mark.THREE));
//        list.add(new LessonProgress("24.12.2017", "Компьютерная графика", "первый", LessonProgress.Mark.FIVE));
//        list.add(new LessonProgress("19.12.2017", "Методика преподавания информатики", "первый", LessonProgress.Mark.FOUR));
//        list.add(new LessonProgress("15.12.2017", "Базы данных и экспертные системы", "первый", LessonProgress.Mark.FIVE));
//        list.add(new LessonProgress("9.12.2017", "Информационно-логические и алгоритмические основы вычислительной техники", "первый", LessonProgress.Mark.SET));
//        list.add(new LessonProgress("7.12.2017", "Языки программирования", "первый", LessonProgress.Mark.SET));

        lessons.add(new Lesson("Численные методы", "Лекция", "Иванов Сергей Игоревич", "ПМИ-1", "352", date, "8:05", "9:35", false));
        lessons.add(new Lesson(true, date, "9:45", "11:15"));
        lessons.add(new Lesson("Компьютерная графика", "Практика", "Куликов Александр Владимирович", "ПМИ-1", "111", date, "11:25", "12:55", false));
        lessons.add(new Lesson("Языки программирования", "Лекция", "Гусев Константин Николаевич", "ПМИ-1", "413", date, "13:00", "14:30", false));

        return lessons;
    }

}
