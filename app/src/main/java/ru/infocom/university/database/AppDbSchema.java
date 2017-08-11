package ru.infocom.university.database;

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

    public static final class LessonsTable {
        public static final String NAME = "lessons";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String TIME_START = "time_start";
            public static final String TIME_END = "time_end";
            public static final String NAME = "name";
            public static final String TYPE = "type";
            public static final String TEACHERS_FIO = "teachers_fio";
            public static final String AUDIENCE = "audience";
            public static final String GROUP_NAME = "group_name";
            public static final String IS_EMPTY = "empty";

        }
    }

    public static final class LessonsProgressTable {
        public static final String NAME = "lessons_progress";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String NAME = "name";
            public static final String MARK = "mark";
            public static final String SEMESTER = "semester";
        }
    }

    public static final class PlanTable {
        public static final String NAME = "plan";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String SEMESTER = "semester";
            public static final String EXAM = "exam";
            public static final String SET = "zach";
            public static final String COURSE  = "course";
            public static final String LOAD = "load";

            @Deprecated
            public static final String LECTURE_HOUR = "lecture_hour";
            @Deprecated
            public static final String LABORATORY_HOUR = "laboratory_hour";
            @Deprecated
            public static final String PRACTICE_HOUR = "practice_hour";
            @Deprecated
            public static final String SELF_WORK_HOUR = "self_work_hour";
        }
    }

    public static final String ID = "_id";
}
