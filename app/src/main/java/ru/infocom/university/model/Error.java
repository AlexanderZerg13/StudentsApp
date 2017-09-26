package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class Error {

    @Element(name = "Code")
    private int mCode;

    @Element(name = "Description")
    private String mDescription;

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
