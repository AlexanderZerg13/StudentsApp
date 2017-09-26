package ru.infocom.university.model;

import org.simpleframework.xml.Element;

/**
 * Created by Alexander Pilipenko on 25.09.2017.
 */

public class Authorization {

    @Element(name = "UserId")
    private String userId;

    @Element(name = "Login")
    private String login;

    @Element(name = "PasswordHash")
    private String password;

    public Authorization() {
    }

    public Authorization(String userId, String login, String password) {
        this.userId = userId;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}