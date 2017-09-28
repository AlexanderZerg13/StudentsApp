package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Recordbook", strict = false)
public class RecordBook implements Serializable {

    @Element(name = "RecordbookId")
    private String mRecordBookId;

    @Element(name = "RecordbookName")
    private String mRecordBookName;

    @Element(name = "CurriculumId")
    private String mCurriculumId;

    @Element(name = "SpecialtyName")
    private String mSpecialityName;

    @Element(name = "AcademicGroupName")
    private String mAcademicGroupName;

    @Element(name = "AcademicGroupCompoundKey")
    private String mGroupId;

    public String getRecordBookId() {
        return mRecordBookId;
    }

    public void setRecordBookId(String recordBookId) {
        mRecordBookId = recordBookId;
    }

    public String getRecordBookName() {
        return mRecordBookName;
    }

    public void setRecordBookName(String recordBookName) {
        mRecordBookName = recordBookName;
    }

    public String getCurriculumId() {
        return mCurriculumId;
    }

    public void setCurriculumId(String curriculumId) {
        mCurriculumId = curriculumId;
    }

    public String getSpecialityName() {
        return mSpecialityName;
    }

    public void setSpecialityName(String specialityName) {
        mSpecialityName = specialityName;
    }

    public String getAcademicGroupName() {
        return mAcademicGroupName;
    }

    public void setAcademicGroupName(String academicGroupName) {
        mAcademicGroupName = academicGroupName;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(String groupId) {
        mGroupId = groupId;
    }
}
