package ru.infocom.university.modules.grades;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import java.util.List;

import ru.infocom.university.AbstractViewPagerFragment;
import ru.infocom.university.DataPreferenceManager;
import ru.infocom.university.R;
import ru.infocom.university.StudentApplication;
import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.LessonProgress;
import ru.infocom.university.model.RecordBook;
import ru.infocom.university.network.DataRepository;
import rx.Subscription;

public class GradesViewPagerFragment extends AbstractViewPagerFragment<LessonProgress> {

    private static final String TAG = "GradesViewPagerFragment";

    private DataRepository mDataRepository;
    private Subscription mGetEducationalPerformance;

    public static GradesViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        GradesViewPagerFragment fragment = new GradesViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRepository = new DataRepository(DataPreferenceManager.provideUserPreferences().getUniversityId(getActivity()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doFetchEducationalPerformance();
    }

    @Override
    public void onDestroy() {
        if (mGetEducationalPerformance != null) {
            mGetEducationalPerformance.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    protected int getTitle() {
        return R.string.grades_title;
    }

    @Override
    protected int getErrorString() {
        return R.string.errorLessonsProgress;
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

        mGetEducationalPerformance = mDataRepository
                .getEducationalPerformance(authorizationObject.getId(), recordBook.getRecordBookId())
                .doOnSubscribe(this::showLoading)
                .doOnTerminate(this::hideLoading)
                .subscribe(
                        stringListMap -> {
                            updateAdapter(stringListMap);
                            showNavigatorLayout();
                            Log.i(TAG, "doFetchEducationalPerformance: Success");
                        },
                        throwable -> {
                            Log.i(TAG, "doFetchEducationalPerformance: Error" + throwable);
                            showErrorNetwork();
                        });
    }

}
