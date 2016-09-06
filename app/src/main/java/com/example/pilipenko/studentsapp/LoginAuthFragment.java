package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
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

import com.example.pilipenko.studentsapp.data.AuthorizationObject;
import com.maksim88.passwordedittext.PasswordEditText;

import android.util.Base64;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginAuthFragment extends Fragment {

    private static final String TAG = "LoginAuthFragment";

    private static final String ADDRESS_AUTH = "http://web-03:8080/InfoBase-Stud/hs/Authorization/Passwords";

    public static final String LOGIN = "ws";
    public static final String PASS = "ws";

    private Button mEnterButton;
    private Button mEnterAnonymouslyButton;
    private EditText mNameEditText;
    private PasswordEditText mPasswordEditText;
    private TextView mDescribeTextView;
    private ProgressBar mProgressBar;

    private ILoginAnon mLoginAnonActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEnterButton = (Button) v.findViewById(R.id.fragment_login_btn_enter);
        mEnterAnonymouslyButton = (Button) v.findViewById(R.id.fragment_login_btn_enter_anon);
        mNameEditText = (EditText) v.findViewById(R.id.fragment_login_et_name);
        mPasswordEditText = (PasswordEditText) v.findViewById(R.id.fragment_login_et_password);

        mDescribeTextView = (TextView) v.findViewById(R.id.fragment_login_tv_describe);
        mProgressBar = (ProgressBar) v.findViewById(R.id.fragment_login_progress_bar);

        LoginButtonOnClickListener buttonListener = new LoginButtonOnClickListener();
        mEnterButton.setOnClickListener(buttonListener);
        mEnterAnonymouslyButton.setOnClickListener(buttonListener);

        LoginTextWatcher editTextTextWatcher = new LoginTextWatcher();
        mNameEditText.addTextChangedListener(editTextTextWatcher);
        mNameEditText.setText("Абраменко Алексей Николаевич");
        mPasswordEditText.addTextChangedListener(editTextTextWatcher);
        mPasswordEditText.setText("JLxY6C0E");
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
        mLoginAnonActivity = (ILoginAnon) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginAnonActivity = null;
    }

    private void enableUI(boolean enabled) {
        mEnterButton.setEnabled(enabled);
        mEnterAnonymouslyButton.setEnabled(enabled);
        mNameEditText.setEnabled(enabled);
        mPasswordEditText.setEnabled(enabled);
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
            mDescribeTextView.setText(getString(R.string.fragment_login_tv_describe));
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
            switch (view.getId()) {
                case R.id.fragment_login_btn_enter:
                    String name = mNameEditText.getText().toString();
                    String password = mPasswordEditText.getText().toString();

                    new DoLoginTask().execute(name, password);
                    break;
                case R.id.fragment_login_btn_enter_anon:
                    mLoginAnonActivity.goToLoginAnon();
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
            CharSequence name = mNameEditText.getText();
            CharSequence password = mPasswordEditText.getText();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                mEnterButton.setEnabled(false);
            } else {
                mEnterButton.setEnabled(true);
            }
            //костыль
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mPasswordEditText.setPadding(0, 8, 0, 16);
            }
        }
    }

    private class DoLoginTask extends AsyncTask<String, Void, AuthorizationObject> {

        private int idRes = R.string.fragment_login_tv_describe_error_access;

        @Override
        protected void onPreExecute() {
            enableUI(false);
        }

        @Override
        protected AuthorizationObject doInBackground(String... strSequences) {
            AuthorizationObject authorizationObject = null;
            String name = strSequences[0];
            String password = strSequences[1];

            if (!FetchUtils.isNetworkAvailableAndConnected(getActivity())) {
                idRes = R.string.fragment_login_tv_describe_error_internet;
                return null;
            }

            try {
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("login", name));
                params.add(new Pair<>("hash", new String(Hex.encodeHex(DigestUtils.sha1(password)))));

                byte[] bytes = FetchUtils.doPostRequest(LOGIN, PASS, ADDRESS_AUTH, params);

                authorizationObject = Utils.parseResponseAuthorizationObject(new ByteArrayInputStream(bytes));

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            return authorizationObject;
        }

        @Override
        protected void onPostExecute(AuthorizationObject object) {


            Log.i(TAG, "onPostExecute: " + object);

            if (object == null) {
                enableUI(true);
                enableError(true, getString(idRes));
                return;
            }

            if (!object.isSuccess()) {
                enableUI(true);
                enableError(true, getString(R.string.fragment_login_tv_describe_error));
            } else {
                UserPreferences.setUser(getActivity(), object);
                startActivity(MainContentActivity.newIntent(getActivity(), object));
                enableError(false, null);
            }
        }
    }

    public interface ILoginAnon {
        void goToLoginAnon();
    }
}
