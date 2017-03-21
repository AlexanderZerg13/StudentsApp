package com.example.pilipenko.studentsapp.data.respones;

import com.example.pilipenko.studentsapp.data.University;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by pilipenko on 21.03.2017.
 */

@Root(name = "universities")
public class UniversitiesRespones {

    @ElementList(entry = "university", inline = true)
    public ArrayList<University> mUniversities;
}
