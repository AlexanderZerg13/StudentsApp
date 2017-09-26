package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
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
        RecordBooksRequestBody body = new RecordBooksRequestBody();
        RecordBooks recordBooks = new RecordBooks(userId);

        envelop.setBody(body);
        body.setRecordBooks(recordBooks);

        return envelop;
    }

    @Element(name = "Body")
    private RecordBooksRequestBody mGetRecordBooksBody;

    public RecordBooksRequestBody getBody() {
        return mGetRecordBooksBody;
    }

    public void setBody(RecordBooksRequestBody getRecordBooksBody) {
        mGetRecordBooksBody = getRecordBooksBody;
    }
}
