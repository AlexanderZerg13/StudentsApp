package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.Discipline;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.StaticData;

import java.util.List;

public class DisciplineFragment extends Fragment {

    private ToolbarI mToolbarActivity;

    private RecyclerView mRecyclerViewDiscipline;
    private ImageButton mNavigatorPriorImageButton;
    private ImageButton mNavigatorNextImageButton;
    private TextView mNavigatorSubTitle;

    public static DisciplineFragment newInstance() {

        Bundle args = new Bundle();

        DisciplineFragment fragment = new DisciplineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discipline, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_discipline_toolbar);
        mToolbarActivity.useToolbar(toolbar);

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton = (ImageButton) view.findViewById(R.id.toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageButton) view.findViewById(R.id.toolbar_navigator_btn_next);
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorSubTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_sub_title);

        mRecyclerViewDiscipline = (RecyclerView) view.findViewById(R.id.fragment_discipline_rv_disciplines);
        mRecyclerViewDiscipline.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewDiscipline.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (ToolbarI) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.discipline, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.discipline_menu_item_search:
                Toast.makeText(getActivity(), "Поиск!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        DisciplineItemsAdapter adapter = new DisciplineItemsAdapter(StaticData.sSemesters.get(0).getDisciplineList());
        mRecyclerViewDiscipline.setAdapter(adapter);
    }

    private class DisciplineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNameTextView;
        private TextView mTeacherTextView;
        private TextView mTypeTextView;
        private TextView mHoursTextView;

        public DisciplineViewHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.item_discipline_tv_name);
            mTeacherTextView = (TextView) itemView.findViewById(R.id.item_discipline_tv_teacher_name);
            mTypeTextView = (TextView) itemView.findViewById(R.id.item_discipline_tv_type);
            mHoursTextView = (TextView) itemView.findViewById(R.id.item_discipline_tv_lesson_hours);

            itemView.setOnClickListener(this);
        }

        public void bindDisciplineItem(Discipline discipline) {
            mNameTextView.setText(discipline.getName());
            mTeacherTextView.setText(discipline.getTeacherName());
            mHoursTextView.setText(discipline.getHours() + " ч");
            String type = discipline.getType();
            mTypeTextView.setText(type);
            if (type.equalsIgnoreCase("Экзамен") ){
                mTypeTextView.setTextColor(getResources().getColor(R.color.colorLogo));
            } else {
                mTypeTextView.setTextColor(getResources().getColor(R.color.colorPink));
            }
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();
        }
    }

    private class DisciplineItemsAdapter extends RecyclerView.Adapter<DisciplineViewHolder> {

        private List<Discipline> mDisciplineList;

        public DisciplineItemsAdapter(List<Discipline> list) {
            mDisciplineList = list;
        }

        @Override
        public DisciplineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_discipline, parent, false);
            return new DisciplineViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DisciplineViewHolder holder, int position) {
            holder.bindDisciplineItem(mDisciplineList.get(position));
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
                    Toast.makeText(getActivity(), "Prior", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.toolbar_navigator_btn_next:
                    Toast.makeText(getActivity(), "Next", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top +mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    public interface ToolbarI {
        void useToolbar(Toolbar toolbar);
    }
}
