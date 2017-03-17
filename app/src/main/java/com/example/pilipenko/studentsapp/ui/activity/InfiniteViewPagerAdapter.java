package com.example.pilipenko.studentsapp.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class InfiniteViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> pagerFragments;

    public InfiniteViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public List<Fragment> getPagerFragments() {
        return pagerFragments;
    }

    public void setPagerFragments(final  List<Fragment> pagerFragments) {
        this.pagerFragments = pagerFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return (pagerFragments != null && pagerFragments.size() > position) ?
                pagerFragments.get(position) : null;
    }

    @Override
    public int getCount() {
        return (pagerFragments != null) ? pagerFragments.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
