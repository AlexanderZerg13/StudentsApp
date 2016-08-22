package com.example.pilipenko.studentsapp.com.example.pilipenko.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.example.pilipenko.studentsapp.R;

public class ScheduleViewGroup extends ScrollView {

    private static final String TAG = "ScheduleViewGroup";

    private static final int TEXT_SIZE_NUMBER = 14;
    private static final int TEXT_SIZE_TIME = 12;

    private float mOneRowHeight;

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
                mOneRowHeight = a.getDimension(R.styleable.ScheduleViewGroup_oneRowHeight, 200);
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
        this.addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.i(TAG, "onLayout: ");
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        //get the available size of child view
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;


        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                return;
            }

            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            //child.layout(convertDpToPixel(59), 0, 0, convertDpToPixel(80));

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
        int left = getPaddingLeft();
        int right = getPaddingRight();
        int top = getPaddingTop();


        int px05dp = convertDpToPixel(0.5f);
        int px2dp = convertDpToPixel(2);
        int px5dp = convertDpToPixel(5);
        int px9dp = convertDpToPixel(9);
        int px13dp = convertDpToPixel(13);
        int px35dp = convertDpToPixel(35);


        float y = 0.0f;
        for (int i = 1; i < 6; i++) {
            y += px13dp + mPaintTextNumber.getTextSize();
            canvas.drawText(Integer.toString(i), left, y, mPaintTextNumber);
            y += px2dp + mPaintTextTime.getTextSize();
            canvas.drawText("10:00", left, y, mPaintTextTime);
            y += px35dp + mPaintTextTime.getTextSize();
            canvas.drawText("11:20", left, y, mPaintTextTime);
            y += px9dp;
            mDivider.setBounds(0, (int) y, canvas.getWidth(), (int) y + px05dp);
            mDivider.draw(canvas);
        }


    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
