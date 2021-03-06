package ru.infocom.university;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ru.infocom.university.data.AuthorizationObject;


/*TODO Need to update this class like preferenceManager from m-translator*/
public class DataPreferenceManager {

    private static final String KEY_USER = "USER";
    private static final String KEY_UNIVERSITY_ID = "UNIVERSITY";

    private static DataPreferenceManager sDataPreferenceManager;
    private Gson mGson;

    private DataPreferenceManager() {
        mGson = new GsonBuilder().create();
    }

    public static DataPreferenceManager provideUserPreferences() {
        if (sDataPreferenceManager == null) {
            sDataPreferenceManager = new DataPreferenceManager();
        }

        return sDataPreferenceManager;
    }

    public void saveUniversityId(Context context, int id) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_UNIVERSITY_ID, id).apply();
    }

    public int getUniversityId(Context context) {
        if (BuildConfig.UNIVERSITY_ID != -1) {
            return BuildConfig.UNIVERSITY_ID;
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_UNIVERSITY_ID, 0);
    }

    public void saveUser(@NonNull Context context, @NonNull AuthorizationObject authorizationObject) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(KEY_USER, mGson.toJson(authorizationObject, AuthorizationObject.class)).apply();
    }

    public AuthorizationObject getUser(Context context) {
        AuthorizationObject authorizationObject = null;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(KEY_USER)) {
            authorizationObject = mGson.fromJson(sharedPreferences.getString(KEY_USER, null), AuthorizationObject.class);
        }

        return authorizationObject;
    }

    public boolean hasUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.contains(KEY_USER);
    }

    public void clearUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(KEY_USER).apply();
    }
}
