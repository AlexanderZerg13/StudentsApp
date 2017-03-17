package com.example.pilipenko.studentsapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.utils.Utils;
import com.example.pilipenko.studentsapp.data.LessonPlan;
import com.example.pilipenko.studentsapp.data.LessonPlanLab;
import com.example.pilipenko.studentsapp.data.Teacher;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;

public class AcademicPlanDescribeFragment extends Fragment {

    private static final String KEY_BUNDLE_SEMESTER = "BUNDLE_SEMESTER";
    private static final String KEY_BUNDLE_LESSON_PLAN_ID = "BUNDLE_LESSON_PLAN_ID";

    private IToolbar mToolbarActivity;

    private LessonPlan mLessonPlan;

    private TextView mAcademicPlanTextViewLessonName;
    private LinearLayout mAcademicPlanLayoutTeachersMain;
    private LinearLayout mAcademicPlanLayoutTeachers;
    private Button mAcademicPlanButtonMore;
    private LinearLayout mAcademicPlanLayoutEducationMain;
    private LinearLayout mAcademicPlanLayoutEducation;
    private LinearLayout mAcademicPlanLayoutCourseMain;
    private LinearLayout mAcademicPlanLayoutCourse;
    private TextView mAcademicPlanTextViewCourseName;
    private TextView mAcademicPlanTextViewCourseData;
    private LinearLayout mAcademicPlanLayoutTypeMain;
    private LinearLayout mAcademicPlanLayoutType;
    private TextView mAcademicPlanTextViewTypeName;
    private TextView mAcademicPlanTextViewTypeTime;

    public static AcademicPlanDescribeFragment newInstance(int idSemester, int idDiscipline) {

        Bundle args = new Bundle();
        args.putInt(KEY_BUNDLE_SEMESTER, idSemester);
        args.putInt(KEY_BUNDLE_LESSON_PLAN_ID, idDiscipline);

        AcademicPlanDescribeFragment fragment = new AcademicPlanDescribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int semesterNumber = getArguments().getInt(KEY_BUNDLE_SEMESTER);
        int planId = getArguments().getInt(KEY_BUNDLE_LESSON_PLAN_ID);

        mLessonPlan = LessonPlanLab.get(getContext()).getLesson(planId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academic_plan_describe, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_academic_plan_describe_toolbar);
        mToolbarActivity.useToolbarWithBackStack(toolbar, R.string.discipline_describe);

        mAcademicPlanTextViewLessonName = (TextView) view.findViewById(R.id.fragment_academic_plan_describe_tv_name);
        mAcademicPlanLayoutTeachersMain = (LinearLayout) view.findViewById(R.id.fragment_academic_plan_describe_ll_teachers_main);
        mAcademicPlanLayoutTeachers = (LinearLayout) view.findViewById(R.id.fragment_academic_plan_describe_ll_teachers);
        mAcademicPlanButtonMore = (Button) view.findViewById(R.id.fragment_academic_plan_describe_btn_more);
        mAcademicPlanLayoutEducationMain = (LinearLayout) view.findViewById(R.id.fragment_academic_plan_describe_ll_education_main);
        mAcademicPlanLayoutEducation = (LinearLayout) view.findViewById(R.id.fragment_academic_plan_describe_ll_education);
        mAcademicPlanLayoutCourseMain = (LinearLayout) view.findViewById(R.id.fragment_academic_plan_describe_ll_course_main);
        mAcademicPlanLayoutCourse = (LinearLayout) view.findViewById(R.id.fragment_academic_plan_describe_ll_course);
        mAcademicPlanTextViewCourseName = (TextView) view.findViewById(R.id.fragment_academic_plan_describe_tv_course_name);
        mAcademicPlanTextViewCourseData = (TextView) view.findViewById(R.id.fragment_academic_plan_describe_tv_course_data);
        mAcademicPlanLayoutTypeMain = (LinearLayout) view.findViewById(R.id.fragment_academic_plan_describe_ll_type_main);
        mAcademicPlanLayoutType = (LinearLayout) view.findViewById(R.id.fragment_academic_plan_describe_ll_type);
        mAcademicPlanTextViewTypeName = (TextView) view.findViewById(R.id.fragment_academic_plan_describe_tv_type_name);
        mAcademicPlanTextViewTypeTime = (TextView) view.findViewById(R.id.fragment_academic_plan_describe_tv_type_time);

        mAcademicPlanLayoutEducationMain.setVisibility(View.GONE);
        mAcademicPlanLayoutCourseMain.setVisibility(View.GONE);
        mAcademicPlanLayoutTypeMain.setVisibility(View.GONE);

        mAcademicPlanTextViewLessonName.setText(mLessonPlan.getName());
        if (mLessonPlan.isCourse()) {
            mAcademicPlanLayoutCourseMain.setVisibility(View.VISIBLE);
            mAcademicPlanTextViewCourseData.setVisibility(View.GONE);
            mAcademicPlanTextViewCourseName.setVisibility(View.GONE);
        }

        if (mLessonPlan.isExam() || mLessonPlan.isSet()) {
            mAcademicPlanLayoutTypeMain.setVisibility(View.VISIBLE);
            mAcademicPlanTextViewTypeTime.setVisibility(View.GONE);

            if (mLessonPlan.isExam()) {
                mAcademicPlanTextViewTypeName.setText(Utils.coloredSomePartOfText(getString(R.string.exam), ContextCompat.getColor(getContext(), R.color.colorDeepOrange), null));
            }   else {
                mAcademicPlanTextViewTypeName.setText(Utils.coloredSomePartOfText(getString(R.string.set), ContextCompat.getColor(getContext(), R.color.colorPink), null));
            }
        }

        if (mLessonPlan.getLectureHours() != 0
                || mLessonPlan.getPracticeHours() != 0
                || mLessonPlan.getLaboratoryHours() != 0
                || mLessonPlan.getSelfWorkHours() != 0) {
            mAcademicPlanLayoutEducationMain.setVisibility(View.VISIBLE);

            if (mLessonPlan.getLectureHours() != 0) {
                mAcademicPlanLayoutEducation.addView(
                        getEducationView(R.string.lecture, R.color.colorGreen2,
                                mLessonPlan.getLectureHours()));
            }

            if (mLessonPlan.getPracticeHours() != 0) {
                mAcademicPlanLayoutEducation.addView(
                        getEducationView(R.string.practice, R.color.colorBlue2,
                                mLessonPlan.getPracticeHours()));
            }

            if (mLessonPlan.getLectureHours() != 0) {
                mAcademicPlanLayoutEducation.addView(
                        getEducationView(R.string.lab, R.color.colorBlack_87a,
                                mLessonPlan.getLectureHours()));
            }

            if (mLessonPlan.getSelfWorkHours() != 0) {
                mAcademicPlanLayoutEducation.addView(
                        getEducationView(R.string.self, R.color.colorBlack_87a,
                                mLessonPlan.getSelfWorkHours()));
            }

        }

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

    private TextView getEducationView(int resText, int resColor, int hours) {

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack_87a));
        textView.setText(Utils.coloredSomePartOfText(getString(resText), ContextCompat.getColor(getContext(), resColor), hours + " часов"));
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return textView;
    }

    private View getTeacherView(Teacher teacher, LayoutInflater inflater) {
        if (inflater == null) {
            inflater = LayoutInflater.from(getActivity());
        }
        View teacherView = inflater.inflate(R.layout.item_teacher, mAcademicPlanLayoutTeachers, false);
        TextView teacherName = (TextView) teacherView.findViewById(R.id.item_teacher_tv_teacher_name);
        TextView teacherDescribe = (TextView) teacherView.findViewById(R.id.item_teacher_tv_teacher_describe);
        teacherName.setText(teacher.getName());
        teacherDescribe.setText(teacher.getPost());

        return teacherView;
    }
}
