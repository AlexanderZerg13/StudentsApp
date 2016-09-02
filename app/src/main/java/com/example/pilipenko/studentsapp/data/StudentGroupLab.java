package com.example.pilipenko.studentsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.pilipenko.studentsapp.database.AppBaseHelper;
import com.example.pilipenko.studentsapp.database.AppDbSchema;
import com.example.pilipenko.studentsapp.database.AppDbSchema.GroupTable;

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

    public void addStudentGroup(StudentGroup group) {
        ContentValues values = getContentValues(group);

        mDatabase.insert(GroupTable.NAME, null, values);
    }

    public List<StudentGroup> getStudentGroups() {
        return new ArrayList<>();
    }

    public int clearStudentGroups() {
        return mDatabase.delete(GroupTable.NAME, null, null);
    }

    private static ContentValues getContentValues(StudentGroup group) {
        ContentValues values = new ContentValues();
        values.put(GroupTable.Cols.IDENTIFIER, group.getIdentifier());
        values.put(GroupTable.Cols.NAME_GROUP, group.getGroupName());
        values.put(GroupTable.Cols.NAME_SPECIALITY, group.getSpecialityName());
        values.put(GroupTable.Cols.TEACHING_FORM, group.getTeachingForm());
        return values;
    }
}
