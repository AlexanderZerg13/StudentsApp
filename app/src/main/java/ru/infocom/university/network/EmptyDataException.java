package ru.infocom.university.network;

public class EmptyDataException extends Exception {
    public EmptyDataException() {
        super("No Data Exception");
    }

    public EmptyDataException(String message) {
        super(message);
    }

    public EmptyDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
