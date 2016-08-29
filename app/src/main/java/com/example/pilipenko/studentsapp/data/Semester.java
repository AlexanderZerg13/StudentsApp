package com.example.pilipenko.studentsapp.data;

import java.util.List;

public class Semester {
    private String mSemesterName;
    private List<Discipline> mDisciplineList;

    public Semester(String semesterName, List<Discipline> disciplineList) {
        mSemesterName = semesterName;
        mDisciplineList = disciplineList;
    }

    public String getSemesterName() {
        return mSemesterName;
    }

    public void setSemesterName(String semesterName) {
        mSemesterName = semesterName;
    }

    public List<Discipline> getDisciplineList() {
        return mDisciplineList;
    }

    public void setDisciplineList(List<Discipline> disciplineList) {
        mDisciplineList = disciplineList;
    }
}
