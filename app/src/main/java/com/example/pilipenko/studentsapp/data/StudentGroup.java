package com.example.pilipenko.studentsapp.data;

public class StudentGroup {
    private String mIdentifier;
    private String mGroupName;
    private String mSpecialityName;
    private String mTeachingForm;

    public StudentGroup(String groupName, String identifier, String specialityName, String teachingForm) {
        mGroupName = groupName;
        mIdentifier = identifier;
        mSpecialityName = specialityName;
        mTeachingForm = teachingForm;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String identifier) {
        mIdentifier = identifier;
    }

    public String getSpecialityName() {
        return mSpecialityName;
    }

    public void setSpecialityName(String specialityName) {
        mSpecialityName = specialityName;
    }

    public String getTeachingForm() {
        return mTeachingForm;
    }

    public void setTeachingForm(String teachingForm) {
        mTeachingForm = teachingForm;
    }
}
