package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "MarkRecord", strict = false)
public class MarkRecord {

    @Element(name = "Subject", required = false)
    private String mSubject;

    @Element(name = "Term", required = false)
    private String mTerm;

    @Element(name = "Mark", required = false)
    private String mMark;

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public String getTerm() {
        return mTerm;
    }

    public void setTerm(String term) {
        mTerm = term;
    }

    public String getMark() {
        return mMark;
    }

    public void setMark(String mark) {
        mMark = mark;
    }
}
