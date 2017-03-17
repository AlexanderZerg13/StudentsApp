package com.example.pilipenko.studentsapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import com.example.pilipenko.studentsapp.ui.fragment.ChooseEducationFragment;
import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.service.FetchDataIntentService;

public class MainChooseActivity extends AppCompatActivity {

    private static final String TAG = "MainChooseActivity";

    public static final int KEY_REQUEST_UNIVERSITY = 1;
    public static final int KEY_REQUEST_SPECIALITY = 2;

    public static final String REQUEST_CODE = "requestCode";

    private FetchDataReceiver mFetchDataReceiver;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

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

        IntentFilter intentFilter = new IntentFilter(FetchDataIntentService.BROADCAST_ACTION);
        mFetchDataReceiver = new FetchDataReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mFetchDataReceiver,
                intentFilter);

        if (fragment == null) {
            int request = getIntent().getIntExtra(REQUEST_CODE, 0);

            fragment = ChooseEducationFragment.newInstance(request);
            fragmentManager.beginTransaction()
                    .add(R.id.main_choose_fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mFetchDataReceiver);
    }

    private class FetchDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_choose_fragmentContainer);
            if (fragment != null && fragment instanceof MainContentActivity.IFragmentReceiver) {
                ((MainContentActivity.IFragmentReceiver) fragment).onReceive(context, intent);
            }
        }
    }
}
