package com.example.pilipenko.studentsapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pilipenko.studentsapp.R;

public class BasicFragment extends Fragment {

    public static BasicFragment newInstance() {

        Bundle args = new Bundle();

        BasicFragment fragment = new BasicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_basic_toolbar);
        toolbar.setTitle("Test Fragment");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return view;
    }
}
