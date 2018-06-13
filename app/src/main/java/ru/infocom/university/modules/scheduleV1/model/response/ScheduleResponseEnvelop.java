package ru.infocom.university.modules.scheduleV1.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.ReturnContainer;

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class ScheduleResponseEnvelop {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetScheduleResponse")
    @Path("Body")
    private ReturnContainer mReturnContainer;

    public ReturnContainer getReturnContainer() {
        return mReturnContainer;
    }

    public void setReturnContainer(ReturnContainer returnContainer) {
        mReturnContainer = returnContainer;
    }
}