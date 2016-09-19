package com.example.pilipenko.studentsapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.pilipenko.studentsapp.data.StudentGroup;
import com.example.pilipenko.studentsapp.database.AppDbSchema.GroupTable;

public class StudentGroupCursorWrapper extends CursorWrapper {
    public StudentGroupCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public StudentGroup getStudentGroup() {
        String identifierString = getString(getColumnIndex(GroupTable.Cols.IDENTIFIER));
        String nameGroupString = getString(getColumnIndex(GroupTable.Cols.GROUP_NAME));
        String nameSpecialityString = getString(getColumnIndex(GroupTable.Cols.SPECIALITY_NAME));
        String teachingFromString = getString(getColumnIndex(GroupTable.Cols.TEACHING_FORM));

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setIdentifier(identifierString);
        studentGroup.setGroupName(nameGroupString);
        studentGroup.setSpecialityName(nameSpecialityString);
        studentGroup.setTeachingForm(teachingFromString);

        return studentGroup;
    }
}
