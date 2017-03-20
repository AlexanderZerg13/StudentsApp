package com.example.pilipenko.studentsapp.ui.activity.module;

import com.example.pilipenko.studentsapp.ui.activity.ActivityScope;
import com.example.pilipenko.studentsapp.ui.activity.LoginActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pilipenko on 20.03.2017.
 */

@Module
public class LoginActivityModule {

    private LoginActivity mLoginActivity;

    public LoginActivityModule(LoginActivity loginActivity) {
        mLoginActivity = loginActivity;
    }

    @ActivityScope
    @Provides
    public LoginActivity provideLoginActivity() {
        return mLoginActivity;
    }
}
