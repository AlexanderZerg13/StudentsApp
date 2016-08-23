package com.example.pilipenko.studentsapp.com.example.pilipenko.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.pilipenko.studentsapp.R;

public class ScheduleViewGroup extends LinearLayout {

    private static final String TAG = "ScheduleViewGroup";

    private static final int TEXT_SIZE_NUMBER = 14;
    private static final int TEXT_SIZE_TIME = 12;

    private float mOneRowHeight;
    private float mPaddingToCard;

    private Paint mPaintTextNumber;
    private Paint mPaintTextTime;
    private Drawable mDivider;

    private int deviceWidth;

    public ScheduleViewGroup(Context context) {
        this(context, null, 0);
    }

    public ScheduleViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ScheduleViewGroup,
                    0, 0);
            try {
                mOneRowHeight = a.getDimension(R.styleable.ScheduleViewGroup_oneRowHeight, 100);
                mPaddingToCard = a.getDimension(R.styleable.ScheduleViewGroup_paddingToCard, 47);
            } finally {
                a.recycle();
            }
        }
        init(context);
    }

    private void init(Context context) {
        //Device Width
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;
        //-----------

        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        int densityDpi = getContext().getResources().getDisplayMetrics().densityDpi;

        mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider);

        mPaintTextNumber = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTextNumber.setColor(ContextCompat.getColor(getContext(), R.color.colorTextChoose));
        mPaintTextNumber.setTextSize(scaledDensity * TEXT_SIZE_NUMBER);

        mPaintTextTime = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTextTime.setColor(ContextCompat.getColor(getContext(), R.color.colorTextChooseAlpha));
        mPaintTextTime.setTextSize(scaledDensity * TEXT_SIZE_TIME);

        //DELETE
        testData();
    }

    private void testData() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_schedule_view_group_lesson, this, false);
        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.item_schedule_view_group_lesson, this, false);
        ((CardView)view2).setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLessonCardLab));
        this.addView(view);
        this.addView(view2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        int curWidth, curHeight, curLeft, curTop;

        final int childLeft = this.getPaddingLeft() + (int) mPaddingToCard;
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childTop = this.getPaddingTop();
        final int childWidth = childRight - childLeft;

        curLeft = childLeft;
        curTop = childTop;

        //Why???? Понять, почему не нужно вычитать childTop дважды!
        final int childHeightNormal = (int) mOneRowHeight - childTop;
        final int childHeightBig = (int) (mOneRowHeight * 2);

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                return;
            }
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(childHeightNormal, MeasureSpec.AT_MOST));


            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

            curTop += curHeight + childTop * 2;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();

        float numberTextSize = mPaintTextNumber.getTextSize();
        float timeTextSize = mPaintTextTime.getTextSize();
        int firstSpace = convertDpToPixel(5);
        int secondSpace = convertDpToPixel(2);
        int thirdSpace;
        int fourthSpace = convertDpToPixel(9);
        int dividerHeight = convertDpToPixel(0.5f);

        thirdSpace = Math.round(mOneRowHeight - (firstSpace + secondSpace + fourthSpace + numberTextSize + (timeTextSize * 2)));


        float y = 0.0f;
        for (int i = 1; i < 6; i++) {
            y += paddingTop + firstSpace + numberTextSize;
            canvas.drawText(Integer.toString(i), paddingLeft, y, mPaintTextNumber);
            y += secondSpace + timeTextSize;
            canvas.drawText("10:00", paddingLeft, y, mPaintTextTime);
            y += thirdSpace + timeTextSize;
            canvas.drawText("11:20", paddingLeft, y, mPaintTextTime);
            y += fourthSpace;
            mDivider.setBounds(0, (int) y, canvas.getWidth(), (int) y + dividerHeight);
            mDivider.draw(canvas);
        }


    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
