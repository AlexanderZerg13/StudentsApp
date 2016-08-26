package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Lesson;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LessonDescribeFragment extends Fragment {

    private static final String KEY_BUNDLE_LESSON_ID = "BUNDLE_LESSON_ID";

    private IToolbar mToolbarActivity;
    private ITransitionActions mITransitionActions;

    private Lesson mLesson;
    private List<Teacher> mTeacherList;

    private Button mMoreTeacherButton;
    private Button mAboutDisciplineButton;
    private TextView mLessonNameTextView;
    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;
    private TextView mAudienceTextView;
    private TextView mTypeTextView;
    private LinearLayout mTeachersLinearLayout;

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
        View view = inflater.inflate(R.layout.fragment_lesson_describe, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_lesson_describe_toolbar);
        mToolbarActivity.useToolbarWithBackStack(toolbar, R.string.lesson_describe);

        mMoreTeacherButton = (Button) view.findViewById(R.id.fragment_lesson_describe_btn_more);
        mAboutDisciplineButton = (Button) view.findViewById(R.id.fragment_lesson_describe_btn_about);
        mLessonNameTextView = (TextView) view.findViewById(R.id.fragment_lesson_describe_tv_name);
        mStartTimeTextView = (TextView) view.findViewById(R.id.fragment_lesson_describe_tv_start_time);
        mEndTimeTextView = (TextView) view.findViewById(R.id.fragment_lesson_describe_tv_end_time);
        mAudienceTextView = (TextView) view.findViewById(R.id.fragment_lesson_describe_audience);
        mTypeTextView = (TextView) view.findViewById(R.id.fragment_lesson_describe_tv_type);
        mTeachersLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_lesson_describe_ll_teachers);

        setupTeachers();
        mAudienceTextView.setText(mLesson.getAudience());
        mLessonNameTextView.setText(mLesson.getName());
        switch (mLesson.getType()) {
            case "ЛАБ":
                mTypeTextView.setText(Utils.coloredSomePartOfText("Лабораторная работа",
                        ContextCompat.getColor(getActivity(), R.color.colorLessonCardLab),
                        null));
                break;
            case "ЛЕК":
                mTypeTextView.setText(Utils.coloredSomePartOfText("Лекция",
                        ContextCompat.getColor(getActivity(), R.color.colorLessonCardLecture),
                        null));
                break;
        }
        mAboutDisciplineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mITransitionActions.goToDescribeDiscipline(0, 0);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (IToolbar) context;
        mITransitionActions = (ITransitionActions) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
        mITransitionActions = null;
    }

    private void setupTeachers() {
        Teacher teacher = mTeacherList.get(0);
        mTeachersLinearLayout.addView(getTeacherView(teacher, null));

        if (mTeacherList.size() == 1) {
            mMoreTeacherButton.setVisibility(View.INVISIBLE);
        } else {
            mMoreTeacherButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMoreTeacherButton.setVisibility(View.INVISIBLE);
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    Resources r = getResources();
                    for (int i = 1; i < mTeacherList.size(); i++) {
                        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, r.getDisplayMetrics());
                        margin.setMargins(0, topMargin, 0, 0);
                        Log.i("TAG", "onClick: " + topMargin);
                        mTeachersLinearLayout.addView(getTeacherView(mTeacherList.get(i), layoutInflater), margin);
                    }
                }
            });
        }
    }

    private View getTeacherView(Teacher teacher, LayoutInflater inflater) {
        if (inflater == null) {
            inflater = LayoutInflater.from(getActivity());
        }
        View teacherView = inflater.inflate(R.layout.item_teacher, mTeachersLinearLayout, false);
        TextView teacherName = (TextView) teacherView.findViewById(R.id.item_teacher_tv_teacher_name);
        TextView teacherDescribe = (TextView) teacherView.findViewById(R.id.item_teacher_tv_teacher_describe);
        teacherName.setText(teacher.getName());
        teacherDescribe.setText(teacher.getPost());

        return teacherView;
    }
}