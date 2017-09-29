package ru.infocom.university;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import ru.infocom.university.data.LessonProgress;

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
    protected Fragment getItemFragment(List<LessonProgress> list) {
        return null;
    }

}
