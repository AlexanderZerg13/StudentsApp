package ru.infocom.university.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.infocom.university.database.AppBaseHelper;
import ru.infocom.university.database.StudentGroupCursorWrapper;

import java.util.ArrayList;
import java.util.List;

import ru.infocom.university.database.AppDbSchema;

public class StudentGroupLab {

    private static StudentGroupLab sStudentGroupLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static StudentGroupLab get(Context context) {
        if (sStudentGroupLab == null) {
            sStudentGroupLab = new StudentGroupLab(context);
        }
        return sStudentGroupLab;
    }

    private StudentGroupLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AppBaseHelper(mContext).getWritableDatabase();
    }

    public long addStudentGroup(StudentGroup group) {
        ContentValues values = getContentValues(group);

        return mDatabase.insert(AppDbSchema.GroupTable.NAME, null, values);
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
        return mDatabase.delete(AppDbSchema.GroupTable.NAME, null, null);
    }

    private StudentGroupCursorWrapper queryStudentGroups(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                AppDbSchema.GroupTable.NAME,
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
        values.put(AppDbSchema.GroupTable.Cols.IDENTIFIER, group.getIdentifier());
        values.put(AppDbSchema.GroupTable.Cols.GROUP_NAME, group.getGroupName());
        values.put(AppDbSchema.GroupTable.Cols.SPECIALITY_NAME, group.getSpecialityName());
        values.put(AppDbSchema.GroupTable.Cols.TEACHING_FORM, group.getTeachingForm());
        return values;
    }
}
