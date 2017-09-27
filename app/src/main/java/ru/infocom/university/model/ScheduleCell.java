package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(name = "ScheduleCell")
public class ScheduleCell {

    @Element(name = "DateBegin")
    private Date mDateBegin;

    @Element(name = "DateEnd")
    private Date mDateEnd;

    @Element(name = "Lesson", required = false)
    private Lesson mLesson;

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

    public Lesson getLesson() {
        return mLesson;
    }

    public void setLesson(Lesson lesson) {
        mLesson = lesson;
    }
}
