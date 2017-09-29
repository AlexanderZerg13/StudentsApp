package ru.infocom.university;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import ru.infocom.university.data.LessonPlan;
import ru.infocom.university.data.LessonPlanLab;
import ru.infocom.university.service.FetchDataIntentService;

import java.util.List;
import java.util.Map;

public class AcademicPlanViewPagerFragment extends AbstractViewPagerFragment<LessonPlan> {

    private static final String TAG = "AcademicPlanVPFragment";

    private String mLastRequest;
    private boolean showSearch;

    public static AcademicPlanViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        AcademicPlanViewPagerFragment fragment = new AcademicPlanViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.discipline, menu);
        MenuItem searchItem = menu.findItem(R.id.discipline_menu_item_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.fragment_choose_education_et_input_discipline));
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                setPagingEnabled(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mLastRequest = null;
                setPagingEnabled(true);
                updateUISearch();
                return true;
            }
        });

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
        updateOptionsMenu(menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateOptionsMenu(menu);
    }

    private void updateOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.discipline_menu_item_search);
        searchItem.setVisible(showSearch);
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

    @Override
    protected int getTitle() {
        return R.string.discipline_describe;
    }

    @Override
    protected Fragment getItemFragment(List<LessonPlan> list) {
        return AcademicPlanFragment.newInstance(list);
    }

    @Override
    protected void reloadData() {

    }


    private void updateUISearch() {

        Fragment fragment = getCurrentFragment();
        if (fragment instanceof Filter) {
            ((Filter) fragment).doFilter(mLastRequest);
        } else {
            throw new IllegalStateException("Must implements Filter");
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) AcademicPlanViewPagerFragment.this
                .getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        IBinder iBinder = getActivity().getCurrentFocus().getWindowToken();
        if (iBinder != null) {
            imm.hideSoftInputFromWindow(iBinder, 0);
        }
    }


    public interface Filter {
        void doFilter(String str);
    }
}
