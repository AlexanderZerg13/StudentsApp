package ru.infocom.university.data;

public class University implements Comparable<University>, Basic{
    private int mId;
    private String mName;
    private String mCity;

    public University() {

    }

    public University(String name, String city) {
        this.mName = name;
        this.mCity = city;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public int compareTo(University university) {
        return this.getName().compareTo(university.getName());
    }

    @Override
    public String firstData() {
        return getName();
    }

    @Override
    public String secondData() {
        return getCity();
    }
}
