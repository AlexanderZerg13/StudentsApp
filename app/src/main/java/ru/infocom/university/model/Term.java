package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(name = "Term")
public class Term {

    @Element(name = "TermId")
    private String mTermId;

    @Element(name = "TermName")
    private String mTermName;

    @Element(name = "TermNumber")
    private String mTermNumber;

    public String getTermId() {
        return mTermId;
    }

    public void setTermId(String termId) {
        mTermId = termId;
    }

    public String getTermName() {
        return mTermName;
    }

    public void setTermName(String termName) {
        mTermName = termName;
    }

    public String getTermNumber() {
        return mTermNumber;
    }

    public void setTermNumber(String termNumber) {
        mTermNumber = termNumber;
    }
}
