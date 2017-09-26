package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.Authorization;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

public class AuthorizationRequestBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "Authorization")
    private Authorization mAuthorization;

    public Authorization getAuthorization() {
        return mAuthorization;
    }

    public void setAuthorization(Authorization authorization) {
        mAuthorization = authorization;
    }
}
