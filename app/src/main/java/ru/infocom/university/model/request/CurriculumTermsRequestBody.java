package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.CurriculumTerms;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

public class CurriculumTermsRequestBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetCurriculumTerms")
    private CurriculumTerms mCurriculumTerms;

    public CurriculumTerms getCurriculumTerms() {
        return mCurriculumTerms;
    }

    public void setCurriculumTerms(CurriculumTerms curriculumTerms) {
        mCurriculumTerms = curriculumTerms;
    }
}
