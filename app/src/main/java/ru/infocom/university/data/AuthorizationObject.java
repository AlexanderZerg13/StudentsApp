package ru.infocom.university.data;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import ru.infocom.university.model.RecordBook;

public class AuthorizationObject implements Serializable {
    private String mId;
    private String mName;
    private String mPassword;
    private Role mRole;
    private List<RecordBook> mRecordBooks;

    private int code;
    private String description;

    public enum Role {
        TEACHER, STUDENT, BOTH
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

    public List<RecordBook> getRecordBooks() {
        return mRecordBooks;
    }

    public void setRecordBooks(List<RecordBook> recordBooks) {
        mRecordBooks = recordBooks;
    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mId) && mRole != null;
    }

    public Role getRole() {
        return mRole;
    }

    public void setRole(Role role) {
        mRole = role;
    }

    @Override
    public String toString() {
        return "AuthorizationObject{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mRole=" + mRole +
                ", mRecordBooks=" + mRecordBooks +
                ", code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
