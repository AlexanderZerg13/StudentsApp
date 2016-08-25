package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Lesson;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LessonDescribeFragment extends Fragment {

    private static final String KEY_BUNDLE_LESSON_ID = "BUNDLE_LESSON_ID";

    private IToolbar mToolbarActivity;

    private Lesson mLesson;
    private List<Teacher> mTeacherList;

    public static LessonDescribeFragment newInstance(int idLesson) {

        Bundle args = new Bundle();
        args.putInt(KEY_BUNDLE_LESSON_ID, idLesson);

        LessonDescribeFragment fragment = new LessonDescribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int idLesson = getArguments().getInt(KEY_BUNDLE_LESSON_ID);

        mLesson = StaticData.sLessons.get(idLesson);
        Random r = new Random();
        if (r.nextBoolean()) {
            mTeacherList = StaticData.sTeachers;
            mTeacherList.get(0).setName(mLesson.getTeacherName());
        } else {
            mTeacherList = new ArrayList<>();
            mTeacherList.add(new Teacher(mLesson.getTeacherName(), StaticData.sTeachers.get(0).getPost()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_lesson_describe, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (IToolbar) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
    }
}
