package com.example.pilipenko.studentsapp.data;

import java.io.Serializable;

public class LessonPlan implements Serializable {

    private int mId;
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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

    public void mergeLessonPlan(LessonPlan plan) {
        if (plan.getLectureHours() != 0) {
            this.setLectureHours(plan.getLectureHours());
        }
        if (plan.getLaboratoryHours() != 0) {
            this.setLaboratoryHours(plan.getLaboratoryHours());
        }
        if (plan.getPracticeHours() != 0) {
            this.setPracticeHours(plan.getPracticeHours());
        }
        if (plan.getSelfWorkHours() != 0) {
            this.setSelfWorkHours(plan.getSelfWorkHours());
        }
        if (plan.isExam()) {
            this.setExam(true);
        }
        if (plan.isSet()) {
            this.setExam(true);
        }
        if (plan.isCourse()) {
            this.setSet(true);
        }

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
