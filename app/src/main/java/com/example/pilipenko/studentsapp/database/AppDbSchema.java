package com.example.pilipenko.studentsapp.database;

public class AppDbSchema {
    public static final class GroupTable {
        public static final String NAME = "student_groups";

        public static final class Cols {
            public static final String IDENTIFIER = "identifier";
            public static final String NAME_GROUP = "name_group";
            public static final String NAME_SPECIALITY = "name_speciality";
            public static final String TEACHING_FORM = "teaching_form";
        }
    }

    public static final class Lessons {
        public static final String NAME = "lessons";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String TIME_START = "time_start";
            public static final String TIME_END = "time_end";
            public static final String NAME_LESSON = "name_lesson";
            public static final String TYPE_LESSON = "type_lesson";
            public static final String TEACHER_FIO = "teacher_fio";
            public static final String AUDIENCE = "audience";
            public static final String IS_EMPTY = "empty";

        }
    }
}
