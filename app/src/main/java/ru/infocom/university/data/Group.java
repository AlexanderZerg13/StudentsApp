package ru.infocom.university.data;

public class Group implements Basic, Comparable<Group>{
    private String mSpeciality;
    private String mGroup;

    public Group(String speciality, String group) {
        mSpeciality = speciality;
        mGroup = group;
    }

    public String getSpeciality() {
        return mSpeciality;
    }

    public void setSpeciality(String speciality) {
        mSpeciality = speciality;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    @Override
    public String firstData() {
        return getSpeciality();
    }

    @Override
    public String secondData() {
        return getGroup();
    }

    @Override
    public int compareTo(Group group) {
        int specialityComp = this.getSpeciality().compareTo(group.getSpeciality());
        if (specialityComp != 0) {
            return specialityComp;
        }
        return this.getGroup().compareTo(group.getGroup());
    }
}
