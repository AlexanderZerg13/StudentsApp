package com.example.pilipenko.studentsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choose);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_choose_fragmentContainer);

        if (fragment == null) {
            fragment = ChooseUniversityFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.main_choose_fragmentContainer, fragment)
                    .commit();
        }
    }
}
