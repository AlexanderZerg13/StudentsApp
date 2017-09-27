package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
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
        CurriculumTermsRequestBody body = new CurriculumTermsRequestBody();
        CurriculumTerms curriculumTerms = new CurriculumTerms(curriculumId);

        envelop.setBody(body);
        body.setCurriculumTerms(curriculumTerms);

        return envelop;
    }

    @Element(name = "Body")
    private CurriculumTermsRequestBody mCurriculumTermsRequestBody;

    public CurriculumTermsRequestBody getBody() {
        return mCurriculumTermsRequestBody;
    }

    public void setBody(CurriculumTermsRequestBody curriculumTermsRequestBody) {
        mCurriculumTermsRequestBody = curriculumTermsRequestBody;
    }
}
