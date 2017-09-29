package ru.infocom.university;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.LessonProgress;
import ru.infocom.university.model.RecordBook;
import ru.infocom.university.network.DataRepository;

public class GradesViewPagerFragment extends AbstractViewPagerFragment<LessonProgress> {

    private static final String TAG = "GradesViewPagerFragment";

    private DataRepository mDataRepository;

    public static GradesViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        GradesViewPagerFragment fragment = new GradesViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRepository = new DataRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        doFetchEducationalPerformance();

        return view;
    }

    @Override
    protected int getTitle() {
        return R.string.grades_title;
    }

    @Override
    protected Fragment getItemFragment(List<LessonProgress> list) {
        return GradesFragment.newInstance(list);
    }

    @Override
    protected void reloadData() {
        doFetchEducationalPerformance();
    }

    private void doFetchEducationalPerformance() {
        AuthorizationObject authorizationObject = DataPreferenceManager.provideUserPreferences().getUser(this.getActivity());
        RecordBook recordBook = ((StudentApplication)getActivity().getApplication()).getRecordBookSelected();

        mDataRepository
                .getEducationalPerformance(authorizationObject.getId(), recordBook.getRecordBookId())
                .doOnSubscribe(this::showLoading)
                .doOnTerminate(this::hideLoading)
                .subscribe(
                        integerListMap -> {
                            updateAdapter(integerListMap);
                            showNavigatorLayout();
                            Log.i(TAG, "doFetchEducationalPerformance: Success");
                        },
                        throwable -> {
                            Log.i(TAG, "doFetchEducationalPerformance: Error" + throwable);
                            showErrorNetwork();
                        });
    }

}
