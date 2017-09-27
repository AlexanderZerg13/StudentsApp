package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

public class CurriculumTerms {

    @Element(name = "CurriculumId")
    private String mCurriculumId;

    public CurriculumTerms() {
    }

    public CurriculumTerms(String curriculumId) {
        mCurriculumId = curriculumId;
    }

    public String getCurriculumId() {
        return mCurriculumId;
    }

    public void setCurriculumId(String curriculumId) {
        mCurriculumId = curriculumId;
    }
}
