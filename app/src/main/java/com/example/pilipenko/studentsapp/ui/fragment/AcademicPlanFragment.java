package com.example.pilipenko.studentsapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.utils.Utils;
import com.example.pilipenko.studentsapp.data.LessonPlan;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AcademicPlanFragment extends Fragment implements AcademicPlanViewPagerFragment.Filter {

    private static final String TAG = "AcademicPlanFragment";
    private static final String KEY_EXTRA_LIST = "EXTRA_LIST";

    private RecyclerView mRecyclerViewGrades;
    private String mFilter;

    private ITransitionActions mITransitionActions;

    public static AcademicPlanFragment newInstance(List<LessonPlan> list) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_EXTRA_LIST, (Serializable) list);
        AcademicPlanFragment fragment = new AcademicPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);

        mRecyclerViewGrades = (RecyclerView) view.findViewById(R.id.layout_recycler_view_recycler_view);

        mRecyclerViewGrades.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewGrades.addItemDecoration(new Utils.SimpleDividerItemDecoration(getActivity()));

        updateUI((List<LessonPlan>) getArguments().getSerializable(KEY_EXTRA_LIST));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mITransitionActions = (ITransitionActions) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mITransitionActions = null;
    }

    @Override
    public void doFilter(String filter) {
        List<LessonPlan> plans = (List<LessonPlan>) getArguments().getSerializable(KEY_EXTRA_LIST);

        if (TextUtils.isEmpty(filter)) {
            mFilter = null;
            updateUI(plans);
        } else {
            mFilter = filter;
            List<LessonPlan> filteredPlans = new ArrayList<>();
            for (LessonPlan plan : plans) {
                if (Utils.checkContains(plan.getName(), mFilter)) {
                    filteredPlans.add(plan);
                }
            }
            updateUI(filteredPlans);
        }
    }

    private void updateUI(List<LessonPlan> list) {

        Collections.sort(list, new Comparator<LessonPlan>() {
            @Override
            public int compare(LessonPlan lessonPlan1, LessonPlan lessonPlan2) {
                int lessonPlan1Mask = (lessonPlan1.isExam() ? 4 : 0) + (lessonPlan1.isSet() && !lessonPlan1.isExam() ? 2 : 0) + (lessonPlan1.isCourse() ? 1 : 0);
                int lessonPlan2Mask = (lessonPlan2.isExam() ? 4 : 0) + (lessonPlan2.isSet() && !lessonPlan2.isExam() ? 2 : 0) + (lessonPlan2.isCourse() ? 1 : 0);
                return lessonPlan2Mask - lessonPlan1Mask;
            }
        });

        for (LessonPlan lessonPlan: list) {
            Log.i(TAG, "updateUI: " + lessonPlan);
        }

        AcademicPlanAdapter adapter = new AcademicPlanAdapter(list);
        mRecyclerViewGrades.setAdapter(adapter);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) AcademicPlanFragment.this
                .getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        IBinder iBinder = getActivity().getCurrentFocus().getWindowToken();
        if (iBinder != null) {
            imm.hideSoftInputFromWindow(iBinder, 0);
        }
    }

    private class AcademicPlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LessonPlan mLessonPlan;

        private TextView mAcademicPlanLessonNameTextView;
        private TextView mAcademicPlanLessonTeacherTextView;
        private TextView mAcademicPlanLessonTypeTextView;
        private TextView mAcademicPlanCourseWorkTextView;

        public AcademicPlanViewHolder(View itemView) {
            super(itemView);

            mAcademicPlanLessonNameTextView = (TextView) itemView.findViewById(R.id.item_academic_plan_tv_lesson_name);
            mAcademicPlanLessonTeacherTextView = (TextView) itemView.findViewById(R.id.item_academic_plan_tv_teacher_name);
            mAcademicPlanLessonTypeTextView = (TextView) itemView.findViewById(R.id.item_academic_plan_tv_type);
            mAcademicPlanCourseWorkTextView = (TextView) itemView.findViewById(R.id.item_academic_plan_tv_lesson_hours);

            itemView.setOnClickListener(this);
        }

        public void bindGradeViewHolder(LessonPlan lessonPlan) {
            mLessonPlan = lessonPlan;
            if (TextUtils.isEmpty(mFilter)) {
                mAcademicPlanLessonNameTextView.setText(lessonPlan.getName());
            } else {
                mAcademicPlanLessonNameTextView.setText(Utils.getSpannableStringMatches(lessonPlan.getName(), mFilter));
            }
//            Teacher
//            mAcademicPlanLessonTeacherTextView.setText();
            if (lessonPlan.isExam()) {
                mAcademicPlanLessonTypeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDeepOrange));
                mAcademicPlanLessonTypeTextView.setText(getString(R.string.exam).toUpperCase());
            } else if (lessonPlan.isSet()) {
                mAcademicPlanLessonTypeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPink));
                mAcademicPlanLessonTypeTextView.setText(getString(R.string.set).toUpperCase());
            } else {
                mAcademicPlanLessonTypeTextView.setText("");
            }
//            int hours = lessonPlan.getLaboratoryHours() + lessonPlan.getPracticeHours() + lessonPlan.getLectureHours();
//            if (hours > 0) {
//                mAcademicPlanCourseWorkTextView.setText(hours + " ч");
//            }
            if (lessonPlan.isCourse()) {
                mAcademicPlanCourseWorkTextView.setText("К/Р");
            } else {
                mAcademicPlanCourseWorkTextView.setText("");
            }
        }

        @Override
        public void onClick(View view) {
            mFilter = null;
            hideSoftKeyboard();
            mITransitionActions.goToDescribeAcademicPlan(mLessonPlan.getSemester(), mLessonPlan.getId());
        }
    }

    private class AcademicPlanAdapter extends RecyclerView.Adapter<AcademicPlanViewHolder> {

        private List<LessonPlan> mAcademicPlansList;

        public AcademicPlanAdapter(List<LessonPlan> list) {
            mAcademicPlansList = list;
        }

        @Override
        public AcademicPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_academic_plan, parent, false);
            return new AcademicPlanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AcademicPlanViewHolder holder, int position) {
            holder.bindGradeViewHolder(mAcademicPlansList.get(position));
        }

        @Override
        public int getItemCount() {
            return mAcademicPlansList.size();
        }
    }
}
