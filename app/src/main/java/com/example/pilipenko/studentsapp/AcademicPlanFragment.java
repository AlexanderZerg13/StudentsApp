package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.data.LessonPlan;
import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AcademicPlanFragment extends Fragment implements AcademicPlanViewPagerFragment.Filter {

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
        AcademicPlanAdapter adapter = new AcademicPlanAdapter(list);
        mRecyclerViewGrades.setAdapter(adapter);
    }

    private class AcademicPlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mAcademicPlanLessonNameTextView;
        TextView mAcademicPlanLessonTeacherTextView;
        TextView mAcademicPlanLessonTypeTextView;
        TextView mAcademicPlanLessonHoursTextView;

        public AcademicPlanViewHolder(View itemView) {
            super(itemView);

            mAcademicPlanLessonNameTextView = (TextView) itemView.findViewById(R.id.item_academic_plan_tv_lesson_name);
            mAcademicPlanLessonTeacherTextView = (TextView) itemView.findViewById(R.id.item_academic_plan_tv_teacher_name);
            mAcademicPlanLessonTypeTextView = (TextView) itemView.findViewById(R.id.item_academic_plan_tv_type);
            mAcademicPlanLessonHoursTextView = (TextView) itemView.findViewById(R.id.item_academic_plan_tv_lesson_hours);

            itemView.setOnClickListener(this);
        }

        public void bindGradeViewHolder(LessonPlan lessonPlan) {
            if (TextUtils.isEmpty(mFilter)) {
                mAcademicPlanLessonNameTextView.setText(lessonPlan.getName());
            } else {
                mAcademicPlanLessonNameTextView.setText(Utils.getSpannableStringMatches(lessonPlan.getName(), mFilter));
            }
//            Teacher
//            mAcademicPlanLessonTeacherTextView.setText();
            if (lessonPlan.isExam()) {
                mAcademicPlanLessonTypeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDeepOrange));
                mAcademicPlanLessonTypeTextView.setText(R.string.exam);
            } else if (lessonPlan.isSet()) {
                mAcademicPlanLessonTypeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPink));
                mAcademicPlanLessonTypeTextView.setText(R.string.set);
            }
            int hours = lessonPlan.getLaboratoryHours() + lessonPlan.getPracticeHours() + lessonPlan.getLectureHours();
            mAcademicPlanLessonHoursTextView.setText(hours + " ч");
        }

        @Override
        public void onClick(View view) {
//            mITransitionActionsActivity.goToDescribeDiscipline(mCurrentSemester, mDisciplinePosition);
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
