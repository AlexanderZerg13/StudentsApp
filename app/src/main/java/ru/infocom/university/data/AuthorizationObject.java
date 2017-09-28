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

    public AuthorizationObject() {
    }

    public AuthorizationObject(String id, String name, String password, String role) {
        this.mId = id;
        this.mName = name;
        this.mPassword = password;
        this.mRole = Role.valueOf(role.toUpperCase());
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
        if (mRole == null) {
            return null;
        } else {
            return mRole;
        }
    }

    public void setRole(String role) {
        if (mRole != null && mRole.equals(Role.BOTH)) {
            return;
        }
        Role roleArg = Role.valueOf(role.toUpperCase());
        if (mRole == null) {
            mRole = roleArg;
        } else if (mRole != roleArg) {
            mRole = Role.BOTH;
        }
    }

    public void setRole(Role role) {
        mRole = role;
    }

    @Override
    public String toString() {
        return "AuthorizationObject{" +
                "code=" + code +
                ", mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", description='" + description + '\'' +
                ", role='" + mRole + '\'' +
                '}';
    }
}
