package ru.infocom.university.model.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class RecordBooksResponseEnvelop {

    @Element(name = "Body")
    private RecordBooksResponseBody mRecordBooksResponseBody;

    public RecordBooksResponseBody getBody() {
        return mRecordBooksResponseBody;
    }

    public void setBody(RecordBooksResponseBody recordBooksResponseBody) {
        mRecordBooksResponseBody = recordBooksResponseBody;
    }
}
