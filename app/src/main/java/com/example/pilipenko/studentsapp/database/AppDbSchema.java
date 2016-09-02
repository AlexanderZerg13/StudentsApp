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
}
