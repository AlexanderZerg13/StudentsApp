package ru.infocom.university.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class LessonProgress implements Serializable {

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

    public enum Mark implements Parcelable {
        FIVE("отлично", 5), FOUR("хорошо", 4), THREE("удовлетворительно", 3), TWO("неудовлетворительно", 2), SET_OOF("не зачет", 0), SET("зачет", 0);

        String mText;
        int mMark;

        private Mark(String s, int mark) {
            mText = s;
            mMark = mark;
        }

        public String getText() {
            return mText;
        }



        public int getMark() {
            return mMark;
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
            if (this == SET || this == SET_OOF) {
                return getText();
            } else {
                return Integer.toString(getMark());
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }
    }

    @Override
    public String toString() {
        return "LessonProgress{" +
                "mDate='" + mDate + '\'' +
                ", mLessonName='" + mLessonName + '\'' +
                ", mSemester='" + mSemester + '\'' +
                ", mMark=" + mMark +
                '}';
    }
}
