package ru.infocom.university.network;

/**
 * Created by Alexander Pilipenko on 28.09.2017.
 */

public class AuthorizationException extends Exception {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
