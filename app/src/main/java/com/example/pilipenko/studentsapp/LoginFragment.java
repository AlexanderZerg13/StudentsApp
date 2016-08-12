package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maksim88.passwordedittext.PasswordEditText;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {

    private Button mEnterButton;
    private Button mEnterAnonymouslyButton;
    private EditText mNameEditText;
    private PasswordEditText mPasswordEditText;
    private TextView mDescribeTextView;

    private ILoginAnon mLoginAnonActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEnterButton = (Button) v.findViewById(R.id.fragment_login_btn_enter);
        mEnterAnonymouslyButton = (Button) v.findViewById(R.id.fragment_login_btn_enter_anon);
        mNameEditText = (EditText) v.findViewById(R.id.fragment_login_et_name);
        mPasswordEditText = (PasswordEditText) v.findViewById(R.id.fragment_login_et_password);

        mDescribeTextView = (TextView) v.findViewById(R.id.fragment_login_tv_describe);

        LoginButtonOnClickListener buttonListener = new LoginButtonOnClickListener();
        mEnterButton.setOnClickListener(buttonListener);
        mEnterAnonymouslyButton.setOnClickListener(buttonListener);

        LoginTextWatcher editTextTextWatcher = new LoginTextWatcher();
        mNameEditText.addTextChangedListener(editTextTextWatcher);
        mPasswordEditText.addTextChangedListener(editTextTextWatcher);

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
    }

    private void enableError(boolean enabled, String text) {
        if (enabled) {
            if (!TextUtils.isEmpty(text)) {
                mDescribeTextView.setText(getString(R.string.fragment_login_tv_describe_error));
            }
            mDescribeTextView.setTextColor(getResources().getColor(R.color.colorUnderLineError));
            mPasswordEditText.setBackgroundResource(R.drawable.edittext_error);
        } else {
            mDescribeTextView.setText(getString(R.string.fragment_login_tv_describe));
            mDescribeTextView.setTextColor(getResources().getColor(R.color.colorFloatText));
            mPasswordEditText.setBackgroundResource(R.drawable.edittext);
        }
    }

    private class LoginButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_login_btn_enter:
                    CharSequence name = mNameEditText.getText();
                    CharSequence password = mPasswordEditText.getText();

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
        }
    }

    private class DoLoginTask extends AsyncTask<CharSequence, Void, Void> {

        @Override
        protected void onPreExecute() {
            enableUI(false);
        }

        @Override
        protected Void doInBackground(CharSequence... charSequences) {
            CharSequence name = charSequences[0];
            CharSequence password = charSequences[1];

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
//                if (!name.equals("Alex") || !password.equals("123")) {
//
//                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            enableUI(true);
            enableError(true, getString(R.string.fragment_login_tv_describe_error));

        }
    }

    public interface ILoginAnon {
        void goToLoginAnon();
    }
}
