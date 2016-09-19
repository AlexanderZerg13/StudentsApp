package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.data.Discipline;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;

import java.util.List;

public class GradesFragment extends Fragment {

    private ITransitionActions mITransitionActions;

    private static final String TAG = "GradesFragment";

    private RecyclerView mRecyclerViewGrades;

    public static GradesFragment newInstance() {

        Bundle args = new Bundle();

        GradesFragment fragment = new GradesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grades, container, false);

        mRecyclerViewGrades = (RecyclerView) view.findViewById(R.id.fragment_grades_recycler_view);

        mRecyclerViewGrades.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewGrades.addItemDecoration(new Utils.SimpleDividerItemDecoration(getActivity()));

        updateUI();

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

    private void updateUI() {
//        mNavigatorSubTitle.setText(StaticData.sSemesters.get(mCurrentSemester).getSemesterName());
        GradesAdapter adapter = new GradesAdapter(StaticData.sSemesters.get(0).getDisciplineList());
        mRecyclerViewGrades.setAdapter(adapter);
    }

    private class GradeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mDisciplineNameTextView;
        TextView mDateWithTeacherTextView;
        TextView mMarkTextView;

        private int mDisciplinePosition;

        public GradeViewHolder(View itemView) {
            super(itemView);

            mDisciplineNameTextView = (TextView) itemView.findViewById(R.id.item_grade_tv_discipline);
            mDateWithTeacherTextView = (TextView) itemView.findViewById(R.id.item_grade_tv_data_and_teacher);
            mMarkTextView = (TextView) itemView.findViewById(R.id.item_grade_tv_mark);

            itemView.setOnClickListener(this);
        }

        public void bindGradeViewHolder(Discipline discipline, int position) {
            mDisciplinePosition = position;
            mDisciplineNameTextView.setText(discipline.getName());
            mDateWithTeacherTextView.setText("12.05.16 " + discipline.getTeacherName());

            Discipline.Mark mark = discipline.getMark();
            int width;
            if (mark == Discipline.Mark.SET || mark == Discipline.Mark.SET_OOF) {
                width = (int) getResources().getDimension(R.dimen.grand_mark_zach);
                mMarkTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            } else {
                width = (int) getResources().getDimension(R.dimen.grand_mark_normal);
                mMarkTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }

            ViewGroup.LayoutParams param = mMarkTextView.getLayoutParams();
            param.width = width;
            mMarkTextView.setLayoutParams(param);

            switch (mark) {
                case SET:
                case FIVE:
                    itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen1_07a));
                    mMarkTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen1));
                    break;
                case FOUR:
                case THREE:
                    itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorYellowGreen_07a));
                    mMarkTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorYellowGreen));
                    break;
                case SET_OOF:
                case TWO:
                    itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorRed2_07a));
                    mMarkTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorRed2));
                    break;
            }

            mMarkTextView.setText(mark.toString());
        }

        @Override
        public void onClick(View view) {
//            mITransitionActionsActivity.goToDescribeDiscipline(mCurrentSemester, mDisciplinePosition);
        }
    }

    private class GradesAdapter extends RecyclerView.Adapter<GradeViewHolder> {

        private List<Discipline> mDisciplineList;

        public GradesAdapter(List<Discipline> list) {
            mDisciplineList = list;
        }

        @Override
        public GradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_grade, parent, false);
            return new GradeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GradeViewHolder holder, int position) {
            holder.bindGradeViewHolder(mDisciplineList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mDisciplineList.size();
        }
    }

}
