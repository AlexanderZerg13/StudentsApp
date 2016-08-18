package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

public class Discipline {
    private String mName;
    private String mTeacherName;
    private String mType;
    private int hours;

    public Discipline(String name, String teacherName, String type, int hours) {
        mName = name;
        mTeacherName = teacherName;
        mType = type;
        this.hours = hours;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTeacherName() {
        return mTeacherName;
    }

    public void setTeacherName(String teacherName) {
        mTeacherName = teacherName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
