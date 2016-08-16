package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.materialdrawer.DrawerBuilder;


public class MainContentActivity extends AppCompatActivity {

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MainContentActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        new DrawerBuilder().withActivity(this).build();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_content_fragmentContainer);

        if (fragment == null) {
            fragment = BasicFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.main_content_fragmentContainer, fragment)
                    .commit();
        }
    }
}
