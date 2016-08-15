package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

public class Group {
    private String mSpeciality;
    private String mGroup;

    public Group(String speciality, String group) {
        mSpeciality = speciality;
        mGroup = group;
    }

    public String getSpeciality() {
        return mSpeciality;
    }

    public void setSpeciality(String speciality) {
        mSpeciality = speciality;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }
}
