package com.example.pilipenko.studentsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pilipenko.studentsapp.database.AppDbSchema.GroupTable;
import com.example.pilipenko.studentsapp.database.AppDbSchema.Lessons;
import com.example.pilipenko.studentsapp.database.AppDbSchema.LessonsProgress;
import com.example.pilipenko.studentsapp.database.AppDbSchema.Plan;

public class AppBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "app.db";

    public AppBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + GroupTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        GroupTable.Cols.IDENTIFIER + " TEXT, " +
                        GroupTable.Cols.GROUP_NAME + " TEXT, " +
                        GroupTable.Cols.SPECIALITY_NAME + " TEXT, " +
                        GroupTable.Cols.TEACHING_FORM + " TEXT " +
                        ")"
        );
        sqLiteDatabase.execSQL("create table " + Lessons.NAME + "(" +
                        "_id integer primary key autoincrement," +
                        Lessons.Cols.DATE + " TEXT, " +
                        Lessons.Cols.AUDIENCE + " TEXT, " +
                        Lessons.Cols.NAME + " TEXT, " +
                        Lessons.Cols.TEACHERS_FIO + " TEXT, " +
                        Lessons.Cols.TIME_START + " TEXT, " +
                        Lessons.Cols.TIME_END + " TEXT, " +
                        Lessons.Cols.TYPE + " TEXT, " +
                        Lessons.Cols.IS_EMPTY + " INTEGER " +
                        ")"
        );
        sqLiteDatabase.execSQL("create table " + LessonsProgress.NAME + "(" +
                        "_id integer primary key autoincrement," +
                        LessonsProgress.Cols.DATE + " TEXT, " +
                        LessonsProgress.Cols.NAME + " TEXT, " +
                        LessonsProgress.Cols.MARK + " TEXT, " +
                        LessonsProgress.Cols.SEMESTER + " TEXT " +
                        ")");

        sqLiteDatabase.execSQL("create table " + Plan.NAME + "(" +
                        "_id integer primary key autoincrement," +
                        Plan.Cols.NAME + " TEXT, " +
                        Plan.Cols.SEMESTER + " INTEGER, " +
                        Plan.Cols.LECTURE_HOUR + " INTEGER, " +
                        Plan.Cols.LABORATORY_HOUR + " INTEGER, " +
                        Plan.Cols.PRACTICE_HOUR + " INTEGER, " +
                        Plan.Cols.SELF_WORK_HOUR + " INTEGER, " +
                        Plan.Cols.EXAM + " INTEGER, " +
                        Plan.Cols.SET + " INTEGER, " +
                        Plan.Cols.COURSE + " INTEGER, " +
                        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
