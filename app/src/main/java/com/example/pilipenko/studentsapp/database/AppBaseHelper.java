package com.example.pilipenko.studentsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pilipenko.studentsapp.database.AppDbSchema.GroupTable;
import com.example.pilipenko.studentsapp.database.AppDbSchema.LessonsTable;
import com.example.pilipenko.studentsapp.database.AppDbSchema.LessonsProgressTable;
import com.example.pilipenko.studentsapp.database.AppDbSchema.PlanTable;

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
        sqLiteDatabase.execSQL("create table " + LessonsTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        LessonsTable.Cols.DATE + " TEXT, " +
                        LessonsTable.Cols.AUDIENCE + " TEXT, " +
                        LessonsTable.Cols.NAME + " TEXT, " +
                        LessonsTable.Cols.TEACHERS_FIO + " TEXT, " +
                        LessonsTable.Cols.GROUP_NAME + " TEXT, " +
                        LessonsTable.Cols.TIME_START + " TEXT, " +
                        LessonsTable.Cols.TIME_END + " TEXT, " +
                        LessonsTable.Cols.TYPE + " TEXT, " +
                        LessonsTable.Cols.IS_EMPTY + " INTEGER " +
                        ")"
        );
        sqLiteDatabase.execSQL("create table " + LessonsProgressTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        LessonsProgressTable.Cols.DATE + " TEXT, " +
                        LessonsProgressTable.Cols.NAME + " TEXT, " +
                        LessonsProgressTable.Cols.MARK + " TEXT, " +
                        LessonsProgressTable.Cols.SEMESTER + " TEXT " +
                        ")"
        );
        sqLiteDatabase.execSQL("create table " + PlanTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        PlanTable.Cols.NAME + " TEXT, " +
                        PlanTable.Cols.SEMESTER + " INTEGER, " +
                        PlanTable.Cols.LECTURE_HOUR + " INTEGER, " +
                        PlanTable.Cols.LABORATORY_HOUR + " INTEGER, " +
                        PlanTable.Cols.PRACTICE_HOUR + " INTEGER, " +
                        PlanTable.Cols.SELF_WORK_HOUR + " INTEGER, " +
                        PlanTable.Cols.EXAM + " INTEGER, " +
                        PlanTable.Cols.SET + " INTEGER, " +
                        PlanTable.Cols.COURSE + " INTEGER " +
                        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
