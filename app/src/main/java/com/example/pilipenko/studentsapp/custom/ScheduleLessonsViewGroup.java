package com.example.pilipenko.studentsapp.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.data.Lesson;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ScheduleLessonsViewGroup extends LinearLayout {

    private static final String TAG = "ScheduleViewGroup";

    private static final int TEXT_SIZE_NUMBER = 14;
    private static final int TEXT_SIZE_TIME = 12;

    private static final String[] arrayStart = {"8:30", "10:00", "11:30", "13:10", "14:40", "16:10", "17:40"};
    private static final String[] arrayEnd = {"9:50", "11:20", "12:50", "14:30", "16:00", "17:30", "19:00"};

    private static final int MIN_COUNT = 5;

    private float mOneRowHeight;
    private float mPaddingToCard;
    private float mSpaceLeft;
    private float mSpaceRight;
    private float mSpaceTop;

    private boolean mIsInformation;
    private String mSessionFrom;
    private String mSessionTo;

    private Paint mPaintTextNumber;
    private Paint mPaintTextTime;
    private Paint mPaintEmptyPair;
    private Drawable mDivider;

    private int deviceWidth;

    private List<Lesson> mLessonList;

    public ScheduleLessonsViewGroup(Context context) {
        this(context, null, 0);
    }

    public ScheduleLessonsViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleLessonsViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ScheduleLessonsViewGroup,
                    0, 0);
            try {
                mOneRowHeight = a.getDimension(R.styleable.ScheduleLessonsViewGroup_oneRowHeight, convertDpToPixel(100));
                mPaddingToCard = a.getDimension(R.styleable.ScheduleLessonsViewGroup_spaceToCard, convertDpToPixel(47));
                mSpaceLeft = a.getDimension(R.styleable.ScheduleLessonsViewGroup_spaceLeft, convertDpToPixel(12));
                mSpaceRight = a.getDimension(R.styleable.ScheduleLessonsViewGroup_spaceRight, convertDpToPixel(8));
                mSpaceTop = a.getDimension(R.styleable.ScheduleLessonsViewGroup_spaceTop, convertDpToPixel(8));
                mIsInformation = a.getBoolean(R.styleable.ScheduleLessonsViewGroup_isInformation, false);
                mSessionFrom = a.getString(R.styleable.ScheduleLessonsViewGroup_sessionFrom);
                mSessionTo = a.getString(R.styleable.ScheduleLessonsViewGroup_sessionTo);
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

        mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider_05dp);

        mPaintTextNumber = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTextNumber.setColor(ContextCompat.getColor(getContext(), R.color.colorBlack_87a));
        mPaintTextNumber.setTextSize(scaledDensity * TEXT_SIZE_NUMBER);

        mPaintTextTime = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTextTime.setColor(ContextCompat.getColor(getContext(), R.color.colorBlack_37a));
        mPaintTextTime.setTextSize(scaledDensity * TEXT_SIZE_TIME);

        mPaintEmptyPair = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintEmptyPair.setColor(ContextCompat.getColor(getContext(), R.color.colorBlack_37a));
        mPaintEmptyPair.setTextSize(scaledDensity * TEXT_SIZE_NUMBER);


        setIsInformation(mIsInformation, null, null, null);
        //DELETE
        //testData();
        //addTimeLine();
    }

    //Getters and Setters
    public boolean isInformation() {
        return mIsInformation;
    }

    public void setIsInformation(boolean isSession, String titleText, String subTitleText, String buttonText, OnClickListener listener) {
        mIsInformation = isSession;
        if (mIsInformation) {
            this.removeAllViews();
            mLessonList = null;

            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_schedule_view_group_session_background, this, false);

            TextView textViewTitle = (TextView) view.findViewById(R.id.item_schedule_view_group_session_background_tv_title);
            TextView textViewSubTitle = (TextView) view.findViewById(R.id.item_schedule_view_group_session_background_tv_sub_title);
            Button buttonGoTo = (Button) view.findViewById(R.id.item_schedule_view_group_session_background_btn_go_to);

            if (TextUtils.isEmpty(titleText)) {
                textViewTitle.setVisibility(GONE);
            } else {
                textViewTitle.setText(titleText);
            }

            if (TextUtils.isEmpty(subTitleText)) {
                textViewSubTitle.setVisibility(GONE);
            } else {
                textViewSubTitle.setText(subTitleText);
            }

            if (TextUtils.isEmpty(buttonText)) {
                buttonGoTo.setVisibility(GONE);
            } else {
                buttonGoTo.setText(buttonText);
                if (listener != null) {
                    buttonGoTo.setOnClickListener(listener);
                }
            }

            this.addView(view);
        }
        invalidate();
    }

    public void setIsInformation(boolean isSession, String titleText, String subTitleText, String buttonText) {
        setIsInformation(isSession, titleText, subTitleText, buttonText, null);
    }


    public String getSessionFrom() {
        return mSessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        mSessionFrom = sessionFrom;
        invalidate();
    }

    public String getSessionTo() {
        return mSessionTo;
    }

    public void setSessionTo(String sessionTo) {
        mSessionTo = sessionTo;
        invalidate();
    }
    //*******************

    public void addLessons(List<Lesson> lessons, OnClickListener listener) {
        this.removeAllViews();
        mIsInformation = false;

        mLessonList = lessons;
        for (Lesson l : mLessonList) {
            if (l.isEmpty()) {
                continue;
            }
            CardView view = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.item_schedule_view_group_lesson, this, false);
            ViewGroup clickVied = (ViewGroup) view.findViewById(R.id.item_schedule_view_group_lesson_card);
            TextView name = (TextView) view.findViewById(R.id.item_schedule_view_group_lesson_name);
            TextView type = (TextView) view.findViewById(R.id.item_schedule_view_group_lesson_type);
            TextView teacher = (TextView) view.findViewById(R.id.item_schedule_view_group_lesson_teacher);
            TextView audience = (TextView) view.findViewById(R.id.item_schedule_view_group_lesson_audience);
            name.setText(l.getName());
            type.setText(l.getType());
            teacher.setText(l.getTeacherName());
            audience.setText(l.getAudience());
            clickVied.setTag(l);
            clickVied.setOnClickListener(listener);

            switch (l.getType()) {
                case "ЛЕК":
                    view.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen2));
                    break;
                case "ЛАБ":
                    view.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue2));
                    break;
            }

            view.setTag(l);
            this.addView(view);
        }

        addTimeLine();

        invalidate();
    }

    private void addTimeLine() {
        View line = new LineView(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            line.setElevation(15.0f);
        }
        this.addView(line);
    }

    private void onLayoutTimeLine(LineView line) {
        float lineHeight = 1f;
        Calendar calendar = GregorianCalendar.getInstance();
        int startFromHour = 8;
        int startFromMinute = 30;
        //update
        int minute = (calendar.get(Calendar.HOUR_OF_DAY) - startFromHour) * 60 + calendar.get(Calendar.MINUTE) - startFromMinute;
//        int minute = (11 - startFromHour) * 60 + 25;
        float delta = 85.0f;
        int position = (int) ((minute / delta) * mOneRowHeight) - convertDpToPixel(lineHeight);


        line.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(convertDpToPixel(lineHeight), MeasureSpec.AT_MOST));

        line.layout(0, position, this.getMeasuredWidth(), position + convertDpToPixel(lineHeight));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        int curWidth, curHeight, curLeft, curTop;

        final int childLeft = (int) (mSpaceLeft + mPaddingToCard);
        final int childRight = (int) (this.getMeasuredWidth() - mSpaceRight);
        final int childTop = (int) mSpaceTop;
        final int childWidth = childRight - childLeft;

        curLeft = childLeft;
        curTop = childTop;

        final int childHeightNormal = (int) mOneRowHeight - childTop * 2;
        final int childHeightBig = (int) (mOneRowHeight * 2) - childTop * 2;

        int iterator = 0;
        View child;

        if (mIsInformation) {
            child = getChildAt(0);
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), MeasureSpec.EXACTLY));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();
            child.layout(curLeft, 0, curLeft + curWidth, curHeight);
            return;
        }
        if (mLessonList == null) {
            return;
        }
        for (int i = 0; i < mLessonList.size(); i++) {

            Lesson lesson = mLessonList.get(i);
            if (lesson.isEmpty()) {
                curTop += childHeightNormal + childTop * 2 + convertDpToPixel(0.5f);
                continue;
            }

            child = getChildAt(iterator);
            iterator++;
            if (child.getVisibility() == GONE) {
                return;
            }

            if (child instanceof LineView) {
                onLayoutTimeLine((LineView) child);
                continue;
            }

            int height = childHeightNormal;
            if (lesson.isTwoPair()) {
                height = childHeightBig;
            }
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

            curTop += curHeight + childTop * 2 + convertDpToPixel(0.5f);
        }

        if (iterator < count) {
            child = getChildAt(iterator);
            if (child instanceof LineView) {
                onLayoutTimeLine((LineView) child);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightFromParent = MeasureSpec.getSize(heightMeasureSpec);

        int heightFromRowHeight = (int) (mOneRowHeight * minCount());

        int measureHeight;
        if (heightFromParent > heightFromRowHeight || mIsInformation) {
            measureHeight = heightFromParent;
        } else {
            measureHeight = heightFromRowHeight;
        }

        setMeasuredDimension(deviceWidth, measureHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int countToDraw = minCount();
        float spaceLeft = mSpaceLeft;
        float spaceTop = mSpaceTop;

        float numberTextSize = mPaintTextNumber.getTextSize();
        float timeTextSize = mPaintTextTime.getTextSize();
        int firstSpace = convertDpToPixel(5);
        int secondSpace = convertDpToPixel(2);
        int thirdSpace;
        int fourthSpace = convertDpToPixel(9);
        int dividerHeight = convertDpToPixel(0.5f);

        thirdSpace = Math.round(mOneRowHeight - (firstSpace + secondSpace + fourthSpace + numberTextSize + (timeTextSize * 2) + spaceTop));

        int leftTab = (int) (spaceLeft + mPaddingToCard + convertDpToPixel(13));
        float y = 0.0f;

        int twoPairCount = 0;
        boolean[] emptyIndex = new boolean[countToDraw];
        if (!mIsInformation && mLessonList != null) {
            for (int i = 0; i < mLessonList.size(); i++) {
                Lesson lesson = mLessonList.get(i);
                if (lesson.isTwoPair()) {
                    twoPairCount++;
                } else if (lesson.isEmpty() && (i + twoPairCount < emptyIndex.length)) {
                    emptyIndex[i + twoPairCount] = true;
                }
            }
        }

        for (int i = 1; i < countToDraw + 1; i++) {
            y += spaceTop + firstSpace + numberTextSize;
            canvas.drawText(Integer.toString(i), spaceLeft, y, mPaintTextNumber);
            if (emptyIndex[i - 1]) {
                canvas.drawText(getResources().getString(R.string.empty_pair), leftTab, y, mPaintEmptyPair);
            }
            y += secondSpace + timeTextSize;
            canvas.drawText(arrayStart[i - 1], spaceLeft, y, mPaintTextTime);
            y += thirdSpace + timeTextSize;
            canvas.drawText(arrayEnd[i - 1], spaceLeft, y, mPaintTextTime);
            y += fourthSpace;
            if (i != countToDraw) {
                mDivider.setBounds(0, (int) y, canvas.getWidth(), (int) y + dividerHeight);
                mDivider.draw(canvas);
                y += dividerHeight;
            }
        }

        if (mIsInformation) {
            this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhiteYellow2));
        } else {
            this.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        }
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private int minCount() {


        int min = MIN_COUNT;

        if (mLessonList != null) {
            int k = 0;
            for (int i = 0; i < mLessonList.size(); i++) {
                k += mLessonList.get(i).isTwoPair() ? 2 : 1;
            }

            int bottomSpace = 0;
            for (int i = mLessonList.size() - 1; i > 0; i--) {
                Lesson lesson = mLessonList.get(i);
                if (lesson.isEmpty()) {
                    bottomSpace++;
                } else {
//                if (mLessonList.get(i).isTwoPair() && mBottomSpace > 0) {
//                    mBottomSpace--;
//                }
                    break;
                }
            }

            k -= bottomSpace;

            min = k > min ? k : min;
        }
        return min;
    }

    private class LineView extends View {
        private Paint paint = new Paint();

        public LineView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public LineView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.colorYellow2));
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        }
    }
}
