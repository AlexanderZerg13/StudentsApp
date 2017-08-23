package ru.infocom.university.mock;

import java.util.ArrayList;
import java.util.List;

import ru.infocom.university.data.LessonProgress;

/**
 * Created by pilipenko on 23.08.2017.
 */

public class LessonProgressMock {
    public static List<LessonProgress> get() {
        List<LessonProgress> list = new ArrayList<>();

        list.add(new LessonProgress("26.12.2017", "Численные методы", "первый", LessonProgress.Mark.THREE));
        list.add(new LessonProgress("24.12.2017", "Компьютерная графика", "первый", LessonProgress.Mark.FIVE));
        list.add(new LessonProgress("19.12.2017", "Методика преподавания информатики", "первый", LessonProgress.Mark.FOUR));
        list.add(new LessonProgress("15.12.2017", "Базы данных и экспертные системы", "первый", LessonProgress.Mark.FIVE));
        list.add(new LessonProgress("9.12.2017", "Информационно-логические и алгоритмические основы вычислительной техники", "первый", LessonProgress.Mark.SET));
        list.add(new LessonProgress("7.12.2017", "Языки программирования", "первый", LessonProgress.Mark.SET));
        list.add(new LessonProgress("5.12.2017", "Системы компьютерной математики", "первый", LessonProgress.Mark.SET));
        list.add(new LessonProgress("3.12.2017", "Теория вероятностей и математическая статистика", "первый", LessonProgress.Mark.SET));
        list.add(new LessonProgress("2.12.2017", "Дифференциальные уравнения", "первый", LessonProgress.Mark.SET));

        return list;
    }
}
