package com.example.pilipenko.studentsapp.data;

import android.content.Context;

import com.example.pilipenko.studentsapp.utils.Utils;

import java.util.ArrayList;
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
        /*mUniversities = new ArrayList<>(StaticData.sUniversities);
        Collections.sort(mUniversities);*/
    }

    private void clearUniversities() {
        mUniversities = new ArrayList<>();
    }

    public long addUniversity(University university) {
        return mUniversities.add(university)? 1 : 0;
    }

    public long addUniversity(List<University> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        long count = 0;
        clearUniversities();
        for (University university : list) {
            count += addUniversity(university);
        }
        return count;
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
