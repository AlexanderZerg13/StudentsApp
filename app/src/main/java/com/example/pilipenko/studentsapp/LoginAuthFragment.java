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
import android.widget.TextView;

import com.maksim88.passwordedittext.PasswordEditText;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LoginAuthFragment extends Fragment {

    private static final String TAG = "LoginAuthFragment";
    private static final String ADDRESS = "http://web-03:8080/InfoBase-Stud/hs/Authorization/Passwords";

    private Button mEnterButton;
    private Button mEnterAnonymouslyButton;
    private EditText mNameEditText;
    private PasswordEditText mPasswordEditText;
    private TextView mDescribeTextView;

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

        LoginButtonOnClickListener buttonListener = new LoginButtonOnClickListener();
        mEnterButton.setOnClickListener(buttonListener);
        mEnterAnonymouslyButton.setOnClickListener(buttonListener);

        LoginTextWatcher editTextTextWatcher = new LoginTextWatcher();
        mNameEditText.addTextChangedListener(editTextTextWatcher);
        mPasswordEditText.addTextChangedListener(editTextTextWatcher);
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
    }

    private void enableError(boolean enabled, String text) {
        if (enabled) {
            if (!TextUtils.isEmpty(text)) {
                mDescribeTextView.setText(getString(R.string.fragment_login_tv_describe_error));
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

                    new DoLoginTask().execute("Абраменко Алексей Николаевич", "JLxY6C0E");
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
            Log.i("TAG", "beforeTextChanged: " + mPasswordEditText.getPaddingTop() + " " + mPasswordEditText.getPaddingBottom());
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

    private class DoLoginTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            enableUI(false);
        }

        @Override
        protected Void doInBackground(String... strSequences) {
            String name = strSequences[0];
            String password = strSequences[1];

            String baseAuthStr = "d3M6d3M=";
            HttpURLConnection conn = null;
            try {
                URL url = new URL(ADDRESS);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.addRequestProperty("Authorization", "Basic " + baseAuthStr);


                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("login", name.toString()));
                params.add(new Pair<>("hash", new String(Hex.encodeHex(DigestUtils.sha1(password)))));

                OutputStream os = conn.getOutputStream();

                BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                write.write(Utils.getQuery(params));
                write.flush();
                write.close();
                os.close();

                Log.i(TAG, "Header: " + Utils.getHeaderString(conn.getHeaderFields()));

                conn.connect();

                InputStream in = conn.getInputStream();

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                in.close();

                Log.i(TAG, "doInBackground: " + result.toString("UTF-8"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            enableUI(true);

            if (new Random().nextBoolean()) {
                enableError(true, getString(R.string.fragment_login_tv_describe_error));
            } else {
                startActivity(MainContentActivity.newIntent(getActivity()));
            }
        }
    }

    public interface ILoginAnon {
        void goToLoginAnon();
    }
}
