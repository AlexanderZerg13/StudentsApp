package com.example.pilipenko.studentsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainLoginActivity extends AppCompatActivity implements LoginAuthFragment.ILoginAnon, LoginAnonFragment.ILoginAuth {

    private static final String KEY_FRAGMENT_LOGIN_AUTH = "FRAGMENT_LOGIN_AUTH";
    private static final String KEY_FRAGMENT_LOGIN_ANON = "FRAGMENT_LOGIN_ANON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            //fragment = new LoginAuthFragment();
            fragment = new ChooseUniversityFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public void goToLoginAnon() {
        Fragment fragment = LoginAnonFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)

                .commit();
    }

    @Override
    public void goToLoginAuth() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
