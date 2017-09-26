package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.AuthorizationResponse;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

@Root(name = "soap:Envelope")
@NamespaceList({
        @Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope")
})
public class AuthorizationResponseEnvelop {

    @Element(name = "Body")
    private AuthorizationResponseBody mAuthorizationResponseBody;

    public AuthorizationResponseBody getAuthorizationResponseBody() {
        return mAuthorizationResponseBody;
    }

    public void setAuthorizationResponseBody(AuthorizationResponseBody authorizationResponseBody) {
        mAuthorizationResponseBody = authorizationResponseBody;
    }
}
