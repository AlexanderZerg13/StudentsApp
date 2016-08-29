package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.data.Discipline;
import com.example.pilipenko.studentsapp.data.StaticData;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;

import java.util.ArrayList;
import java.util.List;

public class DisciplineFragment extends Fragment {

    private IToolbar mToolbarActivity;
    private ITransitionActions mITransitionActionsActivity;

    private RecyclerView mRecyclerViewDiscipline;
    private ImageButton mNavigatorPriorImageButton;
    private ImageButton mNavigatorNextImageButton;
    private TextView mNavigatorSubTitle;

    private int mCurrentSemester = 2;
    private String mLastRequest;

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
        mToolbarActivity.useToolbar(toolbar, 0);

        NavigatorButtonOnClickListener onClickListener = new NavigatorButtonOnClickListener();
        mNavigatorPriorImageButton = (ImageButton) view.findViewById(R.id.toolbar_navigator_btn_prior);
        mNavigatorNextImageButton = (ImageButton) view.findViewById(R.id.toolbar_navigator_btn_next);
        mNavigatorPriorImageButton.setOnClickListener(onClickListener);
        mNavigatorNextImageButton.setOnClickListener(onClickListener);

        mNavigatorSubTitle = (TextView) view.findViewById(R.id.toolbar_navigator_tv_sub_title);

        mRecyclerViewDiscipline = (RecyclerView) view.findViewById(R.id.fragment_discipline_rv_disciplines);
        mRecyclerViewDiscipline.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewDiscipline.addItemDecoration(new Utils.SimpleDividerItemDecoration(getActivity()));


        updateUI();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolbarActivity = (IToolbar) context;
        mITransitionActionsActivity = (ITransitionActions) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarActivity = null;
        mITransitionActionsActivity = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.discipline, menu);
        MenuItem searchItem = menu.findItem(R.id.discipline_menu_item_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.fragment_choose_education_et_input_discipline));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mLastRequest = query;
                updateUISearch();
                hideSoftKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mLastRequest = newText;
                updateUISearch();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.discipline_menu_item_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        mNavigatorSubTitle.setText(StaticData.sSemesters.get(mCurrentSemester).getSemesterName());
        DisciplineItemsAdapter adapter = new DisciplineItemsAdapter(StaticData.sSemesters.get(mCurrentSemester).getDisciplineList());
        mRecyclerViewDiscipline.setAdapter(adapter);
    }

    private void updateUISearch() {
        if (TextUtils.isEmpty(mLastRequest)) {
            updateUI();
            return;
        }
        List<Discipline> list = StaticData.sSemesters.get(mCurrentSemester).getDisciplineList();
        List<Discipline> listAdapter = new ArrayList<>();
        for (Discipline discipline : list) {
            if (Utils.checkContains(discipline.getName(), mLastRequest)) {
                listAdapter.add(discipline);
            }
        }
        DisciplineItemsAdapter adapter = new DisciplineItemsAdapter(listAdapter);
        mRecyclerViewDiscipline.setAdapter(adapter);

    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) DisciplineFragment.this
                .getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        IBinder iBinder = getActivity().getCurrentFocus().getWindowToken();
        if (iBinder != null) {
            imm.hideSoftInputFromWindow(iBinder, 0);
        }
    }

    private class DisciplineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mTeacherTextView;
        private TextView mTypeTextView;
        private TextView mHoursTextView;

        private int mDisciplinePosition;

        public DisciplineViewHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.item_discipline_tv_name);
            mTeacherTextView = (TextView) itemView.findViewById(R.id.item_discipline_tv_teacher_name);
            mTypeTextView = (TextView) itemView.findViewById(R.id.item_discipline_tv_type);
            mHoursTextView = (TextView) itemView.findViewById(R.id.item_discipline_tv_lesson_hours);

            itemView.setOnClickListener(this);
        }

        public void bindDisciplineItem(Discipline discipline, int position) {
            mDisciplinePosition = position;
            if (!TextUtils.isEmpty(mLastRequest)) {
                mNameTextView.setText(Utils.getSpannableStringMatches(discipline.getName(), mLastRequest));
            } else {
                mNameTextView.setText(discipline.getName());
            }
            mTeacherTextView.setText(discipline.getTeacherName());
            mHoursTextView.setText(discipline.getHours() + " ч");
            String type = discipline.getType();
            mTypeTextView.setText(type);
            if (type.equalsIgnoreCase("Экзамен")) {
                mTypeTextView.setTextColor(getResources().getColor(R.color.colorDeepOrange));
            } else {
                mTypeTextView.setTextColor(getResources().getColor(R.color.colorPink));
            }
        }

        @Override
        public void onClick(View view) {
            mLastRequest = null;
            hideSoftKeyboard();
            mITransitionActionsActivity.goToDescribeDiscipline(mCurrentSemester, mDisciplinePosition);
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
            holder.bindDisciplineItem(mDisciplineList.get(position), position);
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
