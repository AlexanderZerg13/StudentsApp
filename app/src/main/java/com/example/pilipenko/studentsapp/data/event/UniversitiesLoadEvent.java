package com.example.pilipenko.studentsapp.data.event;

import com.example.pilipenko.studentsapp.data.University;

import java.util.List;

/**
 * Created by pilipenko on 21.03.2017.
 */

public class UniversitiesLoadEvent {

    private List<University> mUniversitiesList;

    public UniversitiesLoadEvent(List<University> universityList) {
        mUniversitiesList = universityList;
    }

    public List<University> getUniversitiesList() {
        return mUniversitiesList;
    }
}
