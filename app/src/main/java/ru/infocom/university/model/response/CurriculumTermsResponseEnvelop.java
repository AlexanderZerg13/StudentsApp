package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class CurriculumTermsResponseEnvelop {

    @Element(name = "Body")
    private CurriculumTermsResponseBody mCurriculumTermsResponseBody;

    public CurriculumTermsResponseBody getBody() {
        return mCurriculumTermsResponseBody;
    }

    public void setBody(CurriculumTermsResponseBody curriculumTermsResponseBody) {
        mCurriculumTermsResponseBody = curriculumTermsResponseBody;
    }
}
