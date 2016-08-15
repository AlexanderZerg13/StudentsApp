package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.example.pilipenko.studentsapp.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniversityLab {

    private static UniversityLab sUniversityLab;

    public static UniversityLab get(Context context) {
        if (sUniversityLab == null) {
            sUniversityLab = new UniversityLab(context);
        }
        return sUniversityLab;
    }

    private Context mContext;
    private List<University> mUniversities;

    private UniversityLab(Context context) {
        mContext = context;
        mUniversities = new ArrayList<>(StaticData.sUniversities);
        Collections.sort(mUniversities);
    }

    public List<University> getAllUniversities() {
        return mUniversities;
    }

    public List<University> findUniversities(String request) {
        List<University> returnedList = new ArrayList<>();
        for(University university: mUniversities) {
            if (Utils.checkContains(university.getName(), request)) {
                returnedList.add(university);
            }
        }

        return returnedList;
    }
}
