package ru.infocom.university;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.University;
import ru.infocom.university.service.LoginIntentService;
import com.maksim88.passwordedittext.PasswordEditText;

public class LoginAuthFragment extends Fragment {

    private static final String TAG = "LoginAuthFragment";

    public static final String LOGIN = "ws";
    public static final String PASS = "ws";

    private University mUniversity;

    private Button mEnterButton;
    private Button mEnterAnonymouslyButton;
    private Button mSettingsButton;
    private Button mEnterDemo;
    private EditText mNameEditText;
    private PasswordEditText mPasswordEditText;
    private TextView mDescribeTextView;
    private ProgressBar mProgressBar;
    private TextInputLayout mVuzSelectorEditTextTIL;
    private EditText mVuzSelectorEditText;

    private ILoginAnon mLoginActivity;

    private LoginReceiver mLoginReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

        IntentFilter LoginIntentFilter = new IntentFilter(LoginIntentService.BROADCAST_ACTION);
        mLoginReceiver = new LoginReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                mLoginReceiver,
                LoginIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        LocalBroadcastManager.getInstance(getContext())
                .unregisterReceiver(mLoginReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mSettingsButton = (Button) v.findViewById(R.id.fragment_login_btn_settings);
        mEnterButton = (Button) v.findViewById(R.id.fragment_login_btn_enter);
        mEnterDemo = (Button) v.findViewById(R.id.fragment_login_demo);
        mEnterAnonymouslyButton = (Button) v.findViewById(R.id.fragment_login_btn_enter_anon);
        mNameEditText = (EditText) v.findViewById(R.id.fragment_login_et_name);
        mPasswordEditText = (PasswordEditText) v.findViewById(R.id.fragment_login_et_password);

        mDescribeTextView = (TextView) v.findViewById(R.id.fragment_login_tv_describe);
        mProgressBar = (ProgressBar) v.findViewById(R.id.fragment_login_progress_bar);

        LoginButtonOnClickListener buttonListener = new LoginButtonOnClickListener();
        mEnterButton.setOnClickListener(buttonListener);
        mEnterDemo.setOnClickListener(buttonListener);
        mEnterAnonymouslyButton.setOnClickListener(buttonListener);
        mSettingsButton.setOnClickListener(buttonListener);

        mVuzSelectorEditTextTIL = (TextInputLayout) v.findViewById(R.id.fragment_login_et_select_vuz_til);
        mVuzSelectorEditTextTIL.setHintAnimationEnabled(false);
        mVuzSelectorEditText = (EditText) v.findViewById(R.id.fragment_login_et_select_vuz);
        mVuzSelectorEditText.setOnClickListener(buttonListener);

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
        return v;
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
        }
    }

    private void enableUI(boolean enabled) {
        mEnterButton.setEnabled(enabled && !(TextUtils.isEmpty(mPasswordEditText.getText()) || TextUtils.isEmpty(mNameEditText.getText())));
        mEnterDemo.setEnabled(enabled);
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

                    //new DoLoginTask().execute(name, password);
                    intent = LoginIntentService.newIntent(getActivity(), name, password, mUniversity.getId());
                    enableUI(false);
                    getActivity().startService(intent);
                    break;
                case R.id.fragment_login_et_select_vuz:
                    intent = MainChooseActivity.newIntent(getActivity(), MainChooseActivity.KEY_REQUEST_UNIVERSITY);
                    startActivityForResult(intent, MainChooseActivity.KEY_REQUEST_UNIVERSITY);
                    break;
                case R.id.fragment_login_btn_enter_anon:
                    mLoginActivity.goToLoginAnon();
                    break;
                case R.id.fragment_login_btn_settings:
                    mLoginActivity.goToSettings();
                    break;
                case R.id.fragment_login_demo:
                    name = "demo demo";
                    password = "demo";

                    intent = LoginIntentService.newIntent(getActivity(), name, password, BuildConfig.DEMO_UNIVERSITY_ID);
                    enableUI(false);
                    getActivity().startService(intent);

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

    private class LoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (getActivity() == null) {
                return;
            }

            AuthorizationObject object = (AuthorizationObject) intent.getSerializableExtra(LoginIntentService.KEY_EXTRA_DATA);
            int idRes = intent.getIntExtra(LoginIntentService.KEY_EXTRA_ERROR, R.string.fragment_login_tv_describe_error_access);

            if (object == null) {
                enableUI(true);
                enableError(true, getString(idRes));
                return;
            }

            if (!object.isSuccess()) {
                enableUI(true);
                enableError(true, getString(R.string.fragment_login_tv_describe_error));
            } else {
                UserPreferences.setUser(context, object);
                System.out.println(UserPreferences.getUser(context));
                startActivity(MainContentActivity.newIntent(getActivity(), object));
                enableError(false, null);
            }
        }
    }

    public interface ILoginAnon {
        void goToLoginAnon();
        void goToSettings();
    }
}
