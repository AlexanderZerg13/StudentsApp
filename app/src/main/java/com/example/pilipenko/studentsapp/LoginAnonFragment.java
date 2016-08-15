package com.example.pilipenko.studentsapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.University;

public class LoginAnonFragment extends Fragment {

    private Button mEnterButton;
    private Button mEnterAuthorizationButton;
    private TextInputLayout mVuzSelectorEditTextTIL;
    private TextInputLayout mSpecialitySelectorEditTextTIL;
    private EditText mVuzSelectorEditText;
    private EditText mSpecialitySelectorEditText;

    private ILoginAuth mLoginAuth;

    private static final int KEY_REQUEST_UNIVERSITY = 1;
    private static final int KEY_REQUEST_SPECIALITY = 2;

    public static LoginAnonFragment newInstance() {
        
        Bundle args = new Bundle();
        
        LoginAnonFragment fragment = new LoginAnonFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_anon, container, false);

        mEnterButton = (Button) v.findViewById(R.id.fragment_login_anon_btn_enter);
        mEnterAuthorizationButton = (Button) v.findViewById(R.id.fragment_login_anon_btn_enter_auth);

        mVuzSelectorEditText = (EditText) v.findViewById(R.id.fragment_login_anon_et_select_vuz);
        mSpecialitySelectorEditText = (EditText) v.findViewById(R.id.fragment_login_anon_et_select_speciality);

        mVuzSelectorEditTextTIL = (TextInputLayout) v.findViewById(R.id.fragment_login_anon_et_select_vuz_til);
        mSpecialitySelectorEditTextTIL = (TextInputLayout) v.findViewById(R.id.fragment_login_anon_et_select_speciality_til);


        LoginButtonOnClickListener buttonListener = new LoginButtonOnClickListener();

        mEnterButton.setOnClickListener(buttonListener);
        mEnterAuthorizationButton.setOnClickListener(buttonListener);

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(getActivity(), MainChooseActivity.class);
                startActivityForResult(intent, 1);
                return true;
            }
        };


        mVuzSelectorEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainChooseActivity.class);
                startActivityForResult(intent, KEY_REQUEST_UNIVERSITY);
            }
        });
        mSpecialitySelectorEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainChooseActivity.class);
                startActivityForResult(intent, KEY_REQUEST_SPECIALITY);
            }
        });



        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoginAuth = (ILoginAuth) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginAuth = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == KEY_REQUEST_UNIVERSITY) {
            University university = (University) data.getSerializableExtra(ChooseUniversityFragment.KEY_RETURN_UNIVERSITY);
            mVuzSelectorEditText.setText(university.getName());
        } else if (requestCode == KEY_REQUEST_SPECIALITY) {

        }
    }

    private class LoginButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_login_anon_btn_enter:
                    break;
                case R.id.fragment_login_anon_btn_enter_auth:
                    mLoginAuth.goToLoginAuth();
                    break;
                default:
                    mLoginAuth.goToLoginAuth();
            }
        }
    }

    public interface ILoginAuth {
        void goToLoginAuth();
    }

}
