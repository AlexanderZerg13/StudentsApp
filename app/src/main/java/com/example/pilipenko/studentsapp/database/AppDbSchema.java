package com.example.pilipenko.studentsapp.database;

public class AppDbSchema {
    public static final class GroupTable {
        public static final String NAME = "student_groups";

        public static final class Cols {
            public static final String IDENTIFIER = "identifier";
            public static final String GROUP_NAME = "group_name";
            public static final String SPECIALITY_NAME = "speciality_name";
            public static final String TEACHING_FORM = "teaching_form";
        }
    }

    public static final class Lessons {
        public static final String NAME = "lessons";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String TIME_START = "time_start";
            public static final String TIME_END = "time_end";
            public static final String NAME = "name";
            public static final String TYPE = "type";
            public static final String TEACHER_FIO = "teacher_fio";
            public static final String AUDIENCE = "audience";
            public static final String IS_EMPTY = "empty";

        }
    }

    public static final class LessonsProgress {
        public static final String NAME = "lessons_progress";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String NAME = "name";
            public static final String MARK = "mark";
            public static final String SEMESTER = "semester";
        }
    }


    public static final String ID = "_id";
}
