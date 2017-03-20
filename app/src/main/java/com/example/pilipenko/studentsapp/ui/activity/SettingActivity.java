package com.example.pilipenko.studentsapp.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.pilipenko.studentsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pilipenko on 20.03.2017.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.activity_settings_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void setupActivityComponent() {
    }
}
