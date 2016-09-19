package com.example.pilipenko.studentsapp.data;

public class LessonProgress {

    private String mDate;
    private String mLessonName;
    private String mSemester;
    private Mark mMark;

    public LessonProgress() {
    }

    public LessonProgress(String date, String lessonName, String semester, Mark mark) {
        mDate = date;
        mLessonName = lessonName;
        mMark = mark;
        mSemester = semester;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getLessonName() {
        return mLessonName;
    }

    public void setLessonName(String lessonName) {
        mLessonName = lessonName;
    }

    public Mark getMark() {
        return mMark;
    }

    public void setMark(Mark mark) {
        mMark = mark;
    }

    public String getSemester() {
        return mSemester;
    }

    public void setSemester(String semester) {
        mSemester = semester;
    }

    public enum Mark {
        FIVE("отлично"), FOUR("хорошо"), THREE("удовлетворительно"), TWO("неудовлетворительно"), SET_OOF("не зачет"), SET("зачет");

        String mText;

        private Mark(String s) {
            mText = s;
        }

        private String getText() {
            return mText;
        }

        public static Mark fromString(String string) {
            for(Mark mark : Mark.values()) {
                if (mark.getText().equalsIgnoreCase(string)) {
                    return mark;
                }
            }
            throw new IllegalArgumentException("Argument can not be " + string);
        }

        @Override
        public String toString() {
            return mText;
        }
    }
}
