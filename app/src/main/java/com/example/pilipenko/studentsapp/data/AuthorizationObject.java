package com.example.pilipenko.studentsapp.data;

import android.text.TextUtils;

import java.io.Serializable;

public class AuthorizationObject implements Serializable{
    private String id;
    private String name;
    private String password;

    private int code;
    private String description;

    public AuthorizationObject() {
    }

    public AuthorizationObject(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(id);
    }

    @Override
    public String toString() {
        return "AuthorizationObject{" +
                "code=" + code +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
