package ru.infocom.university.data;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lesson implements Serializable{
    private int mId;

    private String mName;
    private String mType;
    private String mAudience;
    private String mDate;
    private String mTimeStart;
    private String mTimeEnd;
    private String mGroup;
    private List<String> mTeachers;

    private boolean mIsTwoPair;
    private boolean mIsEmpty;

    public Lesson() {
        mTeachers = new ArrayList<>();
    }

    public Lesson(boolean isEmpty) {
        this();
        mIsEmpty = isEmpty;
    }

    public Lesson(boolean isEmpty, String date, String timeStart, String timeEnd) {
        this();
        mIsEmpty = isEmpty;

        this.mDate = date;
        this.mTimeStart = timeStart;
        this.mTimeEnd = timeEnd;
    }

    public Lesson(String name, String type, String teacherName, String groupName, String audience, String date, String timeStart, String timeEnd, boolean isTwoPair) {
        this();
        this.mName = name;
        this.mType = type;
        this.mGroup = groupName;
        addTeacher(teacherName);
        this.mAudience = audience;

        this.mDate = date;
        this.mTimeStart = timeStart;
        this.mTimeEnd = timeEnd;

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

    public List<String> getTeachers() {
        return mTeachers;
    }

    public String getTeachersString() {
        return TextUtils.join(",", mTeachers);
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public void setTeachers(List<String> teachers) {
        mTeachers = teachers;
    }

    public void setTeachers(String teachers) {
        List<String> list = new ArrayList<>();
        String[] strTeachers = teachers.split(",");
        for (String t: strTeachers) {
            list.add(t);
        }
        setTeachers(list);
    }

    public void addTeacher(String teacher) {
        mTeachers.add(teacher);
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (mId != lesson.mId) return false;
        if (mIsTwoPair != lesson.mIsTwoPair) return false;
        if (mIsEmpty != lesson.mIsEmpty) return false;
        if (mName != null ? !mName.equals(lesson.mName) : lesson.mName != null) return false;
        if (mType != null ? !mType.equals(lesson.mType) : lesson.mType != null) return false;
        if (mAudience != null ? !mAudience.equals(lesson.mAudience) : lesson.mAudience != null)
            return false;
        if (mDate != null ? !mDate.equals(lesson.mDate) : lesson.mDate != null) return false;
        if (mTimeStart != null ? !mTimeStart.equals(lesson.mTimeStart) : lesson.mTimeStart != null)
            return false;
        if (mTimeEnd != null ? !mTimeEnd.equals(lesson.mTimeEnd) : lesson.mTimeEnd != null)
            return false;
        if (mGroup != null ? !mGroup.equals(lesson.mGroup) : lesson.mGroup != null) return false;
        return mTeachers != null ? mTeachers.equals(lesson.mTeachers) : lesson.mTeachers == null;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "mAudience='" + mAudience + '\'' +
                ", mName='" + mName + '\'' +
                ", mType='" + mType + '\'' +
                ", mTeacherName='" + TextUtils.join(",", mTeachers) + '\'' +
                ", mGroup='" + mGroup + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mTimeStart='" + mTimeStart + '\'' +
                ", mTimeEnd='" + mTimeEnd + '\'' +
                ", mIsTwoPair=" + mIsTwoPair +
                ", mIsEmpty=" + mIsEmpty +
                '}';
    }
}
