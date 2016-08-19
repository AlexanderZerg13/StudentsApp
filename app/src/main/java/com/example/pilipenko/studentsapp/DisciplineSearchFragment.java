package com.example.pilipenko.studentsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class DisciplineSearchFragment extends Fragment {

    private EditText mInputEditText;

    public static DisciplineSearchFragment newInstance() {

        Bundle args = new Bundle();

        DisciplineSearchFragment fragment = new DisciplineSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_education, container, false);

        mInputEditText = (EditText) view.findViewById(R.id.fragment_choose_education_et_input);

        return view;
    }
}
