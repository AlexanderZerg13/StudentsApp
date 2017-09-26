package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.GetEducationalPerformanceResponse;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class EducationPerformanceResponseBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetEducationalPerformanceResponse")
    private GetEducationalPerformanceResponse mGetEducationalPerformanceResponse;

    public GetEducationalPerformanceResponse getGetEducationalPerformanceResponse() {
        return mGetEducationalPerformanceResponse;
    }

    public void setGetEducationalPerformanceResponse(GetEducationalPerformanceResponse getEducationalPerformanceResponse) {
        mGetEducationalPerformanceResponse = getEducationalPerformanceResponse;
    }
}
