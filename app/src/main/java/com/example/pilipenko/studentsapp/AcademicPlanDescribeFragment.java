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

import com.example.pilipenko.studentsapp.data.Discipline;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.data.Teacher;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AcademicPlanDescribeFragment extends Fragment {

    private static final String KEY_BUNDLE_SEMESTER = "BUNDLE_SEMESTER";
    private static final String KEY_BUNDLE_DISCIPLINE = "BUNDLE_DISCIPLINE";

    private IToolbar mToolbarActivity;

    private Discipline mDiscipline;
    private List<Teacher> mTeacherList;

    private Button mMoreButton;
    private TextView mDisciplineNameTextView;
    private TextView mLectureTextView;
    private TextView mPracticeTextView;
    private LinearLayout mTeachersLinearLayout;

    public static AcademicPlanDescribeFragment newInstance(int idSemester, int idDiscipline) {

        Bundle args = new Bundle();
        args.putInt(KEY_BUNDLE_SEMESTER, idSemester);
        args.putInt(KEY_BUNDLE_DISCIPLINE, idDiscipline);

        AcademicPlanDescribeFragment fragment = new AcademicPlanDescribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int idSemester = getArguments().getInt(KEY_BUNDLE_SEMESTER);
        int idDiscipline = getArguments().getInt(KEY_BUNDLE_DISCIPLINE);

        mDiscipline = StaticData.sSemesters.get(idSemester).getDisciplineList().get(idDiscipline);
        Random r = new Random();
        if (r.nextBoolean()) {
            mTeacherList = StaticData.sTeachers;
            mTeacherList.get(0).setName(mDiscipline.getTeacherName());
        } else {
            mTeacherList = new ArrayList<>();
            mTeacherList.add(new Teacher(mDiscipline.getTeacherName(), StaticData.sTeachers.get(0).getPost()));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discipline_describe, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_discipline_describe_toolbar);
        mToolbarActivity.useToolbarWithBackStack(toolbar, R.string.discipline_describe);

        mMoreButton = (Button) view.findViewById(R.id.fragment_lesson_describe_btn_more);
        mDisciplineNameTextView = (TextView) view.findViewById(R.id.fragment_lesson_describe_tv_name);
        mLectureTextView = (TextView) view.findViewById(R.id.fragment_lesson_describe_tv_start_time);
        mPracticeTextView = (TextView) view.findViewById(R.id.fragment_lesson_describe_tv_end_time);
        mTeachersLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_lesson_describe_ll_teachers);

        mDisciplineNameTextView.setText(mDiscipline.getName());

        mLectureTextView.setText(
                Utils.coloredSomePartOfText(getString(R.string.lecture),
                        ContextCompat.getColor(getActivity(), R.color.colorGreen2),
                        "70 часов"));

        mPracticeTextView.setText(
                Utils.coloredSomePartOfText(getString(R.string.practice),
                        ContextCompat.getColor(getActivity(), R.color.colorBlue2),
                        "50 часов"));

        setupTeachers();

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

    private void setupTeachers() {
        Teacher teacher = mTeacherList.get(0);
        mTeachersLinearLayout.addView(getTeacherView(teacher, null));

        if (mTeacherList.size() == 1) {
            mMoreButton.setVisibility(View.INVISIBLE);
        } else {
            mMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMoreButton.setVisibility(View.INVISIBLE);
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
