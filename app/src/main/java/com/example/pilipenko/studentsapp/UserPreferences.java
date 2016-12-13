package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.pilipenko.studentsapp.data.AuthorizationObject;

public class UserPreferences {

    private static final String KEY_NAME = "NAME";
    private static final String KEY_PASSWORD = "PASSWORD";
    private static final String KEY_ID = "ID";
    private static final String KEY_PLAN = "PLAN";

    public static void setUser(Context context, AuthorizationObject authorizationObject) {

        if (authorizationObject == null) {
            throw new IllegalArgumentException("AuthorizationObject can't be null");
        }

        if (TextUtils.isEmpty(authorizationObject.getName()) ||
                TextUtils.isEmpty(authorizationObject.getPassword()) ||
                TextUtils.isEmpty(authorizationObject.getId())) {
            throw new IllegalStateException("Some field in AuthorizationObject is empty");
        }

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor
                .remove(KEY_ID)
                .remove(KEY_NAME)
                .remove(KEY_PASSWORD)
                .remove(KEY_PLAN)
                .putString(KEY_ID, authorizationObject.getId())
                .putString(KEY_NAME, authorizationObject.getName())
                .putString(KEY_PASSWORD, authorizationObject.getPassword())
                .putString(KEY_PLAN, authorizationObject.getPlan())
                .apply();
    }

    public static AuthorizationObject getUser(Context context) {
        AuthorizationObject authorizationObject = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sharedPreferences.getString(KEY_NAME, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);
        String id = sharedPreferences.getString(KEY_ID, null);
        String plan = sharedPreferences.getString(KEY_PLAN, null);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(plan)) {
            authorizationObject = new AuthorizationObject();
            authorizationObject.setId(id);
            authorizationObject.setName(name);
            authorizationObject.setPassword(password);
            authorizationObject.setPlan(plan);
        }
        return authorizationObject;
    }

    public static boolean hasUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sharedPreferences.getString(KEY_NAME, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);
        String id = sharedPreferences.getString(KEY_ID, null);
        String plan = sharedPreferences.getString(KEY_PLAN, null);
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(plan);
    }

    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(KEY_NAME).remove(KEY_PASSWORD).remove(KEY_ID).remove(KEY_PLAN).apply();
    }
}
