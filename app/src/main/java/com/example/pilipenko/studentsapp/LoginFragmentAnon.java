package com.example.pilipenko.studentsapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginFragmentAnon extends Fragment {

    private Button mEnterButton;
    private Button mEnterAuthorizationButton;

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



        return v;
    }
}
