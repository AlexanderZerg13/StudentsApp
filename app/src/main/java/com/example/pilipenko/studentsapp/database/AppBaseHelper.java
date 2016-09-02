package com.example.pilipenko.studentsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pilipenko.studentsapp.database.AppDbSchema.GroupTable;
import com.example.pilipenko.studentsapp.database.AppDbSchema.Lessons;

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
                        GroupTable.Cols.NAME_GROUP + " TEXT, " +
                        GroupTable.Cols.NAME_SPECIALITY + " TEXT, " +
                        GroupTable.Cols.TEACHING_FORM + " TEXT " +
                        ")"
        );
        sqLiteDatabase.execSQL("create table " + Lessons.NAME + "(" +
                        "_id integer primary key autoincrement," +
                        Lessons.Cols.DATE + " TEXT, " +
                        Lessons.Cols.AUDIENCE + " TEXT, " +
                        Lessons.Cols.NAME_LESSON + " TEXT, " +
                        Lessons.Cols.TEACHER_FIO + " TEXT, " +
                        Lessons.Cols.TIME_START + " TEXT, " +
                        Lessons.Cols.TIME_END + " TEXT, " +
                        Lessons.Cols.TYPE_LESSON + "TEXT " +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
