package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

public class Return {

    @Element(name = "Error", required = false)
    private Error mErrors;

    @Element(name = "User", required = false)
    private User mUser;

    @ElementList(inline = true, required = false)
    private List<RecordBook> mRecordBooksList;

    @ElementList(inline = true, required = false)
    private List<MarkRecord> mMarkRecordList;

    @ElementList(inline = true, required = false)
    private List<Term> mTermList;

    @ElementList(inline = true, required = false)
    private List<CurriculumLoad> mCurriculumLoadList;

    @ElementList(inline = true, required = false)
    private List<Day> mDayList;

    public Error getErrors() {
        return mErrors;
    }

    public void setErrors(Error errors) {
        mErrors = errors;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public List<RecordBook> getRecordBooksList() {
        return mRecordBooksList;
    }

    public void setRecordBooksList(List<RecordBook> recordBooksList) {
        mRecordBooksList = recordBooksList;
    }

    public List<MarkRecord> getMarkRecordList() {
        return mMarkRecordList;
    }

    public void setMarkRecordList(List<MarkRecord> markRecordList) {
        mMarkRecordList = markRecordList;
    }

    public List<Term> getTermList() {
        return mTermList;
    }

    public void setTermList(List<Term> termList) {
        mTermList = termList;
    }

    public List<CurriculumLoad> getCurriculumLoadList() {
        return mCurriculumLoadList;
    }

    public void setCurriculumLoadList(List<CurriculumLoad> curriculumLoadList) {
        mCurriculumLoadList = curriculumLoadList;
    }

    public List<Day> getDayList() {
        return mDayList;
    }

    public void setDayList(List<Day> dayList) {
        mDayList = dayList;
    }
}
