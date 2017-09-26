package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.EducationalPerformance;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class EducationPerformanceRequestBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "EducationalPerformance")
    private EducationalPerformance mEducationalPerformance;

    public EducationalPerformance getEducationalPerformance() {
        return mEducationalPerformance;
    }

    public void setEducationalPerformance(EducationalPerformance educationalPerformance) {
        mEducationalPerformance = educationalPerformance;
    }
}
