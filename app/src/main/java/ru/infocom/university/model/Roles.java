package ru.infocom.university.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */
@Root(name = "Roles")
public class Roles {

    @Element(name = "Role")
    private String mRole;

    public Roles() {
    }

    public Roles(String role) {
        mRole = role;
    }

    public String getRole() {
        return mRole;
    }

    public void setRole(String role) {
        mRole = role;
    }
}
