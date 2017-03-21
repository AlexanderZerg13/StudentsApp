package com.example.pilipenko.studentsapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.StudentsAppApplication;
import com.example.pilipenko.studentsapp.data.University;
import com.example.pilipenko.studentsapp.data.api.UserLoginManager;
import com.example.pilipenko.studentsapp.ui.activity.module.LoginActivityModule;
import com.example.pilipenko.studentsapp.ui.fragment.ChooseEducationFragment;
import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.utils.UserPreferences;
import com.example.pilipenko.studentsapp.data.AuthorizationObject;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.maksim88.passwordedittext.PasswordEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements IToolbar {

    private University mUniversity;

    @Inject
    UserLoginManager mUserLoginManager;

    @BindView(R.id.activity_login_enter_btn)
    public Button mEnterButton;
    @BindView(R.id.activity_login_enter_anon_btn)
    public Button mEnterAnonymouslyButton;
    @BindView(R.id.activity_login_settings_btn)
    public Button mSettingsButton;
    @BindView(R.id.activity_login_name_edit_text)
    public EditText mNameEditText;
    @BindView(R.id.activity_login_password_edit_text)
    public PasswordEditText mPasswordEditText;
    @BindView(R.id.activity_login_describe_text_view)
    public TextView mDescribeTextView;
    @BindView(R.id.activity_login_progress_bar)
    public ProgressBar mProgressBar;
    @BindView(R.id.activity_login_select_university_text_layout)
    public TextInputLayout mVuzSelectorEditTextTIL;
    @BindView(R.id.activity_login_select_university_edit_text)
    public EditText mVuzSelectorEditText;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (UserPreferences.hasUser(this)) {
            AuthorizationObject object = UserPreferences.getUser(this);
            startActivity(MainContentActivity.newIntent(this, object));
        }

        mVuzSelectorEditTextTIL.setHintAnimationEnabled(false);
        LoginTextWatcher editTextTextWatcher = new LoginTextWatcher();
        mNameEditText.addTextChangedListener(editTextTextWatcher);
        //mNameEditText.setText("Кузнецов Александр Владимирович");
        mVuzSelectorEditText.addTextChangedListener(editTextTextWatcher);
        mPasswordEditText.addTextChangedListener(editTextTextWatcher);
        //mPasswordEditText.setText("123");
        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_GO) {
                    if (TextUtils.isEmpty(mNameEditText.getText().toString())) {
                        handled = mNameEditText.requestFocus();
                    } else {
                        handled = mEnterButton.performClick();
                    }
                }
                return handled;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == ChooseUniversityActivity.KEY_REQUEST_UNIVERSITY) {
            mUniversity = (University) data.getSerializableExtra(ChooseEducationFragment.KEY_RETURN_BASIC);
            mVuzSelectorEditText.setText(mUniversity.getName());
        }
    }

    @OnClick(R.id.activity_login_enter_btn)
    void onEnterButtonClick() {
        String name = mNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        //TODO login process
        mUserLoginManager.doLogin(name, password);
        enableUI(false);
    }

    @OnClick(R.id.activity_login_select_university_edit_text)
    void onSelectUniversityClick() {
        Intent intent = ChooseUniversityActivity.newIntent(this, ChooseUniversityActivity.KEY_REQUEST_UNIVERSITY);
        startActivityForResult(intent, ChooseUniversityActivity.KEY_REQUEST_UNIVERSITY);
    }

    @OnClick(R.id.activity_login_settings_btn)
    void onSettingsClick() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void setupActivityComponent() {
        StudentsAppApplication.get(this).getAppComponent()
                .plus(new LoginActivityModule(this))
                .inject(this);
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

    private void enableUI(boolean enabled) {
        mEnterButton.setEnabled(enabled);
        mEnterAnonymouslyButton.setEnabled(enabled);
        mNameEditText.setEnabled(enabled);
        mPasswordEditText.setEnabled(enabled);
        mSettingsButton.setEnabled(enabled);
        mVuzSelectorEditText.setClickable(enabled);
        mPasswordEditText.setClickable(enabled);
        mVuzSelectorEditText.setEnabled(enabled);
        if (enabled) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void enableError(boolean enabled, String text) {
        if (enabled) {
            if (TextUtils.isEmpty(text)) {
                mDescribeTextView.setText(getString(R.string.fragment_login_tv_describe_error));
            } else {
                mDescribeTextView.setText(text);
            }
            mDescribeTextView.setTextColor(getResources().getColor(R.color.colorRed1));
            mPasswordEditText.setBackgroundResource(R.drawable.edit_text_login_state_wrong);
        } else {
            mDescribeTextView.setText("");
            mDescribeTextView.setTextColor(getResources().getColor(R.color.colorBlack_38a));
            mPasswordEditText.setBackgroundResource(R.drawable.edit_text_login_state_normal);
        }
        //костыль
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mPasswordEditText.setPadding(0, 8, 0, 16);
        }
    }

    private void checkEnterButton() {
        CharSequence name = mNameEditText.getText();
        CharSequence password = mPasswordEditText.getText();
        CharSequence vuz = mVuzSelectorEditText.getText();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(vuz)) {
            mEnterButton.setEnabled(false);
            mPasswordEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else {
            mEnterButton.setEnabled(true);
            mPasswordEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
        }
    }

    private class LoginTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            enableError(false, null);
            checkEnterButton();
            //костыль
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mPasswordEditText.setPadding(0, 8, 0, 16);
            }
        }
    }
}
