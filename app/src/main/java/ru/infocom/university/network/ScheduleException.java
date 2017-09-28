package ru.infocom.university.network;

/**
 * Created by Alexander Pilipenko on 28.09.2017.
 */

public class ScheduleException extends Exception {
    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
