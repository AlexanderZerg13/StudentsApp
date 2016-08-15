package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainChooseActivity extends AppCompatActivity {


    public static final int KEY_REQUEST_UNIVERSITY = 1;
    public static final int KEY_REQUEST_SPECIALITY = 2;

    public static final String REQUEST_CODE = "requestCode";

    public static Intent newIntent(Context packageContext, int requestCode) {
        if (requestCode != KEY_REQUEST_SPECIALITY && requestCode != KEY_REQUEST_UNIVERSITY) {
            throw new IllegalArgumentException("illegal requestCode");
        }
        Intent intent = new Intent(packageContext, MainChooseActivity.class);
        intent.putExtra(MainChooseActivity.REQUEST_CODE, requestCode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choose);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_choose_fragmentContainer);

        if (fragment == null) {
            int request = getIntent().getIntExtra(REQUEST_CODE, 0);

            fragment = ChooseEducationFragment.newInstance(request);
            fragmentManager.beginTransaction()
                    .add(R.id.main_choose_fragmentContainer, fragment)
                    .commit();
        }
    }
}
