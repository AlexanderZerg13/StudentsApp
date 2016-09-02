package com.example.pilipenko.studentsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pilipenko.studentsapp.database.AppDbSchema.GroupTable;

public class AppBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "app.db";

    public AppBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + GroupTable.NAME + "("
                        + "_id integer primary key autoincrement, " +
                        GroupTable.Cols.IDENTIFIER + " TEXT, " +
                        GroupTable.Cols.NAME_GROUP + " TEXT, " +
                        GroupTable.Cols.NAME_SPECIALITY + " TEXT, " +
                        GroupTable.Cols.TEACHING_FORM + " TEXT " +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
