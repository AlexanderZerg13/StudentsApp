package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.GetEducationalPerformance;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class EducationPerformanceRequestBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetEducationalPerformance")
    private GetEducationalPerformance mGetEducationalPerformance;

    public GetEducationalPerformance getGetEducationalPerformance() {
        return mGetEducationalPerformance;
    }

    public void setGetEducationalPerformance(GetEducationalPerformance getEducationalPerformance) {
        mGetEducationalPerformance = getEducationalPerformance;
    }
}
