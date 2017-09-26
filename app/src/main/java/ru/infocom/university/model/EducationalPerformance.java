package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class EducationalPerformance {

    @Element(name = "UserId")
    private String mUserId;

    @Element(name = "RecordbookId")
    private String mRecordBookId;

    public EducationalPerformance() {
    }

    public EducationalPerformance(String userId, String recordBookId) {
        mUserId = userId;
        mRecordBookId = recordBookId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getRecordBookId() {
        return mRecordBookId;
    }

    public void setRecordBookId(String recordBookId) {
        mRecordBookId = recordBookId;
    }
}
