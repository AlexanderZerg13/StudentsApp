package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.RecordBooks;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class RecordBooksRequestEnvelop {

    public static RecordBooksRequestEnvelop generate(String userId) {
        RecordBooksRequestEnvelop envelop = new RecordBooksRequestEnvelop();
        envelop.setRecordBooks(new RecordBooks(userId));
        return envelop;
    }

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetRecordbooks")
    @Path("Body")
    private RecordBooks mRecordBooks;

    public RecordBooks getRecordBooks() {
        return mRecordBooks;
    }

    public void setRecordBooks(RecordBooks recordBooks) {
        mRecordBooks = recordBooks;
    }
}
