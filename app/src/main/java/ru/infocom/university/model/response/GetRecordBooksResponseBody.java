package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.GetRecordBooksResponse;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class GetRecordBooksResponseBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetRecordbooksResponse")
    private GetRecordBooksResponse mGetRecordBooksResponse;

    public GetRecordBooksResponse getGetRecordBooksResponse() {
        return mGetRecordBooksResponse;
    }

    public void setGetRecordBooksResponse(GetRecordBooksResponse getRecordBooksResponse) {
        mGetRecordBooksResponse = getRecordBooksResponse;
    }
}
