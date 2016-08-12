package com.example.pilipenko.studentsapp;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragmentAnon extends Fragment {

    private Button mEnterButton;
    private Button mEnterAuthorizationButton;
    private TextInputLayout mVuzSelectorEditTextTIL;
    private TextInputLayout mSpecialitySelectorEditTextTIL;
    private EditText mVuzSelectorEditText;
    private EditText mSpecialitySelectorEditText;

    private ILoginAuth mLoginAuth;

    public static LoginFragmentAnon newInstance() {
        
        Bundle args = new Bundle();
        
        LoginFragmentAnon fragment = new LoginFragmentAnon();
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
                mLoginAuth.goToLoginAuth();
                return false;
            }
        };

        mVuzSelectorEditText.setOnTouchListener(touchListener);
        mSpecialitySelectorEditText.setOnTouchListener(touchListener);



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
