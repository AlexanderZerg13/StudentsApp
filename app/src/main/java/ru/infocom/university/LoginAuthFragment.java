package ru.infocom.university;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.University;
import ru.infocom.university.network.AuthorizationException;
import ru.infocom.university.network.DataRepository;
import rx.Subscription;

import com.afollestad.materialdialogs.MaterialDialog;
import com.maksim88.passwordedittext.PasswordEditText;

public class LoginAuthFragment extends Fragment {

    private static final String TAG = "LoginAuthFragment";

    public static final String LOGIN = "ws";
    public static final String PASS = "ws";

    private University mUniversity;

    private Button mEnterButton;
    private Button mSettingsButton;
    private Button mEnterDemo;
    private EditText mNameEditText;
    private PasswordEditText mPasswordEditText;
    private TextView mDescribeTextView;
    private ProgressBar mProgressBar;
    private TextInputLayout mVuzSelectorEditTextTIL;
    private EditText mVuzSelectorEditText;

    private ILoginAnon mLoginActivity;

    private boolean fieldFill = false;

    private DataRepository mDataRepository;
    private Subscription mDoAuthorizationSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

        mDataRepository = new DataRepository(DataPreferenceManager.provideUserPreferences().getUniversityId(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mSettingsButton = v.findViewById(R.id.fragment_login_btn_settings);
        mEnterButton = v.findViewById(R.id.fragment_login_btn_enter);
        mEnterDemo = v.findViewById(R.id.fragment_login_demo);
        mNameEditText = v.findViewById(R.id.fragment_login_et_user_name);
        mPasswordEditText = v.findViewById(R.id.fragment_login_et_password);

        mDescribeTextView = v.findViewById(R.id.fragment_login_tv_describe);
        mProgressBar = v.findViewById(R.id.fragment_login_progress_bar);

        LoginButtonOnClickListener buttonListener = new LoginButtonOnClickListener();
        mEnterButton.setOnClickListener(buttonListener);
        mEnterDemo.setOnClickListener(buttonListener);
        mSettingsButton.setOnClickListener(buttonListener);

        mVuzSelectorEditTextTIL = v.findViewById(R.id.fragment_login_til_select_university);
        mVuzSelectorEditTextTIL.setHintAnimationEnabled(false);
        mVuzSelectorEditText = v.findViewById(R.id.fragment_login_et_select_university);
        mVuzSelectorEditText.setOnClickListener(buttonListener);

        LoginTextWatcher editTextTextWatcher = new LoginTextWatcher();
        LoginEditorAction loginEditorAction = new LoginEditorAction();
        mNameEditText.addTextChangedListener(editTextTextWatcher);
        mVuzSelectorEditText.addTextChangedListener(editTextTextWatcher);
        mPasswordEditText.addTextChangedListener(editTextTextWatcher);
        //mPasswordEditText.setText("123");

        mPasswordEditText.setOnEditorActionListener(loginEditorAction);
        mNameEditText.setOnEditorActionListener(loginEditorAction);
        return v;
    }

    @Override
    public void onDestroy() {
        if (mDoAuthorizationSubscription != null) {
            mDoAuthorizationSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginActivity = (ILoginAnon) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginActivity = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == MainChooseActivity.KEY_REQUEST_UNIVERSITY) {
            mUniversity = (University) data.getSerializableExtra(ChooseEducationFragment.KEY_RETURN_BASIC);
            mVuzSelectorEditText.setText(mUniversity.getName());
            mDataRepository.setUniversityId(mUniversity.getId());
            DataPreferenceManager.provideUserPreferences().saveUniversityId(this.getActivity(), mUniversity.getId());
        }
    }

    private void enableUI(boolean enabled) {
        mEnterButton.setEnabled(enabled && !(TextUtils.isEmpty(mPasswordEditText.getText()) || TextUtils.isEmpty(mNameEditText.getText())));
        mEnterDemo.setEnabled(enabled);
        mNameEditText.setEnabled(enabled);
        mPasswordEditText.setEnabled(enabled);
        mSettingsButton.setEnabled(enabled);
        mVuzSelectorEditText.setClickable(enabled);
        mPasswordEditText.setClickable(enabled);
        mVuzSelectorEditText.setEnabled(enabled);
        if (enabled) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void checkEnterButton() {
        CharSequence name = mNameEditText.getText();
        CharSequence password = mPasswordEditText.getText();
        CharSequence vuz = mVuzSelectorEditText.getText();
        fieldFill = !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)
                && (!TextUtils.isEmpty(vuz) || BuildConfig.UNIVERSITY_ID != -1);
        mEnterButton.setEnabled(fieldFill);
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

    private void showLoading() {
        enableUI(false);
    }

    private void hideLoading() {
        enableUI(true);
    }

    private void doAuthorization(String name, String password) {
        mDoAuthorizationSubscription = mDataRepository
                .authorization(name, password)
                .doOnSubscribe(LoginAuthFragment.this::showLoading)
                .doOnTerminate(LoginAuthFragment.this::hideLoading)
                .subscribe(authorizationObject -> {
                    Log.i(TAG, "authorizationObject: " + authorizationObject);

                    if (authorizationObject.getRole() == AuthorizationObject.Role.BOTH) {
                        new MaterialDialog.Builder(LoginAuthFragment.this.getActivity())
                                .title(R.string.fragment_login_dialog_role)
                                .cancelable(false)
                                .items(R.array.fragment_login_dialog_role_array)
                                .itemsCallback((dialog, view, which, text) -> {
                                    switch (which) {
                                        case 0:
                                            authorizationObject.setRole(AuthorizationObject.Role.STUDENT);
                                            break;
                                        case 1:
                                            authorizationObject.setRole(AuthorizationObject.Role.TEACHER);
                                            break;
                                    }
                                    goToMainScreen(authorizationObject);
                                })
                                .show();
                    } else {
                        goToMainScreen(authorizationObject);
                    }
                }, throwable -> {
                    Log.i(TAG, "authorizationObject: " + throwable);
                    if (throwable instanceof AuthorizationException) {
                        enableError(true, getString(R.string.fragment_login_tv_describe_error));
                    } else {
                        enableError(true, getString(R.string.fragment_login_tv_describe_error_access));
                    }
                });
    }

    private void goToMainScreen(AuthorizationObject object) {
        DataPreferenceManager.provideUserPreferences().saveUser(getActivity(), object);
        Log.i(TAG, "goToMainScreen: " + DataPreferenceManager.provideUserPreferences().getUser(this.getActivity()));
        startActivity(MainContentActivity.newIntent(getActivity(), object));
        enableError(false, null);
    }

    private class LoginButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String name;
            String password;
            Intent intent;

            switch (view.getId()) {
                case R.id.fragment_login_btn_enter:
                    name = mNameEditText.getText().toString();
                    password = mPasswordEditText.getText().toString();

                    LoginAuthFragment.this.doAuthorization(name, password);
                    break;
                case R.id.fragment_login_et_select_university:
                    intent = MainChooseActivity.newIntent(getActivity(), MainChooseActivity.KEY_REQUEST_UNIVERSITY);
                    startActivityForResult(intent, MainChooseActivity.KEY_REQUEST_UNIVERSITY);
                    break;
                case R.id.fragment_login_btn_settings:
                    mLoginActivity.goToSettings();
                    break;
                case R.id.fragment_login_demo:
                    name = "Иван Иванов";
                    password = "demo";
                    mDataRepository.setUniversityId(0);
                    DataPreferenceManager.provideUserPreferences().saveUniversityId(LoginAuthFragment.this.getActivity(), 0);

                    LoginAuthFragment.this.doAuthorization(name, password);
                    break;
            }
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

    private class LoginEditorAction implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            boolean handled = false;
            if (fieldFill) {
                handled = mEnterButton.performClick();
            } else if (textView == mPasswordEditText){
                handled = mNameEditText.requestFocus();
            }
            return handled;
        }
    }

    public interface ILoginAnon {
        void goToSettings();
    }
}
