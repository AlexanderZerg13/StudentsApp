package ru.infocom.university.model.request;

import android.util.Log;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.Date;

import ru.infocom.university.model.GetSchedule;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(name = "Envelope")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope")
public class ScheduleRequestEnvelop {
    public static ScheduleRequestEnvelop generate(String objectType, String objectId, String type, Date begin, Date end) {
        ScheduleRequestEnvelop request = new ScheduleRequestEnvelop();
        request.setGetSchedule(new GetSchedule(objectType, objectId, type, begin, end));

        Log.i("ScheduleRequest okhttp", "generate: " + request);

        return request;
    }

    @Namespace(reference = "http://sgu-infocom.ru/study")
    @Element(name = "GetSchedule")
    @Path("Body")
    private GetSchedule mGetSchedule;

    public GetSchedule getGetSchedule() {
        return mGetSchedule;
    }

    public void setGetSchedule(GetSchedule getSchedule) {
        mGetSchedule = getSchedule;
    }

    @Override
    public String toString() {
        return "ScheduleRequestEnvelop{" +
                "mGetSchedule=" + mGetSchedule +
                '}';
    }
}