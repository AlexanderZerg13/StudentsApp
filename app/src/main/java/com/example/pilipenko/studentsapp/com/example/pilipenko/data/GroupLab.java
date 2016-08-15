package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupLab {

    private static GroupLab sGroupLab;

    private Context mContext;
    private List<Group> mGroups;

    public static GroupLab get(Context context) {
        if (sGroupLab == null) {
            sGroupLab = new GroupLab(context);
        }
        return sGroupLab;
    }

    private GroupLab(Context context) {
        mContext = context;
        mGroups = new ArrayList<>(StaticData.sGroups);
        Collections.sort(mGroups);
    }

    private boolean checkGroups(Group group, String request) {
        String nameLowerCase = group.getSpeciality().toLowerCase();
        String requestLowerCase = request.toLowerCase();

        if (nameLowerCase.contains(requestLowerCase)) {
            return true;
        }
        return false;
    }

    public List<Group> getAllGroups() {
        return mGroups;
    }

    public List<Group> findGroups(String request) {
        List<Group> returnedList = new ArrayList<>();

        for(Group group: mGroups) {
            if (checkGroups(group, request)) {
                returnedList.add(group);
            }
        }

        return returnedList;
    }
}
