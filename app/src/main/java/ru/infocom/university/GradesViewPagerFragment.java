package ru.infocom.university;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import ru.infocom.university.data.LessonProgress;
import ru.infocom.university.data.LessonProgressLab;
import ru.infocom.university.service.FetchDataIntentService;

import java.util.List;
import java.util.Map;

public class GradesViewPagerFragment extends AbstractViewPagerFragment<LessonProgress> {

    private static final String TAG = "GradesViewPagerFragment";

    public static GradesViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        GradesViewPagerFragment fragment = new GradesViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getTitle() {
        return R.string.grades_title;
    }

    @Override
    protected String getAction() {
        return FetchDataIntentService.ACTION_LESSONS_PROGRESS;
    }

    @Override
    protected Loader<Map<Integer, List<LessonProgress>>> getAsyncTaskLoader() {
        return new GradesAsyncTaskLoader(getContext());
    }

    @Override
    protected Intent getIntentToLoad() {
        return FetchDataIntentService.newIntentFetchLessonsProgress(
                getContext(),
                UserPreferences.getUser(getContext()).getId());
    }

    @Override
    protected Fragment getItemFragment(List<LessonProgress> list) {
        return GradesFragment.newInstance(list);
    }

    private static class GradesAsyncTaskLoader extends AsyncTaskLoader<Map<Integer, List<LessonProgress>>> {

        public GradesAsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        public Map<Integer, List<LessonProgress>> loadInBackground() {
            Log.i(TAG, "loadInBackground: ");
            LessonProgressLab lessonProgressLab = LessonProgressLab.get(getContext());
            return lessonProgressLab.getGroupLessonsProgress();
        }
    }

}
