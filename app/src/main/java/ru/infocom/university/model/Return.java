package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

public class Return {

    @Element(name = "Error", required = false)
    private Error mErrors;

    @Element(name = "User", required = false)
    private User mUser;

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
}
