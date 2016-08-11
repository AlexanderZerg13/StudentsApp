package com.example.pilipenko.studentsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maksim88.passwordedittext.PasswordEditText;

import java.util.StringJoiner;

public class LoginFragment extends Fragment {

    private Button mEnterButton;
    private Button mEnterAnonymouslyButton;
    private TextView mNameTextView;
    private PasswordEditText mPasswordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEnterButton = (Button) v.findViewById(R.id.fragment_login_btn_enter);
        mEnterAnonymouslyButton = (Button) v.findViewById(R.id.fragment_login_btn_enter_anon);
        mNameTextView = (EditText) v.findViewById(R.id.fragment_login_et_name);
        mPasswordEditText = (PasswordEditText) v.findViewById(R.id.fragment_login_et_password);

        LoginButtonOnClickListener buttonListener = new LoginButtonOnClickListener();
        mEnterButton.setOnClickListener(buttonListener);
        mEnterAnonymouslyButton.setOnClickListener(buttonListener);

        LoginTextWatcher editTextTextWatcher = new LoginTextWatcher();
        mNameTextView.addTextChangedListener(editTextTextWatcher);
        mPasswordEditText.addTextChangedListener(editTextTextWatcher);

        return v;
    }

    private class LoginButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_login_btn_enter:

                    break;
                case R.id.fragment_login_btn_enter_anon:

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
            CharSequence name = mNameTextView.getText();
            CharSequence password = mPasswordEditText.getText();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                mEnterButton.setEnabled(false);
            } else {
                mEnterButton.setEnabled(true);
            }
        }
    }
}
