package ru.infocom.university.modules.grades.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import ru.infocom.university.modules.grades.model.EducationalPerformance;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class EducationPerformanceRequestEnvelop {

    public static EducationPerformanceRequestEnvelop generate(String userId, String recordBookId) {
        EducationPerformanceRequestEnvelop envelop = new EducationPerformanceRequestEnvelop();
        envelop.setEducationalPerformance(new EducationalPerformance(userId, recordBookId));
        return envelop;
    }

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetEducationalPerformance")
    @Path("Body")
    private EducationalPerformance mEducationalPerformance;

    public EducationalPerformance getEducationalPerformance() {
        return mEducationalPerformance;
    }

    public void setEducationalPerformance(EducationalPerformance educationalPerformance) {
        mEducationalPerformance = educationalPerformance;
    }
}
