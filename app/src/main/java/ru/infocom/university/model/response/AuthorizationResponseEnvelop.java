package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class AuthorizationResponseEnvelop {

    @Element(name = "Body")
    private AuthorizationResponseBody mAuthorizationResponseBody;

    public AuthorizationResponseBody getBody() {
        return mAuthorizationResponseBody;
    }

    public void setBody(AuthorizationResponseBody authorizationResponseBody) {
        mAuthorizationResponseBody = authorizationResponseBody;
    }
}
