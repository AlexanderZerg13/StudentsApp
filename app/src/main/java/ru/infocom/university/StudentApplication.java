package ru.infocom.university;

import android.app.Application;

import ru.infocom.university.model.RecordBook;

/**
 * Created by Alexander Pilipenko on 28.09.2017.
 */

public class StudentApplication extends Application {

    /*TODO it is not the best way to save record book in application class*/
    private RecordBook mRecordBookSelected;

    public StudentApplication() {

    }

    public RecordBook getRecordBookSelected() {
        return mRecordBookSelected;
    }

    public void setRecordBookSelected(RecordBook recordBookSelected) {
        mRecordBookSelected = recordBookSelected;
    }
}
