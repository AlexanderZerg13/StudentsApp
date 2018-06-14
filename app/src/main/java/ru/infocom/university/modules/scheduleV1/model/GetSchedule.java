package ru.infocom.university.modules.scheduleV1.model;

import org.simpleframework.xml.Element;

import java.util.Date;

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

    @Element(name = "UserRef")
    private String mUserRef;

    @Element(name = "RecordbookRef")
    private String mRecordbookRef;

    public GetSchedule() {
    }

    public GetSchedule(String scheduleObjectType, String scheduleObjectId, String scheduleType, Date dateBegin, Date dateEnd) {
        mScheduleObjectType = scheduleObjectType;
        mScheduleObjectId = scheduleObjectId;
        mScheduleType = scheduleType;
        mDateBegin = dateBegin;
        mDateEnd = dateEnd;
        mRecordbookRef = "";
        mUserRef = "";
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

    public String getUserRef() {
        return mUserRef;
    }

    public void setUserRef(String userRef) {
        mUserRef = userRef;
    }

    public String getRecordbookRef() {
        return mRecordbookRef;
    }

    public void setRecordbookRef(String recordbookRef) {
        mRecordbookRef = recordbookRef;
    }

    @Override
    public String toString() {
        return "GetSchedule{" +
                "mScheduleObjectType='" + mScheduleObjectType + '\'' +
                ", mScheduleObjectId='" + mScheduleObjectId + '\'' +
                ", mScheduleType='" + mScheduleType + '\'' +
                ", mDateBegin=" + mDateBegin +
                ", mDateEnd=" + mDateEnd +
                ", mUserRef='" + mUserRef + '\'' +
                ", mRecordbookRef='" + mRecordbookRef + '\'' +
                '}';
    }
}
