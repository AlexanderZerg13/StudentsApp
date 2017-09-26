package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.GetEducationalPerformance;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class EducationPerformanceRequestEnvelop {

    public static EducationPerformanceRequestEnvelop generate(String userId, String recordBookId) {
        EducationPerformanceRequestEnvelop envelop = new EducationPerformanceRequestEnvelop();
        EducationPerformanceRequestBody body = new EducationPerformanceRequestBody();
        GetEducationalPerformance educationalPerformance = new GetEducationalPerformance(userId, recordBookId);

        envelop.setBody(body);
        body.setGetEducationalPerformance(educationalPerformance);

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
