package ru.infocom.university;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.interfaces.IToolbar;

public class MainLoginActivity extends AppCompatActivity implements LoginAuthFragment.ILoginAnon, LoginAnonFragment.ILoginAuth, IToolbar {

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
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());

        setContentView(R.layout.activity_main_login);

        Window window = getWindow();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getAttributes().flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

        if (DataPreferenceManager.provideUserPreferences().hasUser(this)) {
            AuthorizationObject object = DataPreferenceManager.provideUserPreferences().getUser(this);
            startActivity(MainContentActivity.newIntent(this, object).setFlags(0));
            finish();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public void goToSettings() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_login_fragmentContainer, SettingsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

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
