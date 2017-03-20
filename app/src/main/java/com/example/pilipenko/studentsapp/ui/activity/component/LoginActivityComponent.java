package com.example.pilipenko.studentsapp.ui.activity.component;

import com.example.pilipenko.studentsapp.ui.activity.ActivityScope;
import com.example.pilipenko.studentsapp.ui.activity.LoginActivity;
import com.example.pilipenko.studentsapp.ui.activity.module.LoginActivityModule;

import dagger.Subcomponent;

/**
 * Created by pilipenko on 20.03.2017.
 */
@ActivityScope
@Subcomponent(
        modules = LoginActivityModule.class
)
public interface LoginActivityComponent {
    LoginActivity inject(LoginActivity mainLoginActivity);
}
