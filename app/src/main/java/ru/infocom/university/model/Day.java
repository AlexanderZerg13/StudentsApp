package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.List;

/**
 * Created by Alexander Pilipenko on 27.09.2017.
 */

@Root(name = "Day", strict = false)
public class Day {

    @Element(name = "Date")
    private Date mDate;

    @Element(name = "DayOfWeek")
    private String mDayOfWeek;

    @ElementList(inline = true, required = false)
    private List<ScheduleCell> mScheduleCells;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDayOfWeek() {
        return mDayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        mDayOfWeek = dayOfWeek;
    }

    public List<ScheduleCell> getScheduleCells() {
        return mScheduleCells;
    }

    public void setScheduleCells(List<ScheduleCell> scheduleCells) {
        mScheduleCells = scheduleCells;
    }
}
