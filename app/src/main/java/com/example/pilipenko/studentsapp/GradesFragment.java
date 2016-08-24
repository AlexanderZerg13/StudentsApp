package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Discipline;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;

import java.util.List;

public class GradesFragment extends Fragment {

    private IToolbar mToolbarActivity;
    private IDisciplineActions mIDisciplineActionsActivity;

    private TextView mNavigatorTitle;
    private TextView mNavigatorSubTitle;
    private RecyclerView mRecyclerViewDiscipline;
    private ImageButton mNavigatorPriorImageButton;
    private ImageButton mNavigatorNextImageButton;

    private int mCurrentSemester = 2;

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
        View view = inflater.inflate(R.layout.fragment_discipline, container, false);

        mNavigatorTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_title);
        mNavigatorSubTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_sub_title);
        mNavigatorPriorImageButton = (ImageButton) view.findViewById(R.id.toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageButton) view.findViewById(R.id.toolbar_navigator_btn_next);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_discipline_toolbar);
        mToolbarActivity.useToolbar(toolbar, 0);
        mRecyclerViewDiscipline = (RecyclerView) view.findViewById(R.id.fragment_discipline_rv_disciplines);
        mRecyclerViewDiscipline.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewDiscipline.addItemDecoration(new Utils.SimpleDividerItemDecoration(getActivity()));

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorTitle.setText(R.string.grades_title);

        updateUI();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (IToolbar) context;
        mIDisciplineActionsActivity = (IDisciplineActions) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
        mIDisciplineActionsActivity = null;
    }

    private void updateUI() {
        mNavigatorSubTitle.setText(StaticData.sSemesters.get(mCurrentSemester).getSemesterName());
        GradesAdapter adapter = new GradesAdapter(StaticData.sSemesters.get(mCurrentSemester).getDisciplineList());
        mRecyclerViewDiscipline.setAdapter(adapter);

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
                    itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGoodBackground));
                    mMarkTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGoodText));
                    break;
                case FOUR:
                case THREE:
                    itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorNormalBackground));
                    mMarkTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorNormalText));
                    break;
                case SET_OOF:
                case TWO:
                    itemView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBadBackground));
                    mMarkTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBadText));
                    break;
            }

            mMarkTextView.setText(mark.toString());
        }

        @Override
        public void onClick(View view) {
            mIDisciplineActionsActivity.goToDescribeDiscipline(mCurrentSemester, mDisciplinePosition);
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

    private class NavigatorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.toolbar_navigator_btn_prior:
                    if (mCurrentSemester != 0) {
                        mCurrentSemester--;
                        updateUI();
                    }
                    break;
                case R.id.toolbar_navigator_btn_next:
                    if (StaticData.sSemesters.size() - 1 != mCurrentSemester) {
                        mCurrentSemester++;
                        updateUI();
                    }
                    break;
            }
        }
    }
}
