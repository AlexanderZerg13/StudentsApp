package com.example.pilipenko.studentsapp.data;

public class Lesson {
    private String mName;
    private String mType;
    private String mTeacherName;
    private String mAudience;
    private String mDate;
    private String mTimeStart;
    private String mTimeEnd;

    private boolean mIsTwoPair;
    private boolean mIsEmpty;

    public Lesson() {
    }

    public Lesson(boolean isEmpty) {
        mIsEmpty = isEmpty;
    }

    public Lesson(String name, String type, String teacherName, String audience, boolean isTwoPair) {
        this.mName = name;
        this.mType = type;
        this.mTeacherName = teacherName;
        this.mAudience = audience;
        this.mIsTwoPair = isTwoPair;
        mIsEmpty = false;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getTeacherName() {
        return mTeacherName;
    }

    public void setTeacherName(String teacherName) {
        this.mTeacherName = teacherName;
    }

    public String getAudience() {
        return mAudience;
    }

    public void setAudience(String audience) {
        this.mAudience = audience;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTimeEnd() {
        return mTimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        mTimeEnd = timeEnd;
    }

    public String getTimeStart() {
        return mTimeStart;
    }

    public void setTimeStart(String timeStart) {
        mTimeStart = timeStart;
    }

    public boolean isTwoPair() {
        return mIsTwoPair;
    }

    public void setIsTwoPair(boolean isTwoPair) {
        this.mIsTwoPair = isTwoPair;
    }

    public boolean isEmpty() {
        return mIsEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        mIsEmpty = isEmpty;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "mAudience='" + mAudience + '\'' +
                ", mName='" + mName + '\'' +
                ", mType='" + mType + '\'' +
                ", mTeacherName='" + mTeacherName + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mTimeStart='" + mTimeStart + '\'' +
                ", mTimeEnd='" + mTimeEnd + '\'' +
                ", mIsTwoPair=" + mIsTwoPair +
                ", mIsEmpty=" + mIsEmpty +
                '}';
    }
}
