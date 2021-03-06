package ru.infocom.university.modules.academicPlan;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ru.infocom.university.AbstractViewPagerFragment;
import ru.infocom.university.DataPreferenceManager;
import ru.infocom.university.R;
import ru.infocom.university.StudentApplication;
import ru.infocom.university.data.LessonPlan;
import ru.infocom.university.model.RecordBook;
import ru.infocom.university.network.DataRepository;
import ru.infocom.university.network.ExceptionUtils;
import rx.Subscription;

import java.util.List;

public class AcademicPlanViewPagerFragment extends AbstractViewPagerFragment<LessonPlan> {

    private static final String TAG = "AcademicPlanVPFragment";

    private String mLastRequest;
    private DataRepository mDataRepository;
    private Subscription mGetAcademicPlanSubscription;
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
        mDataRepository = new DataRepository(DataPreferenceManager.provideUserPreferences().getUniversityId(getActivity()));

        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        if (mGetAcademicPlanSubscription != null) {
            mGetAcademicPlanSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doFetchAcademicPlan();
    }

    /*TODO need to return search*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
/*

        if (menu.findItem(R.id.discipline_menu_item_search) != null) {
            return;
        }

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
        updateOptionsMenu(menu);*/
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        /*updateOptionsMenu(menu);*/
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
        doFetchAcademicPlan();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        showSearch = false;
        getActivity().invalidateOptionsMenu();
    }

    private void doFetchAcademicPlan() {
        RecordBook recordBook = ((StudentApplication) getActivity().getApplication()).getRecordBookSelected();

        mGetAcademicPlanSubscription = mDataRepository
                .getAcademicPlan(recordBook.getCurriculumId())
                .doOnSubscribe(this::showLoading)
                .doOnTerminate(this::hideLoading)
                .subscribe(
                        stringListMap -> {
                            showSearch = true;
                            updateAdapter(stringListMap);
                            showNavigatorLayout();
                            Log.i(TAG, "doFetchEducationalPerformance: Success" + stringListMap);
                        },
                        throwable -> {
                            Log.i(TAG, "doFetchEducationalPerformance: Error" + throwable);
                            showErrorNetwork(ExceptionUtils.getErrorText(throwable, getResources()));
                        });

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
