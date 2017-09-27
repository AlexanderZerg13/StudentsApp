package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

public class CurriculumTerm {

    @Element(name = "CurriculumId")
    private String mCurriculumId;

    public String getCurriculumId() {
        return mCurriculumId;
    }

    public void setCurriculumId(String curriculumId) {
        mCurriculumId = curriculumId;
    }
}
