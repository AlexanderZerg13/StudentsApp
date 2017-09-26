package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

public class AuthorizationResponse {

    @Element(name = "return", required = false)
    private Return mReturn;

    public Return getReturn() {
        return mReturn;
    }

    public void setReturn(Return aReturn) {
        mReturn = aReturn;
    }

}
