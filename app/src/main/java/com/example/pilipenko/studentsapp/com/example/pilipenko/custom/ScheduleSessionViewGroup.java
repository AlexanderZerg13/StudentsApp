package com.example.pilipenko.studentsapp.com.example.pilipenko.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.example.pilipenko.studentsapp.com.example.pilipenko.data.SessionLesson;

import java.util.List;

public class ScheduleSessionViewGroup extends LinearLayout {

    private List<SessionLesson> mSessionLessons;

    public ScheduleSessionViewGroup(Context context) {
        this(context, null, 0);
    }

    public ScheduleSessionViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleSessionViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        if (attrs != null) {

        }

        init(context);
    }

    public void addSession(List<SessionLesson> lessons) {
        mSessionLessons = lessons;
    }

    private void init(Context context) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
