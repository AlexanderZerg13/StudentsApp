package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import ru.infocom.university.model.RecordBooks;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class RecordBooksRequestBody {

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetRecordbooks")
    private RecordBooks mRecordBooks;

    public RecordBooks getRecordBooks() {
        return mRecordBooks;
    }

    public void setRecordBooks(RecordBooks recordBooks) {
        mRecordBooks = recordBooks;
    }
}
