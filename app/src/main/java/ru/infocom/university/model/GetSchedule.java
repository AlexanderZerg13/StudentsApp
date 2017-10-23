package ru.infocom.university.model;

import org.simpleframework.xml.Element;

import java.util.Date;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

public class GetSchedule {

    @Element(name = "ScheduleObjectType")
    private String mScheduleObjectType;

    @Element(name = "ScheduleObjectId")
    private String mScheduleObjectId;

    @Element(name = "ScheduleType")
    private String mScheduleType;

    @Element(name = "DateBegin")
    private Date mDateBegin;

    @Element(name = "DateEnd")
    private Date mDateEnd;

    public GetSchedule() {
    }

    public GetSchedule(String scheduleObjectType, String scheduleObjectId, String scheduleType, Date dateBegin, Date dateEnd) {
        mScheduleObjectType = scheduleObjectType;
        mScheduleObjectId = scheduleObjectId;
        mScheduleType = scheduleType;
        mDateBegin = dateBegin;
        mDateEnd = dateEnd;
    }

    public String getScheduleObjectType() {
        return mScheduleObjectType;
    }

    public void setScheduleObjectType(String scheduleObjectType) {
        mScheduleObjectType = scheduleObjectType;
    }

    public String getScheduleObjectId() {
        return mScheduleObjectId;
    }

    public void setScheduleObjectId(String scheduleObjectId) {
        mScheduleObjectId = scheduleObjectId;
    }

    public String getScheduleType() {
        return mScheduleType;
    }

    public void setScheduleType(String scheduleType) {
        mScheduleType = scheduleType;
    }

    public Date getDateBegin() {
        return mDateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        mDateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return mDateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        mDateEnd = dateEnd;
    }

    @Override
    public String toString() {
        return "GetSchedule{" +
                "mScheduleObjectType='" + mScheduleObjectType + '\'' +
                ", mScheduleObjectId='" + mScheduleObjectId + '\'' +
                ", mScheduleType='" + mScheduleType + '\'' +
                ", mDateBegin=" + mDateBegin +
                ", mDateEnd=" + mDateEnd +
                '}';
    }
}
