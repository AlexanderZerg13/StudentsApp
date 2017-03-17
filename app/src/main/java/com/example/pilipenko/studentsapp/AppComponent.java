package com.example.pilipenko.studentsapp;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pilipenko on 17.03.2017.
 */

@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {


}
