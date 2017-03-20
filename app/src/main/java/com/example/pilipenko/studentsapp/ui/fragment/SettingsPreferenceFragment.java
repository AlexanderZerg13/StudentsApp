package com.example.pilipenko.studentsapp.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.StudentsAppApplication;
import com.example.pilipenko.studentsapp.manager.UserPreferenceManager;

import javax.inject.Inject;

public class SettingsPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    UserPreferenceManager mUserPreferenceManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        StudentsAppApplication.get(getActivity()).getAppComponent().inject(this);

        SharedPreferences sharedPreferences = mUserPreferenceManager.getSharePreferences();
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.settings_key_endpoint_university));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference == null) {
            return;
        }
        if (preference instanceof EditTextPreference) {
            EditTextPreference editPreference = (EditTextPreference) preference;
            preference.setSummary(editPreference.getText());
        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
    }
}
