package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(name = "CurriculumLoad", strict = false)
public class CurriculumLoad {

    @Element(name = "Subject", required = false)
    private String mSubject;

    @Element(name = "Term", required = false)
    private String mTerm;

    @Element(name = "LoadType", required = false)
    private String mLoadType;

    @Element(name = "Amount", required = false)
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
