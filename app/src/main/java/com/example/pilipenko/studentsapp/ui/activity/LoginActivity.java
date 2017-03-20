package com.example.pilipenko.studentsapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.pilipenko.studentsapp.StudentsAppApplication;
import com.example.pilipenko.studentsapp.ui.activity.module.LoginActivityModule;
import com.example.pilipenko.studentsapp.ui.fragment.LoginAuthFragment;
import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.utils.UserPreferences;
import com.example.pilipenko.studentsapp.data.AuthorizationObject;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;

public class LoginActivity extends BaseActivity implements IToolbar {

    Fragment loginAnonFragment;
    Fragment loginAuthFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
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
    protected void setupActivityComponent() {
        StudentsAppApplication.get(this).getAppComponent()
                .plus(new LoginActivityModule(this))
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.main_login_fragmentContainer);
        if (fragment == loginAnonFragment) {

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void goToSettings() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_login_fragmentContainer, SettingsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }*/

    @Override
    public void useToolbar(Toolbar toolbar, int strResource) {
        setSupportActionBar(toolbar);

        setToolbarTitle(strResource);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void useToolbarWithBackStack(Toolbar toolbar, int strResource) {

    }

    @Override
    public void setToolbarTitle(int strResource) {
        if (strResource == 0) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            getSupportActionBar().setTitle(strResource);
        }
    }
}
