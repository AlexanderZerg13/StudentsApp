package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class DisciplineFragment extends Fragment {

    private ToolbarI mToolbarActivity;

    private ImageButton mNavigatorPriorImageButton;
    private ImageButton mNavigatorNextImageButton;

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

    public interface ToolbarI {
        void useToolbar(Toolbar toolbar);
    }
}
