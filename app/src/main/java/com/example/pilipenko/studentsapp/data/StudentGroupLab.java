package com.example.pilipenko.studentsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pilipenko.studentsapp.database.AppBaseHelper;
import com.example.pilipenko.studentsapp.database.AppDbSchema.GroupTable;
import com.example.pilipenko.studentsapp.database.StudentGroupCursorWrapper;

import java.util.ArrayList;
import java.util.List;

public class StudentGroupLab {

    private static StudentGroupLab sStudentGroupLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private List<StudentGroup> mStudentGroups;

    public static StudentGroupLab get(Context context) {
        if (sStudentGroupLab == null) {
            sStudentGroupLab = new StudentGroupLab(context);
        }
        return sStudentGroupLab;
    }

    private StudentGroupLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AppBaseHelper(mContext).getWritableDatabase();
        mStudentGroups = new ArrayList<>();
    }

    public long addStudentGroup(StudentGroup group) {
        ContentValues values = getContentValues(group);

        return mDatabase.insert(GroupTable.NAME, null, values);
    }

    public long addStudentGroup(List<StudentGroup> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        long count = 0;
        clearStudentGroups();
        for (StudentGroup group : list) {
            count += addStudentGroup(group);
        }
        return count;
    }

    public List<StudentGroup> getStudentGroups() {
        List<StudentGroup> studentGroupList = new ArrayList<>();
        StudentGroupCursorWrapper cursor = queryStudentGroups(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                studentGroupList.add(cursor.getStudentGroup());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return studentGroupList;
    }

    public int clearStudentGroups() {
        return mDatabase.delete(GroupTable.NAME, null, null);
    }

    private StudentGroupCursorWrapper queryStudentGroups(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                GroupTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new StudentGroupCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(StudentGroup group) {
        ContentValues values = new ContentValues();
        values.put(GroupTable.Cols.IDENTIFIER, group.getIdentifier());
        values.put(GroupTable.Cols.GROUP_NAME, group.getGroupName());
        values.put(GroupTable.Cols.SPECIALITY_NAME, group.getSpecialityName());
        values.put(GroupTable.Cols.TEACHING_FORM, group.getTeachingForm());
        return values;
    }
}
