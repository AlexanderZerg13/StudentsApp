package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

public class University implements Comparable<University>{
    private String name;
    private String city;

    public University(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int compareTo(University university) {
        return this.getName().compareTo(university.getName());
    }
}
