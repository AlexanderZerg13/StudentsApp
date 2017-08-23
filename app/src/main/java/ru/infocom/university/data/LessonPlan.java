package ru.infocom.university.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LessonPlan implements Serializable {

    private int mId;
    private String mName;
    private int mSemester;

    private Map<String, Integer> mLoadMap;
    private boolean mExam;
    private boolean mSet;
    private boolean mCourse;

    public LessonPlan() {
        mLoadMap = new HashMap<>();
    }

    public LessonPlan(int id, String name, int semester, Map<String, Integer> loadMap, boolean exam, boolean set, boolean course) {
        mId = id;
        mName = name;
        mSemester = semester;
        mLoadMap = loadMap;
        mExam = exam;
        mSet = set;
        mCourse = course;
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
        if (plan.isExam()) {
            this.setExam(true);
        }
        if (plan.isSet()) {
            this.setSet(true);
        }
        if (plan.isCourse()) {
            this.setCourse(true);
        }

    }


    //Load Map

    public Map<String, Integer> getLoadMap() {
        return mLoadMap;
    }

    public void setLoadMap(Map<String, Integer> loadMap) {
        this.mLoadMap = loadMap;
    }

    public void setLoadToMap(String loadType, int hours) {
        mLoadMap.put(loadType, hours);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonPlan that = (LessonPlan) o;

        if (mSemester != that.mSemester) return false;
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
                ", mExam=" + mExam +
                ", mSet=" + mSet +
                ", mCourse=" + mCourse +
                ", mLoadMap=" + mLoadMap +
                '}';
    }
}
