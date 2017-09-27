package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.ReturnContainer;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

public class CurriculumTermsResponseBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetCurriculumTermsResponse")
    private ReturnContainer mReturnContainer;

    public ReturnContainer getReturnContainer() {
        return mReturnContainer;
    }

    public void setReturnContainer(ReturnContainer returnContainer) {
        mReturnContainer = returnContainer;
    }
}
