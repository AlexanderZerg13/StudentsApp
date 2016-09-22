package com.example.pilipenko.studentsapp.data;

public class LessonPlan {
    private String mName;
    private int mSemester;
    private int mLectureHours;
    private int mLaboratoryHours;
    private int mPracticeHours;
    private int mSelfWorkHours;
    private boolean mExam;
    private boolean mSet;
    private boolean mCourse;

    public LessonPlan() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getSemester() {
        return mSemester;
    }

    public void setSemester(int semester) {
        mSemester = semester;
    }

    public int getLectureHours() {
        return mLectureHours;
    }

    public void setLectureHours(int lectureHours) {
        mLectureHours = lectureHours;
    }

    public int getLaboratoryHours() {
        return mLaboratoryHours;
    }

    public void setLaboratoryHours(int laboratoryHours) {
        mLaboratoryHours = laboratoryHours;
    }

    public int getPracticeHours() {
        return mPracticeHours;
    }

    public void setPracticeHours(int practiceHours) {
        mPracticeHours = practiceHours;
    }

    public int getSelfWorkHours() {
        return mSelfWorkHours;
    }

    public void setSelfWorkHours(int selfWorkHours) {
        mSelfWorkHours = selfWorkHours;
    }

    public boolean isExam() {
        return mExam;
    }

    public void setExam(boolean exam) {
        mExam = exam;
    }

    public boolean isSet() {
        return mSet;
    }

    public void setSet(boolean set) {
        mSet = set;
    }

    public boolean isCourse() {
        return mCourse;
    }

    public void setCourse(boolean course) {
        mCourse = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonPlan that = (LessonPlan) o;

        if (mSemester != that.mSemester) return false;
        if (mLectureHours != that.mLectureHours) return false;
        if (mLaboratoryHours != that.mLaboratoryHours) return false;
        if (mPracticeHours != that.mPracticeHours) return false;
        if (mSelfWorkHours != that.mSelfWorkHours) return false;
        if (mExam != that.mExam) return false;
        if (mSet != that.mSet) return false;
        if (mCourse != that.mCourse) return false;
        return !(mName != null ? !mName.equals(that.mName) : that.mName != null);

    }

    @Override
    public String toString() {
        return "PlanTable{" +
                "mName='" + mName + '\'' +
                ", mSemester=" + mSemester +
                ", mLectureHours=" + mLectureHours +
                ", mLaboratoryHours=" + mLaboratoryHours +
                ", mPracticeHours=" + mPracticeHours +
                ", mSelfWorkHours=" + mSelfWorkHours +
                ", mExam=" + mExam +
                ", mSet=" + mSet +
                ", mCourse=" + mCourse +
                '}';
    }
}
