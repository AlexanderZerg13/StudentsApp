package com.example.pilipenko.studentsapp;

import com.example.pilipenko.studentsapp.data.api.UniversityApiModule;
import com.example.pilipenko.studentsapp.ui.activity.component.LoginActivityComponent;
import com.example.pilipenko.studentsapp.ui.activity.module.LoginActivityModule;
import com.example.pilipenko.studentsapp.ui.fragment.SettingsPreferenceFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pilipenko on 17.03.2017.
 */

@Singleton
@Component(
        modules = {
                AppModule.class,
                UniversityApiModule.class
        }
)
public interface AppComponent {

        LoginActivityComponent plus(LoginActivityModule activityModule);

        SettingsPreferenceFragment inject(SettingsPreferenceFragment settingsPreferenceFragment);
}
