package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
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
        AuthorizationRequestBody body = new AuthorizationRequestBody();
        Authorization authorization = new Authorization(userId, login, password);

        envelop.setBody(body);
        body.setAuthorization(authorization);

        return envelop;
    }

    @Element(name = "Body")
    private AuthorizationRequestBody mAuthorizationBody;

    public AuthorizationRequestBody getBody() {
        return mAuthorizationBody;
    }

    public void setBody(AuthorizationRequestBody authorizationBody) {
        mAuthorizationBody = authorizationBody;
    }
}
