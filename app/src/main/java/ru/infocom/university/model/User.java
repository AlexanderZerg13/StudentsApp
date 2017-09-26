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
}
