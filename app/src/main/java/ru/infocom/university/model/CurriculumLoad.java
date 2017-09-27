package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

public class CurriculumLoad {

    @Element(name = "Subject")
    private String mSubject;

    @Element(name = "Term")
    private String mTerm;

    @Element(name = "LoadType")
    private String mLoadType;

    @Element(name = "Amount")
    private int mAmount;

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

    public String getLoadType() {
        return mLoadType;
    }

    public void setLoadType(String loadType) {
        mLoadType = loadType;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }
}
