package com.example.pilipenko.studentsapp.com.example.pilipenko.data;

import android.content.Context;

import com.example.pilipenko.studentsapp.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupLab {

    private static GroupLab sGroupLab;

    public static GroupLab get(Context context) {
        if (sGroupLab == null) {
            sGroupLab = new GroupLab(context);
        }
        return sGroupLab;
    }

    private Context mContext;
    private List<Group> mGroups;

    private GroupLab(Context context) {
        mContext = context;
        mGroups = new ArrayList<>(StaticData.sGroups);
        Collections.sort(mGroups);
    }

    public List<Group> getAllGroups() {
        return mGroups;
    }

    public List<Group> findGroups(String request) {
        List<Group> returnedList = new ArrayList<>();

        for(Group group: mGroups) {
            if (Utils.checkContains(group.getSpeciality(), request)) {
                returnedList.add(group);
            }
        }

        return returnedList;
    }
}
