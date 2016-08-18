package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

import java.util.ArrayList;
import java.util.List;

public abstract class StaticData {
    public static List<University> sUniversities = new ArrayList<University>() {
        {
            this.add(new University("Санкт-Петербургский государственный университет","Санкт-Петербургск"));
            this.add(new University("Московский государственный университет", "Москва"));
            this.add(new University("Московский физико-технический институт", "Москва"));
            this.add(new University("Национальный исследовательский ядерный университет «МИФИ»", "Москва"));
            this.add(new University("Национальный исследовательский Томский политехнический университет", "Томск"));
            this.add(new University("Тюменский государственный нефтегазовый университет", "Тюменская область"));
            this.add(new University("Воронежская государственная медицинская академия им. Н. Н. Бурденко", "Воронежская область"));
            this.add(new University("Северо-Кавказский федеральный университет", "Ставрополь"));
        }
    };

    public static List<Group> sGroups = new ArrayList<Group>() {
        {
            this.add(new Group("Компьютерная безопасность (КБ)", "1 группа"));
            this.add(new Group("Компьютерная безопасность (КБ)", "2 группа"));
            this.add(new Group("Компьютерная безопасность (КБ)", "3 группа"));
            this.add(new Group("Прикладная геология", "1 группа"));
            this.add(new Group("Прикладная геология", "2 группа"));
            this.add(new Group("География", "1 группа"));
            this.add(new Group("География", "2 группа"));
            this.add(new Group("География", "3 группа"));
            this.add(new Group("Юриспруденция", "1 группа"));
            this.add(new Group("Юриспруденция", "2 группа"));
        }
    };

    public static List<Group> sGroupsSelect = new ArrayList<Group>() {
        {
            this.add(new Group("Компьютерная безопасность", "1 группа"));
            this.add(new Group("Переводчик в сфере профессиональной деятельности", "2 группа"));
        }
    };

    public static List<Semester> sSemesters = new ArrayList<Semester>() {
        {
            this.add(new Semester("2015, 1 семестр", new ArrayList<Discipline>() {
                {
                    this.add(new Discipline("Математический анализ", "Карина Затонская", "Экзамен", 90));
                    this.add(new Discipline("Алгебра", "Вячеслав Иванов", "Зачет", 70));
                }
            }));
            this.add(new Semester("2015, 2 семестр", new ArrayList<Discipline>() {
                {
                    this.add(new Discipline("Математический анализ", "Карина Затонская", "Экзамен", 90));
                    this.add(new Discipline("Физическая культура", "Станислав Бобровский", "Экзамен", 120));
                    this.add(new Discipline("Алгебра", "Вячеслав Иванов", "Зачет", 70));
                }
            }));
            this.add(new Semester("2016, 1 семестр", new ArrayList<Discipline>() {
                {
                    this.add(new Discipline("Математический анализ", "Карина Затонская", "Экзамен", 90));
                    this.add(new Discipline("Физическая культура", "Станислав Бобровский", "Экзамен", 120));
                    this.add(new Discipline("Алгебра", "Вячеслав Иванов", "Зачет", 70));
                    this.add(new Discipline("Информационные системы и защита информации", "Константин Иванов", "Зачет", 70));
                }
            }));
        }
    };

    public static List<Teacher> sTeachers = new ArrayList<Teacher>() {
        {
            this.add(new Teacher("Бобровский Станислав Николаевич", "Кандидат технических наук, профессор\nВедет лекции и практику"));
            this.add(new Teacher("Захаров Станислав Леонидович", "Кандидат технических наук\nВедет практику"));
            this.add(new Teacher("Ивинская Ольга Дмитриевна", "Кандидат технических наук\nВедет лекции"));
        }
    };

}
