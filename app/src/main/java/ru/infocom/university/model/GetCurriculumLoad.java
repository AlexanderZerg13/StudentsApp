package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

public class GetCurriculumLoad {

    @Element(name = "CurriculumId")
    private String mCurriculumLoad;

    @Element(name = "TermId")
    private String mTermId;

    public GetCurriculumLoad() {
    }

    public GetCurriculumLoad(String curriculumLoad, String termId) {
        mCurriculumLoad = curriculumLoad;
        mTermId = termId;
    }

    public String getCurriculumLoad() {
        return mCurriculumLoad;
    }

    public void setCurriculumLoad(String curriculumLoad) {
        mCurriculumLoad = curriculumLoad;
    }

    public String getTermId() {
        return mTermId;
    }

    public void setTermId(String termId) {
        mTermId = termId;
    }
}
