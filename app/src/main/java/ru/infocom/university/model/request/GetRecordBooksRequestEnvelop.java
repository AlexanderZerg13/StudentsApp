package ru.infocom.university.model.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import ru.infocom.university.model.GetRecordBooks;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class GetRecordBooksRequestEnvelop {

    public static GetRecordBooksRequestEnvelop generateGetRecordBooksRequestEnvelop(String userId) {
        GetRecordBooksRequestEnvelop envelop = new GetRecordBooksRequestEnvelop();
        GetRecordBooksRequestBody body = new GetRecordBooksRequestBody();
        GetRecordBooks getRecordBooks = new GetRecordBooks(userId);

        envelop.setBody(body);
        body.setGetRecordBooks(getRecordBooks);

        return envelop;
    }

    @Element(name = "Body")
    private GetRecordBooksRequestBody mGetRecordBooksBody;

    public GetRecordBooksRequestBody getBody() {
        return mGetRecordBooksBody;
    }

    public void setBody(GetRecordBooksRequestBody getRecordBooksBody) {
        mGetRecordBooksBody = getRecordBooksBody;
    }
}
