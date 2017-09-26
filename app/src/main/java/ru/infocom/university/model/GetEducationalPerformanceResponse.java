package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class GetEducationalPerformanceResponse {

    @Element(name = "return", required = false)
    private Return mReturn;

    public Return getReturn() {
        return mReturn;
    }

    public void setReturn(Return aReturn) {
        mReturn = aReturn;
    }
}
