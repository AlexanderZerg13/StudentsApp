package ru.infocom.university.modules.academicPlan;

import android.content.Context;
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

import java.util.Map;

import ru.infocom.university.R;
import ru.infocom.university.Utils;
import ru.infocom.university.data.LessonPlan;
import ru.infocom.university.data.Teacher;
import ru.infocom.university.interfaces.IToolbar;

public class AcademicPlanDescribeFragment extends Fragment {

    private static final String KEY_BUNDLE_LESSON_PLAN = "BUNDLE_LESSON_PLAN";

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

    public static AcademicPlanDescribeFragment newInstance(LessonPlan lessonPlan) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_BUNDLE_LESSON_PLAN, lessonPlan);

        AcademicPlanDescribeFragment fragment = new AcademicPlanDescribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLessonPlan = (LessonPlan) getArguments().getSerializable(KEY_BUNDLE_LESSON_PLAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academic_plan_describe, container, false);

        Toolbar toolbar = view.findViewById(R.id.fragment_academic_plan_describe_toolbar);
        mToolbarActivity.useToolbarWithBackStack(toolbar, R.string.discipline_describe);

        mAcademicPlanTextViewLessonName = view.findViewById(R.id.fragment_academic_plan_describe_tv_name);
        mAcademicPlanLayoutTeachersMain = view.findViewById(R.id.fragment_academic_plan_describe_ll_teachers_main);
        mAcademicPlanLayoutTeachers = view.findViewById(R.id.fragment_academic_plan_describe_ll_teachers);
        mAcademicPlanButtonMore = view.findViewById(R.id.fragment_academic_plan_describe_btn_more);
        mAcademicPlanLayoutEducationMain = view.findViewById(R.id.fragment_academic_plan_describe_ll_education_main);
        mAcademicPlanLayoutEducation = view.findViewById(R.id.fragment_academic_plan_describe_ll_education);
        mAcademicPlanLayoutCourseMain = view.findViewById(R.id.fragment_academic_plan_describe_ll_course_main);
        mAcademicPlanLayoutCourse = view.findViewById(R.id.fragment_academic_plan_describe_ll_course);
        mAcademicPlanTextViewCourseName = view.findViewById(R.id.fragment_academic_plan_describe_tv_course_name);
        mAcademicPlanTextViewCourseData = view.findViewById(R.id.fragment_academic_plan_describe_tv_course_data);
        mAcademicPlanLayoutTypeMain = view.findViewById(R.id.fragment_academic_plan_describe_ll_type_main);
        mAcademicPlanLayoutType = view.findViewById(R.id.fragment_academic_plan_describe_ll_type);
        mAcademicPlanTextViewTypeName = view.findViewById(R.id.fragment_academic_plan_describe_tv_type_name);
        mAcademicPlanTextViewTypeTime = view.findViewById(R.id.fragment_academic_plan_describe_tv_type_time);

        mAcademicPlanLayoutEducationMain.setVisibility(View.GONE);
        mAcademicPlanLayoutCourseMain.setVisibility(View.GONE);
        mAcademicPlanLayoutTypeMain.setVisibility(View.GONE);

        Log.i("AcademicPlan", "onCreateView: " + mLessonPlan);

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
            } else {
                mAcademicPlanTextViewTypeName.setText(Utils.coloredSomePartOfText(getString(R.string.set), ContextCompat.getColor(getContext(), R.color.colorPink), null));
            }
        }

        Map<String, Integer> loadMap = mLessonPlan.getLoadMap();
        if (!loadMap.isEmpty()) {
            mAcademicPlanLayoutEducationMain.setVisibility(View.VISIBLE);

            for (String key : loadMap.keySet()) {
                int color = R.color.colorBlack_87a;
                switch (key) {
                    case "лекции":
                        color = R.color.colorGreen2;
                        break;
                    case "практика":
                        color = R.color.colorBlue2;
                }

                String firstLetterUpperCase = key.substring(0, 1).toUpperCase() + key.substring(1) + ": ";
                View loadView = getEducationView(firstLetterUpperCase, color, loadMap.get(key));
                mAcademicPlanLayoutEducation.addView(loadView);
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

    private TextView getEducationView(String text, int resColor, int hours) {

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack_87a));
        textView.setText(Utils.coloredSomePartOfText(text, ContextCompat.getColor(getContext(), resColor), hours + " часов"));
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
