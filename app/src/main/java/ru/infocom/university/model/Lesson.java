package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(strict = false)
public class Lesson {

    @Element(name = "Subject")
    private String mSubject;

    @Element(name = "LessonType")
    private String mLessonType;

    @Element(name = "TeacherName")
    @Path("Teacher")
    private String mTeacherName;

    @Element(name = "AcademicGroupName")
    @Path("AcademicGroup")
    private String mAcademicGroupName;

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public String getLessonType() {
        return mLessonType;
    }

    public void setLessonType(String lessonType) {
        mLessonType = lessonType;
    }

    public String getTeacherName() {
        return mTeacherName;
    }

    public void setTeacherName(String teacherName) {
        mTeacherName = teacherName;
    }

    public String getAcademicGroupName() {
        return mAcademicGroupName;
    }

    public void setAcademicGroupName(String academicGroupName) {
        mAcademicGroupName = academicGroupName;
    }
}
