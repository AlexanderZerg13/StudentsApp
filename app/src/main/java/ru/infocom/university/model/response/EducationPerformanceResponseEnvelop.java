package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class EducationPerformanceResponseEnvelop {

    @Element(name = "Body")
    private EducationPerformanceResponseBody mEducationPerformanceResponseBody;

    public EducationPerformanceResponseBody getBody() {
        return mEducationPerformanceResponseBody;
    }

    public void setBody(EducationPerformanceResponseBody educationPerformanceResponseBody) {
        mEducationPerformanceResponseBody = educationPerformanceResponseBody;
    }
}
