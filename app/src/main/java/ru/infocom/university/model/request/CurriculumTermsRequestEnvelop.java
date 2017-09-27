package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.CurriculumTerms;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class CurriculumTermsRequestEnvelop {

    public static CurriculumTermsRequestEnvelop generate(String curriculumId) {
        CurriculumTermsRequestEnvelop envelop = new CurriculumTermsRequestEnvelop();
        envelop.setCurriculumTerms(new CurriculumTerms(curriculumId));
        return envelop;
    }

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetCurriculumTerms")
    @Path("Body")
    private CurriculumTerms mCurriculumTerms;

    public CurriculumTerms getCurriculumTerms() {
        return mCurriculumTerms;
    }

    public void setCurriculumTerms(CurriculumTerms curriculumTerms) {
        mCurriculumTerms = curriculumTerms;
    }
}
