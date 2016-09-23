package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.data.LessonProgressLab;
import com.example.pilipenko.studentsapp.interfaces.IToolbar;
import com.example.pilipenko.studentsapp.interfaces.ITransitionActions;
import com.example.pilipenko.studentsapp.service.FetchDataIntentService;

import java.util.HashMap;
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
    protected Loader<Map<String, List<LessonProgress>>> getAsyncTaskLoader() {
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

    private static class GradesAsyncTaskLoader extends AsyncTaskLoader<Map<String, List<LessonProgress>>> {

        public GradesAsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        public Map<String, List<LessonProgress>> loadInBackground() {
            Log.i(TAG, "loadInBackground: ");
            LessonProgressLab lessonProgressLab = LessonProgressLab.get(getContext());
            Map<String, List<LessonProgress>> map = lessonProgressLab.getGroupLessonsProgress();

            return map;
        }
    }

}
