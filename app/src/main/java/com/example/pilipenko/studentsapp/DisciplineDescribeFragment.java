package com.example.pilipenko.studentsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Discipline;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;

public class DisciplineDescribeFragment extends Fragment {

    private static final String KEY_BUNDLE_SEMESTER = "BUNDLE_SEMESTER";
    private static final String KEY_BUNDLE_DISCIPLINE = "BUNDLE_DISCIPLINE";

    private Discipline mDiscipline;

    public static DisciplineDescribeFragment newInstance(int idSemester, int idDiscipline) {

        Bundle args = new Bundle();
        args.putInt(KEY_BUNDLE_SEMESTER, idSemester);
        args.putInt(KEY_BUNDLE_DISCIPLINE, idDiscipline);

        DisciplineDescribeFragment fragment = new DisciplineDescribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int idSemester = getArguments().getInt(KEY_BUNDLE_SEMESTER);
        int idDiscipline = getArguments().getInt(KEY_BUNDLE_DISCIPLINE);

        mDiscipline = StaticData.sSemesters.get(idSemester).getDisciplineList().get(idDiscipline);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discipline_describe, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_discipline_describe_toolbar);
        toolbar.setTitle(R.string.discipline_about);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        return view;
    }
}
