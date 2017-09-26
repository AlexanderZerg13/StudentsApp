package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.EducationalPerformance;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class EducationPerformanceRequestEnvelop {

    public static EducationPerformanceRequestEnvelop generate(String userId, String recordBookId) {
        EducationPerformanceRequestEnvelop envelop = new EducationPerformanceRequestEnvelop();
        EducationPerformanceRequestBody body = new EducationPerformanceRequestBody();
        EducationalPerformance educationalPerformance = new EducationalPerformance(userId, recordBookId);

        envelop.setBody(body);
        body.setEducationalPerformance(educationalPerformance);

        return envelop;
    }

    @Element(name = "Body")
    private EducationPerformanceRequestBody mEducationPerformanceRequestBody;

    public EducationPerformanceRequestBody getBody() {
        return mEducationPerformanceRequestBody;
    }

    public void setBody(EducationPerformanceRequestBody educationPerformanceRequestBody) {
        mEducationPerformanceRequestBody = educationPerformanceRequestBody;
    }
}
