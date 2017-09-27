package ru.infocom.university.utils;

import org.simpleframework.xml.transform.Transform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

public class DateFormatTransformer implements Transform<Date>{

    private DateFormat mDateFormatShot = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
    private DateFormat mDateFormatLong = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.US);

    @Override
    public Date read(String value) throws Exception {
        if (value.contains("T")) {
            return mDateFormatLong.parse(value);
        }
        return mDateFormatShot.parse(value);
    }

    @Override
    public String write(Date value) throws Exception {
        return mDateFormatShot.format(value);
    }
}
