package ru.infocom.university.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.infocom.university.data.LessonPlan;

/**
 * Created by pilipenko on 23.08.2017.
 */

public class LessonPlanMock {
    public static List<LessonPlan> get() {
        List<LessonPlan> lessonPlan = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        map.put("лекции", 30);
        map.put("лабораторные", 12);
        map.put("практические", 8);

        lessonPlan.add(new LessonPlan(0, "Базы данных и экспертные системы", 1, map, true, false, true));
        lessonPlan.add(new LessonPlan(1, "Методика преподавания информатики", 1, new HashMap<String, Integer>(), true, false, false));
        lessonPlan.add(new LessonPlan(2, "Информационно-логические и алгоритмические основы вычислительной техники", 1, new HashMap<String, Integer>(), false, true, false));
        lessonPlan.add(new LessonPlan(3, "Компьютерная графика", 1, new HashMap<String, Integer>(), true, false, false));
        lessonPlan.add(new LessonPlan(4, "Численные методы", 1, new HashMap<String, Integer>(), true, false, false));
        lessonPlan.add(new LessonPlan(5, "Дифференциальные уравнения", 1, new HashMap<String, Integer>(), false, true, true));
        lessonPlan.add(new LessonPlan(6, "Теория вероятностей и математическая статистика", 1, new HashMap<String, Integer>(), false, true, true));
        lessonPlan.add(new LessonPlan(7, "Системы компьютерной математики", 1, new HashMap<String, Integer>(), false, true, false));
        lessonPlan.add(new LessonPlan(8, "Языки программирования", 1, new HashMap<String, Integer>(), false, true, false));

        return lessonPlan;
    }
}
