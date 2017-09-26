package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

@Root(name = "Recordbook", strict = false)
public class RecordBook {

    @Element(name = "RecordbookId")
    private String mRecordBookId;

    @Element(name = "RecordbookName")
    private String mRecordBookName;

    @Element(name = "SpecialtyName")
    private String mSpecialityName;

    public String getRecordBookId() {
        return mRecordBookId;
    }

    public void setRecordBookId(String recordBookId) {
        mRecordBookId = recordBookId;
    }

    public String getRecordBookName() {
        return mRecordBookName;
    }

    public void setRecordBookName(String recordBookName) {
        mRecordBookName = recordBookName;
    }

    public String getSpecialityName() {
        return mSpecialityName;
    }

    public void setSpecialityName(String specialityName) {
        mSpecialityName = specialityName;
    }
}
