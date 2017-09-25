package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

@Root(name = "soap:Envelope")
@NamespaceList({
        @Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope")
})
public class AuthorizationRequestEnvelop {

    @Element(name = "soap:Body")
    private AuthorizationRequestBody mAuthorizationBody;

    public AuthorizationRequestBody getAuthorizationBody() {
        return mAuthorizationBody;
    }

    public void setAuthorizationBody(AuthorizationRequestBody authorizationBody) {
        mAuthorizationBody = authorizationBody;
    }
}
