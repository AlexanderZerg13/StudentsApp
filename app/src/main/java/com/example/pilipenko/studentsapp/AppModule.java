package com.example.pilipenko.studentsapp;

import android.app.Application;

import com.example.pilipenko.studentsapp.manager.UserPreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pilipenko on 17.03.2017.
 */
@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public UserPreferenceManager providePreferenceManager(Application application) {
        return new UserPreferenceManager(application);
    }
}
