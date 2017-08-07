package ru.infocom.university.data;

public class Discipline {
    private String mName;
    private String mTeacherName;
    private String mType;
    private int mHours;
    private Mark mMark;

    public Discipline(String name, String teacherName, String type, int hours, Mark mark) {
        mName = name;
        mTeacherName = teacherName;
        mType = type;
        mHours = hours;
        mMark = mark;
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
        return mHours;
    }

    public void setHours(int hours) {
        this.mHours = hours;
    }

    public Mark getMark() {
        return mMark;
    }

    public void setMark(Mark mark) {
        mMark = mark;
    }

    public enum Mark {
        FIVE("5"), FOUR("4"), THREE("3"), TWO("2"), SET_OOF("не зачтено"), SET("зачтено");

        String mText;

        private Mark(String s) {
            mText = s;
        }

        @Override
        public String toString() {
            return mText;
        }
    }
}
