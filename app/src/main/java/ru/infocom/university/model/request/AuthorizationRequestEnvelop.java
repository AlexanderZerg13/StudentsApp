package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.Authorization;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class AuthorizationRequestEnvelop {

    public static AuthorizationRequestEnvelop generate(String userId, String login, String password) {
        AuthorizationRequestEnvelop envelop = new AuthorizationRequestEnvelop();
        envelop.setAuthorization(new Authorization(userId, login, password));
        return envelop;
    }

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "Authorization")
    @Path("Body")
    private Authorization mAuthorization;

    public Authorization getAuthorization() {
        return mAuthorization;
    }

    public void setAuthorization(Authorization authorization) {
        mAuthorization = authorization;
    }
}
