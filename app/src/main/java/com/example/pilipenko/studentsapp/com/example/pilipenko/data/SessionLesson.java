package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

import java.util.Date;

public class SessionLesson {
    private String mName;
    private String mTeacher;
    private Type mType;
    private String audience;
    private Date mDate;

    public SessionLesson(String name, String teacher, Type type, String audience, Date date) {
        mName = name;
        mTeacher = teacher;
        mType = type;
        this.audience = audience;
        mDate = date;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTeacher() {
        return mTeacher;
    }

    public void setTeacher(String teacher) {
        mTeacher = teacher;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public enum Type {
        EXAM("ЭКЗ"), POINT("ЗАЧ"), CONSULT("КОНС");

        private String mString;

        private Type(String str) {
            mString = str;
        }

        @Override
        public String toString() {
            return mString;
        }
    }
}
