package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

import android.content.Context;

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

    private boolean checkUniversity(University university, String request) {
        String nameLowerCase = university.getName().toLowerCase();
        String requestLowerCase = request.toLowerCase();

        if (nameLowerCase.contains(requestLowerCase)) {
            return true;
        }

        boolean rBoolean = true;
        String splitString[] = nameLowerCase.split("[\\p{Punct}\\s]+");

        int k = 0, check = 0;
        for (int i = 0; i < requestLowerCase.length(); i++) {

            for(int j = k; j < splitString.length; j++) {
                char ch = splitString[j].charAt(0);
                if (requestLowerCase.charAt(i) == ch) {
                    k = j + 1;
                    check++;
                    break;
                }
            }

        }
        if (check != requestLowerCase.length()) {
            rBoolean = false;
        }
        return rBoolean;
    }

    public List<University> getAllUniversities() {
        return mUniversities;
    }

    public List<University> findUniversities(String request) {
        List<University> returnedList = new ArrayList<>();
        for(University university: mUniversities) {
            if (checkUniversity(university, request)) {
                returnedList.add(university);
            }
        }

        return returnedList;
    }
}
