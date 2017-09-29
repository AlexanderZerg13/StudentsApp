package ru.infocom.university;

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
import android.widget.TextView;

import ru.infocom.university.data.LessonProgress;
import ru.infocom.university.interfaces.ITransitionActions;

import java.io.Serializable;
import java.util.List;

public class GradesFragment extends Fragment {

    private static final String TAG = "GradesFragment";

    private static final String KEY_EXTRA_LIST = "EXTRA_LIST";

    private RecyclerView mRecyclerViewGrades;

    public static GradesFragment newInstance(List<LessonProgress> list) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_EXTRA_LIST, (Serializable) list);
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
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);

        mRecyclerViewGrades = (RecyclerView) view.findViewById(R.id.layout_recycler_view_recycler_view);

        mRecyclerViewGrades.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewGrades.addItemDecoration(new Utils.SimpleDividerItemDecoration(getActivity()));

        updateUI((List<LessonProgress>) getArguments().getSerializable(KEY_EXTRA_LIST));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateUI(List<LessonProgress> list) {
        GradesAdapter adapter = new GradesAdapter(list);
        mRecyclerViewGrades.setAdapter(adapter);
    }

    private class GradeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mDisciplineNameTextView;
        TextView mDateWithTeacherTextView;
        TextView mMarkTextView;

        public GradeViewHolder(View itemView) {
            super(itemView);

            mDisciplineNameTextView = (TextView) itemView.findViewById(R.id.item_grade_tv_discipline);
            mDateWithTeacherTextView = (TextView) itemView.findViewById(R.id.item_grade_tv_data_and_teacher);
            mMarkTextView = (TextView) itemView.findViewById(R.id.item_grade_tv_mark);

            itemView.setOnClickListener(this);
        }

        public void bindGradeViewHolder(LessonProgress lessonProgress) {
            mDisciplineNameTextView.setText(lessonProgress.getLessonName());

            LessonProgress.Mark mark = lessonProgress.getMark();
            int width;
            if (mark == LessonProgress.Mark.SET || mark == LessonProgress.Mark.SET_OOF) {
                width = (int) getResources().getDimension(R.dimen.grand_mark_zach);
                mMarkTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            } else {
                width = (int) getResources().getDimension(R.dimen.grand_mark_normal);
                mMarkTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }

            ViewGroup.LayoutParams param = mMarkTextView.getLayoutParams();
            param.width = width;
            mMarkTextView.setLayoutParams(param);
            mMarkTextView.setVisibility(mark != null ? View.VISIBLE : View.INVISIBLE);

            if (mark != null) {
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
                mDateWithTeacherTextView.setText(lessonProgress.getDate());
                mMarkTextView.setText(mark.toString());
            }
        }

        @Override
        public void onClick(View view) {
//            mITransitionActionsActivity.goToDescribeAcademicPlan(mCurrentSemester, mDisciplinePosition);
        }
    }

    private class GradesAdapter extends RecyclerView.Adapter<GradeViewHolder> {

        private List<LessonProgress> mDisciplineList;

        public GradesAdapter(List<LessonProgress> list) {
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
            holder.bindGradeViewHolder(mDisciplineList.get(position));
        }

        @Override
        public int getItemCount() {
            return mDisciplineList.size();
        }
    }

}
