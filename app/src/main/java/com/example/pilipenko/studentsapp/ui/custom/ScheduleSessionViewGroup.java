package com.example.pilipenko.studentsapp.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.data.SessionLesson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ScheduleSessionViewGroup extends LinearLayout {

    private static final String TAG = "ScheduleSessionViewGroup";

    private static final int TEXT_SIZE_TIME = 12;
    private static final int TEXT_SIZE_DATA = 14;

    private SimpleDateFormat sdfDate;
    private SimpleDateFormat sdfTime;

    private float mOneRowHeight;
    private float mOneRowHeightMin;
    private float mPaddingToCard;
    private float mSpaceLeft;
    private float mSpaceRight;
    private float mSpaceTop;
    private float mSpaceBetweenCard;

    private Paint mPaintTime;
    private Paint mPaintDate;

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
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ScheduleSessionViewGroup,
                    0, 0);
            try {
                mOneRowHeight = a.getDimension(R.styleable.ScheduleSessionViewGroup_oneRowHeightSession, convertDpToPixel(141));
                mPaddingToCard = a.getDimension(R.styleable.ScheduleSessionViewGroup_spaceToCardSession, convertDpToPixel(47));
                mSpaceLeft = a.getDimension(R.styleable.ScheduleSessionViewGroup_spaceLeftSession, convertDpToPixel(12));
                mSpaceRight = a.getDimension(R.styleable.ScheduleSessionViewGroup_spaceRightSession, convertDpToPixel(8));
                mSpaceTop = a.getDimension(R.styleable.ScheduleSessionViewGroup_spaceTopSession, convertDpToPixel(13));
            } finally {
                a.recycle();
            }
        }

        init(context);
    }

    public void addSession(List<SessionLesson> lessons) {
        this.removeAllViews();
        mSessionLessons = lessons;
        Collections.sort(mSessionLessons, new Comparator<SessionLesson>() {
            @Override
            public int compare(SessionLesson s1, SessionLesson s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        for (SessionLesson lesson : mSessionLessons) {
            CardView view = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.item_schedule_view_group_lesson, this, false);
            TextView name = (TextView) view.findViewById(R.id.item_schedule_view_group_lesson_name);
            TextView type = (TextView) view.findViewById(R.id.item_schedule_view_group_lesson_type);
            TextView teacher = (TextView) view.findViewById(R.id.item_schedule_view_group_lesson_teacher_or_group);
            TextView audience = (TextView) view.findViewById(R.id.item_schedule_view_group_lesson_audience);

            name.setText(lesson.getName());
            type.setText(lesson.getType().toString());
            teacher.setText(lesson.getTeacher());
            audience.setText(lesson.getAudience());

            int color = -1;
            switch (lesson.getType()) {
                case EXAM:
                    color = ContextCompat.getColor(getContext(), R.color.colorDeepOrange);
                    break;
                case POINT:
                    color = ContextCompat.getColor(getContext(), R.color.colorPink);
                    break;
                case CONSULT:
                    color = ContextCompat.getColor(getContext(), R.color.colorBlue1);
                    break;
                default:
                    throw new IllegalStateException("Lesson type don't recognize");
            }
            view.setCardBackgroundColor(color);

            this.addView(view);
        }

        invalidate();
    }

    private void init(Context context) {

        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;


        Typeface tfRobotoRegular = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        mPaintTime = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTime.setColor(ContextCompat.getColor(context, R.color.colorBlack_54a));
        mPaintTime.setTextSize(scaledDensity * TEXT_SIZE_TIME);
        mPaintTime.setTypeface(tfRobotoRegular);

        Typeface tfRobotoMedium = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaintDate = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintDate.setColor(ContextCompat.getColor(context, R.color.colorBlack_87a));
        mPaintDate.setTextSize(scaledDensity * TEXT_SIZE_DATA);
        mPaintDate.setTypeface(tfRobotoMedium);

        Locale locale = new Locale("ru");
        sdfDate = new SimpleDateFormat("d MMMM, EEEE", locale);
        sdfTime = new SimpleDateFormat("k:mm", locale);

        mSpaceBetweenCard = convertDpToPixel(4);
        mOneRowHeightMin = (int) (mOneRowHeight - mSpaceTop * 3 - mPaintDate.getTextSize() + mSpaceBetweenCard);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float dateTabSpaceLeft = mSpaceLeft + mPaddingToCard + mSpaceTop;
        float dateTabSpaceTop = mSpaceTop + mPaintDate.getTextSize();

        float timeTabSpaceFromCard = convertDpToPixel(17);

        float timeTabSpaceLeft = mSpaceLeft;
        float timeTabSpaceTop = mSpaceTop * 2 + timeTabSpaceFromCard + mPaintDate.getTextSize() + mPaintTime.getTextSize();

        float y = -mOneRowHeight;
        Calendar priorDay = null;
        Calendar curDay = GregorianCalendar.getInstance();
        for (int i = 0; i < mSessionLessons.size(); i++) {
            SessionLesson sessionLesson = mSessionLessons.get(i);
            curDay.setTime(sessionLesson.getDate());

            if (!isTheSameDay(curDay, priorDay)) {
                y += mOneRowHeight;
                canvas.drawText(sdfDate.format(sessionLesson.getDate()), dateTabSpaceLeft, y + dateTabSpaceTop, mPaintDate);
                canvas.drawText(sdfTime.format(sessionLesson.getDate()), timeTabSpaceLeft, y + timeTabSpaceTop, mPaintTime);
            } else {
                y += mOneRowHeightMin;
                canvas.drawText(sdfTime.format(sessionLesson.getDate()), timeTabSpaceLeft, y + timeTabSpaceTop, mPaintTime);
            }

            priorDay = (Calendar) curDay.clone();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        int curWidth, curHeight, curLeft, curTop;

        final int childPlaceLeft = (int) (mSpaceLeft + mPaddingToCard);
        final int childPlaceRight = (int) (this.getMeasuredWidth() - mSpaceRight);
        final int childPlaceTop = (int) (mSpaceTop * 2 + mPaintDate.getTextSize());

        final int childWidth = childPlaceRight - childPlaceLeft;
        final int childHeight = (int) (mOneRowHeight - mSpaceTop * 3 - mPaintDate.getTextSize());

        curTop = (int) (childPlaceTop - mOneRowHeight);
        curLeft = childPlaceLeft;

        Calendar priorDay = null;
        Calendar curDay = GregorianCalendar.getInstance();
        for (int i = 0; i < count; i++) {
            SessionLesson sessionLesson = mSessionLessons.get(i);
            curDay.setTime(sessionLesson.getDate());
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                return;
            }

            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            if (!isTheSameDay(curDay, priorDay)) {
                curTop += mOneRowHeight;
            } else {
                curTop += mOneRowHeightMin;
            }

            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

            priorDay = (Calendar) curDay.clone();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        Calendar priorDay = null;
        Calendar curDay = GregorianCalendar.getInstance();
        if (mSessionLessons != null) {
            for(int i = 0; i < mSessionLessons.size(); i++) {
                SessionLesson sessionLesson = mSessionLessons.get(i);
                curDay.setTime(sessionLesson.getDate());
                if (isTheSameDay(curDay, priorDay)) {
                    height += mOneRowHeightMin;
                } else {
                    height += mOneRowHeight;
                }

                priorDay = (Calendar) curDay.clone();
            }
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private boolean isTheSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }
        if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)){
            return true;
        }
        return false;
    }
}
