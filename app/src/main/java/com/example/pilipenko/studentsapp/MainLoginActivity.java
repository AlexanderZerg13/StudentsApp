package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import com.example.pilipenko.studentsapp.data.AuthorizationObject;

public class MainLoginActivity extends AppCompatActivity implements LoginAuthFragment.ILoginAnon, LoginAnonFragment.ILoginAuth {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Fragment loginAnonFragment;
    Fragment loginAuthFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MainLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        if (UserPreferences.hasUser(this)) {
            AuthorizationObject object = UserPreferences.getUser(this);
            startActivity(MainContentActivity.newIntent(this, object));
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.main_login_fragmentContainer);

        if (fragment == null) {
            fragment = new LoginAuthFragment();
            loginAuthFragment = fragment;
//            fragment = new ChooseEducationFragment();
            fm.beginTransaction()
                    .add(R.id.main_login_fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.main_login_fragmentContainer);
        if (fragment == loginAnonFragment) {
            goToLoginAuth();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void goToLoginAnon() {
        if (loginAnonFragment == null) {
            loginAnonFragment = LoginAnonFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_login_fragmentContainer, loginAnonFragment)
                .commit();
    }

    @Override
    public void goToLoginAuth() {
        if (loginAuthFragment == null) {
            loginAuthFragment = new LoginAuthFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_login_fragmentContainer, loginAuthFragment)
                .commit();
    }
}
