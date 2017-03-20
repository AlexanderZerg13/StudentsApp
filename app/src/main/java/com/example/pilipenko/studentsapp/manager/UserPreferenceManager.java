package com.example.pilipenko.studentsapp.manager;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.data.AuthorizationObject;

/**
 * Created by pilipenko on 20.03.2017.
 */

public class UserPreferenceManager {
    private static final String KEY_NAME = "NAME";
    private static final String KEY_PASSWORD = "PASSWORD";
    private static final String KEY_ID = "ID";
    private static final String KEY_PLAN = "PLAN";
    private static final String KEY_ROLE = "ROLE";

    private Application mApplication;

    public UserPreferenceManager(Application application) {
        mApplication = application;
    }

    public SharedPreferences getSharePreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplication);
    }

    public String getHostUniversity() {
        return getSharePreferences().getString(
                mApplication.getString(R.string.settings_key_endpoint_university),
                mApplication.getString(R.string.settings_default_endpoint_university));
    }

    // Authorization
    public void setUser(AuthorizationObject authorizationObject) {
        if (authorizationObject == null) {
            throw new IllegalArgumentException("AuthorizationObject can't be null");
        }
        if (TextUtils.isEmpty(authorizationObject.getName()) ||
                TextUtils.isEmpty(authorizationObject.getPassword()) ||
                TextUtils.isEmpty(authorizationObject.getId()) ||
                authorizationObject.getRole() == null) {
            throw new IllegalStateException("Some field in AuthorizationObject is empty");
        }
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor
                .remove(KEY_ID)
                .remove(KEY_NAME)
                .remove(KEY_PASSWORD)
                .remove(KEY_PLAN)
                .remove(KEY_ROLE)
                .putString(KEY_ID, authorizationObject.getId())
                .putString(KEY_NAME, authorizationObject.getName())
                .putString(KEY_PASSWORD, authorizationObject.getPassword())
                .putString(KEY_PLAN, authorizationObject.getPlan())
                .putString(KEY_ROLE, authorizationObject.getRole().toString())
                .apply();
        System.out.println("saved");
    }

    public AuthorizationObject getUser() {
        AuthorizationObject authorizationObject = null;
        SharedPreferences sharedPreferences = getSharePreferences();
        String name = sharedPreferences.getString(KEY_NAME, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);
        String id = sharedPreferences.getString(KEY_ID, null);
        String plan = sharedPreferences.getString(KEY_PLAN, null);
        String role = sharedPreferences.getString(KEY_ROLE, null);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(role)) {
            authorizationObject = new AuthorizationObject();
            authorizationObject.setId(id);
            authorizationObject.setName(name);
            authorizationObject.setPassword(password);
            authorizationObject.setPlan(plan);
            authorizationObject.setRole(role);
        }
        return authorizationObject;
    }

    public boolean hasUser() {
        SharedPreferences sharedPreferences = getSharePreferences();
        String name = sharedPreferences.getString(KEY_NAME, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);
        String id = sharedPreferences.getString(KEY_ID, null);
        String plan = sharedPreferences.getString(KEY_PLAN, null);
        String role = sharedPreferences.getString(KEY_ROLE, null);
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(role);
    }

    public void clearUser() {
        SharedPreferences sharedPreferences = getSharePreferences();
        sharedPreferences.edit().remove(KEY_NAME).remove(KEY_PASSWORD).remove(KEY_ID).remove(KEY_PLAN).remove(KEY_ROLE).apply();
    }
}
