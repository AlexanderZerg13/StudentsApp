package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.AuthorizationResponse;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

public class AuthorizationResponseBody {

    @Element(name = "AuthorizationResponse")
    @Namespace(reference = "http://sgu-infocom.ru/study")
    private AuthorizationResponse mAuthorizationResponse;

    public AuthorizationResponse getAuthorizationResponse() {
        return mAuthorizationResponse;
    }

    public void setAuthorizationResponse(AuthorizationResponse authorizationResponse) {
        mAuthorizationResponse = authorizationResponse;
    }
}