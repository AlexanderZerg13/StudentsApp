package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.GetRecordBooks;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class RecordBooksRequestBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetRecordbooks")
    private GetRecordBooks mGetRecordBooks;

    public GetRecordBooks getGetRecordBooks() {
        return mGetRecordBooks;
    }

    public void setGetRecordBooks(GetRecordBooks getRecordBooks) {
        mGetRecordBooks = getRecordBooks;
    }
}
