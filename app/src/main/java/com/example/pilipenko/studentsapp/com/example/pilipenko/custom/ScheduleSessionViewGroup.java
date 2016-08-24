package com.example.pilipenko.studentsapp.com.example.pilipenko.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.com.example.pilipenko.data.SessionLesson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScheduleSessionViewGroup extends LinearLayout {

    private static final String TAG = "ScheduleSessionViewGroup";

    private static final int TEXT_SIZE_TIME = 12;
    private static final int TEXT_SIZE_DATA = 14;

    private float mOneRowHeight;
    private float mPaddingToCard;
    private float mSpaceLeft;
    private float mSpaceRight;
    private float mSpaceTop;

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
        mSessionLessons = lessons;
        invalidate();
    }

    private void init(Context context) {

        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        mPaintTime = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTime.setColor(ContextCompat.getColor(context, R.color.colorTextCity));
        mPaintTime.setTextSize(scaledDensity * TEXT_SIZE_TIME);

        mPaintDate = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintDate.setColor(ContextCompat.getColor(context, R.color.colorTextChoose));
        mPaintDate.setTextSize(scaledDensity * TEXT_SIZE_DATA);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        SimpleDateFormat sdf = new SimpleDateFormat ("d MMMM, EEEE", new Locale("ru"));
        SimpleDateFormat sdfTime = new SimpleDateFormat("k:mm");

        float dateTabSpaceLeft = mSpaceLeft + mPaddingToCard + mSpaceTop;
        float dateTabSpaceTop = mSpaceTop + mPaintDate.getTextSize();

        float timeTabSpaceLeft = mSpaceLeft;
        float timeTabSpaceTop = mSpaceTop * 3 + mPaintDate.getTextSize() + mPaintTime.getTextSize();

        float y = 0.0f;
        for (int i = 0; i < mSessionLessons.size(); i++) {
            SessionLesson sessionLesson = mSessionLessons.get(i);
            canvas.drawText(sdf.format(sessionLesson.getDate()), dateTabSpaceLeft, y + dateTabSpaceTop, mPaintDate);

            canvas.drawText(sdfTime.format(sessionLesson.getDate()), timeTabSpaceLeft, y + timeTabSpaceTop, mPaintTime);

            y += mOneRowHeight;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        if (mSessionLessons != null) {
            size = mSessionLessons.size();
        } else {
            size = MeasureSpec.getSize(heightMeasureSpec);
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) (mOneRowHeight * size));
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
