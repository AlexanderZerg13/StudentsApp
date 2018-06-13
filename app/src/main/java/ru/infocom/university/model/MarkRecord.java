package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

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

    @Element(name = "Date", required = false)
    private Date mDate;

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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
