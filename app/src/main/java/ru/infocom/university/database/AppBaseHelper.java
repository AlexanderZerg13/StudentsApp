package ru.infocom.university.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "app.db";

    public AppBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + AppDbSchema.GroupTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        AppDbSchema.GroupTable.Cols.IDENTIFIER + " TEXT, " +
                        AppDbSchema.GroupTable.Cols.GROUP_NAME + " TEXT, " +
                        AppDbSchema.GroupTable.Cols.SPECIALITY_NAME + " TEXT, " +
                        AppDbSchema.GroupTable.Cols.TEACHING_FORM + " TEXT " +
                        ")"
        );
        sqLiteDatabase.execSQL("create table " + AppDbSchema.LessonsTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        AppDbSchema.LessonsTable.Cols.DATE + " TEXT, " +
                        AppDbSchema.LessonsTable.Cols.AUDIENCE + " TEXT, " +
                        AppDbSchema.LessonsTable.Cols.NAME + " TEXT, " +
                        AppDbSchema.LessonsTable.Cols.TEACHERS_FIO + " TEXT, " +
                        AppDbSchema.LessonsTable.Cols.GROUP_NAME + " TEXT, " +
                        AppDbSchema.LessonsTable.Cols.TIME_START + " TEXT, " +
                        AppDbSchema.LessonsTable.Cols.TIME_END + " TEXT, " +
                        AppDbSchema.LessonsTable.Cols.TYPE + " TEXT, " +
                        AppDbSchema.LessonsTable.Cols.IS_EMPTY + " INTEGER " +
                        ")"
        );
        sqLiteDatabase.execSQL("create table " + AppDbSchema.LessonsProgressTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        AppDbSchema.LessonsProgressTable.Cols.DATE + " TEXT, " +
                        AppDbSchema.LessonsProgressTable.Cols.NAME + " TEXT, " +
                        AppDbSchema.LessonsProgressTable.Cols.MARK + " TEXT, " +
                        AppDbSchema.LessonsProgressTable.Cols.SEMESTER + " TEXT " +
                        ")"
        );
        sqLiteDatabase.execSQL("create table " + AppDbSchema.PlanTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        AppDbSchema.PlanTable.Cols.NAME + " TEXT, " +
                        AppDbSchema.PlanTable.Cols.SEMESTER + " INTEGER, " +
                        AppDbSchema.PlanTable.Cols.LECTURE_HOUR + " INTEGER, " +
                        AppDbSchema.PlanTable.Cols.LABORATORY_HOUR + " INTEGER, " +
                        AppDbSchema.PlanTable.Cols.PRACTICE_HOUR + " INTEGER, " +
                        AppDbSchema.PlanTable.Cols.SELF_WORK_HOUR + " INTEGER, " +
                        AppDbSchema.PlanTable.Cols.EXAM + " INTEGER, " +
                        AppDbSchema.PlanTable.Cols.SET + " INTEGER, " +
                        AppDbSchema.PlanTable.Cols.COURSE + " INTEGER " +
                        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
