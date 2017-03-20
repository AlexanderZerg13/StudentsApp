package com.example.pilipenko.studentsapp;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import timber.log.Timber;

/**
 * Created by pilipenko on 17.03.2017.
 */

public class StudentsAppApplication extends Application {

    private AppComponent appComponent;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static StudentsAppApplication get(Context context) {
        return (StudentsAppApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        initAppComponent();
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
