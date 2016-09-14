package com.example.pilipenko.studentsapp.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.List;

public class InfiniteViewPager extends ViewPager {

    private OnPageChangeListener externalOnPageChangeListener = null;

    public InfiniteViewPager(Context context) {
        super(context);
        setActualOnPageChangeListener(new PageChangeListener(this));

    }

    private void setActualOnPageChangeListener(OnPageChangeListener listener) {
        super.setOnPageChangeListener(listener);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.externalOnPageChangeListener = listener;
    }

    private OnPageChangeListener getExternalOnPageChangeListener() {
        return this.externalOnPageChangeListener;
    }

    private static class PageChangeListener implements OnPageChangeListener {
        private final InfiniteViewPager viewPager;

        public PageChangeListener(final InfiniteViewPager viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            final OnPageChangeListener externalOnPageChangeListener =
                    viewPager.getExternalOnPageChangeListener();
            if (externalOnPageChangeListener != null) {
                externalOnPageChangeListener.onPageScrolled(
                        position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            final OnPageChangeListener externalOnPageChangeListener =
                    viewPager.getExternalOnPageChangeListener();
            if (externalOnPageChangeListener != null) {
                externalOnPageChangeListener.onPageSelected(position);
            }

            InfiniteViewPagerAdapter adapter = (InfiniteViewPagerAdapter) viewPager.getAdapter();
            List<Fragment> pagerFragments = adapter.getPagerFragments();
            // Ensure that cycling only occurs if there are 3 or more fragments.
            if (pagerFragments.size() > 2) {
                final int cycleResult = cyclePagerFragments(pagerFragments, position);
                if (cycleResult != 0) {
                    adapter.setPagerFragments(pagerFragments);
                    adapter.notifyDataSetChanged();

                    // Turn off the actual and external OnPageChangeListeners, so that
                    // this function does not unnecessarily get called again when
                    // setting the current item.
                    viewPager.setOnPageChangeListener(null);
                    viewPager.setActualOnPageChangeListener(null);
                    viewPager.setCurrentItem(position + cycleResult, false);
                    viewPager.setOnPageChangeListener(externalOnPageChangeListener);
                    viewPager.setActualOnPageChangeListener(this);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            final OnPageChangeListener externalOnPageChangeListener =
                    viewPager.getExternalOnPageChangeListener();
            if (externalOnPageChangeListener != null) {
                externalOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        private static int cyclePagerFragments(List<Fragment> pagerFragments, final int position) {
            final int lastPosition = pagerFragments.size() - 1;
            if (position == lastPosition) {
                pagerFragments.add(pagerFragments.remove(0));
                return -1;
            } else if (position == 0) {
                pagerFragments.add(0, pagerFragments.remove(lastPosition));
                return 1;
            }

            return 0;
        }
    }
}
