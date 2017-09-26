package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class User {

    @Element(name = "UserId")
    private String mUserId;

    @Element(name = "Login")
    private String mLogin;

    @Element(name = "PasswordHash")
    private String mPasswordHash;

    @ElementList(inline = true)
    private List<Roles> mRolesList;

    public User() {
    }

    public User(String userId, String login, String passwordHash, List<Roles> rolesList) {
        mUserId = userId;
        mLogin = login;
        mPasswordHash = passwordHash;
        mRolesList = rolesList;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getPasswordHash() {
        return mPasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        mPasswordHash = passwordHash;
    }

    public List<Roles> getRolesList() {
        return mRolesList;
    }

    public void setRolesList(List<Roles> rolesList) {
        mRolesList = rolesList;
    }
}
