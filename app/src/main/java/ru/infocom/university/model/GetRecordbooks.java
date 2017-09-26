package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class GetRecordBooks {

    @Element(name = "UserId")
    private String UserId;

    public GetRecordBooks() {
    }

    public GetRecordBooks(String userId) {
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
