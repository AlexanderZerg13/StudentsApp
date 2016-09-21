package com.example.pilipenko.studentsapp.data;

import android.text.TextUtils;

import java.io.Serializable;

public class AuthorizationObject implements Serializable{
    private String mId;
    private String mName;
    private String mPassword;
    private String mPlan;

    private int code;
    private String description;

    public AuthorizationObject() {
    }

    public AuthorizationObject(String id, String name, String password, String plan) {
        this.mId = id;
        this.mName = name;
        this.mPassword = password;
        this.mPlan = plan;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
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

    public String getPlan() {
        return mPlan;
    }

    public void setPlan(String plan) {
        mPlan = plan;
    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mId);
    }


    @Override
    public String toString() {
        return "AuthorizationObject{" +
                "code=" + code +
                ", mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
