package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.ReturnContainer;


/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class AuthorizationResponseEnvelop {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "AuthorizationResponse")
    @Path("Body")
    private ReturnContainer mReturnContainer;

    public ReturnContainer getReturnContainer() {
        return mReturnContainer;
    }

    public void setReturnContainer(ReturnContainer returnContainer) {
        mReturnContainer = returnContainer;
    }
}
