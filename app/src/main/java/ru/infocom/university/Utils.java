package ru.infocom.university;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import ru.infocom.university.data.AuthorizationObject;
import ru.infocom.university.data.Lesson;
import ru.infocom.university.data.LessonPlan;
import ru.infocom.university.data.LessonProgress;
import ru.infocom.university.data.StudentGroup;
import ru.infocom.university.data.University;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class Utils {
    public static boolean checkContains(String text, String request) {
        String textLowerCase = text.toLowerCase();
        String requestLowerCase = request.toLowerCase();

        if (textLowerCase.contains(requestLowerCase)) {
            return true;
        }

        return getIndexCharContains(textLowerCase, requestLowerCase) != null;
    }

    public static SpannableString getSpannableStringMatches(String text, String request) {
        String textLowerCase = text.toLowerCase();
        String requestLowerCase = request.toLowerCase();

        SpannableString str = new SpannableString(text);
        if (textLowerCase.contains(requestLowerCase)) {
            int indexStart = textLowerCase.indexOf(requestLowerCase);
            str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), indexStart, indexStart + request.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return str;
        }

        int[] indexes = getIndexCharContains(textLowerCase, requestLowerCase);
        Log.i("TAG", "getSpannableStringMatches: " + Arrays.toString(indexes));
        for (int i : indexes) {
            str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), i, i + 1, 0);
        }

        return str;

    }

    public static SpannableStringBuilder coloredSomePartOfText(String coloredText, int color, String simpleText) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString
                .append(coloredText)
                .setSpan(new ForegroundColorSpan(color),
                        0,
                        coloredText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(simpleText)) {
            spannableString.append(simpleText);
        }

        return spannableString;
    }

    public static String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String shortFio(String input) {
        String str[] = input.split(" ");
        StringBuilder fio = new StringBuilder("");

        fio.append(str[0]);
        fio.append(" ");
        for(int i = 1; i < str.length; i++) {
            fio.append(str[1].charAt(0));
            fio.append(".");
        }

        return fio.toString();
    }

    public static String shortAudience(String input) {
        String audience = "АУДИТОРИЯ";
        int index = input.toUpperCase().indexOf(audience);
        if (index == 0) {
            return input.substring(index + audience.length());
        }
        return input;
    }

    public static int getSemesterFromString(String semester) {
        String[] semesters = {"первый", "второй", "третий", "четвертый", "пятый", "шестой", "седьмой",
                "восьмой", "девятый", "десятый", "одиннадцатый", "двенадцатый", "тринадцатый",
                "четырнадцатый", "пятнадцатый", "шестнадцатый", "семнадцатый", "восемнадцатый"};
        for (int i = 0; i < semesters.length; i++) {
            if ((semester.toLowerCase()).contains(semesters[i].toLowerCase())) {
                return i + 1;
            }
        }
        throw new IllegalArgumentException("Illegal argument: " + semester);
    }

    public static int differenceDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Dates can not be null");
        }

        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(date2);

        Log.d("SDViewPagerFragmentTab", "DST_OFFSET: " + cal1.get(Calendar.DST_OFFSET));

        long d1 = ((cal1.getTime().getTime()
                + cal1.get(Calendar.ZONE_OFFSET)) / 86400000L);
        long d2 = ((cal2.getTime().getTime()
                + cal2.get(Calendar.ZONE_OFFSET)) / 86400000L);
        long diff = d1 - d2;

        Log.d("SDViewPagerFragmentTab", "differenceDays: " + diff );

        return (int) diff ;
        //return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private static int[] getIndexCharContains(String text, String request) {

        boolean rBoolean = true;
        String splitString[] = text.split("[\\p{Punct}\\s]+");
        int[] indexes = new int[request.length()];

        int k = 0, check = 0;
        for (int i = 0; i < request.length(); i++) {

            for (int j = k; j < splitString.length; j++) {
                char ch = splitString[j].charAt(0);
                if (request.charAt(i) == ch) {
                    k = j + 1;
                    check++;

                    if (i > 0) {
                        indexes[i] = text.indexOf(splitString[j], indexes[i - 1]);
                    } else {
                        indexes[i] = text.indexOf(splitString[j]);
                    }

                    break;
                }
            }

        }

        if (check != request.length()) {
            rBoolean = false;
        }

        return rBoolean ? indexes : null;
    }

    public static boolean isToday(Date input) {
        Calendar todayCal = GregorianCalendar.getInstance();
        Calendar inputCal = GregorianCalendar.getInstance();
        inputCal.setTime(input);

        //********************** remove this before deploy *******
        todayCal.clear();
        todayCal.set(2013, 9, 7);
        //********************************************************

        return todayCal.get(Calendar.YEAR) == inputCal.get(Calendar.YEAR) &&
                todayCal.get(Calendar.MONTH) == inputCal.get(Calendar.MONTH) &&
                todayCal.get(Calendar.DAY_OF_MONTH) == inputCal.get(Calendar.DAY_OF_MONTH);
    }

    public static String getHeaderString(Map<String, List<String>> map) {
        StringBuilder result = new StringBuilder();

        for (String key : map.keySet()) {
            result.append(key);
            result.append(": ");
            boolean first = true;
            for (String value : map.get(key)) {
                if (first) {
                    result.append(value);
                    first = false;
                } else {
                    result.append(",");
                    result.append(value);
                }
            }
            result.append("\n");
        }

        return result.toString();
    }

    public static List<University> parseUniversityList(InputStream inputStream) throws XmlPullParserException, IOException {
        List<University> list = new ArrayList<>();
        XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
        xpp.setInput(inputStream, null);
        xpp.next();

        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("universities")) {
            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() != XmlPullParser.START_TAG || !xpp.getName().equals("university")) {
                    continue;
                }
                University university = new University();
                while (true) {
                    xpp.next();
                    if (xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals("university")) {
                        break;
                    }
                    if (xpp.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = xpp.getName();
                    if (name.equals("id")) {
                        university.setId(Integer.parseInt(readText(xpp)));
                    } else if (name.equals("name")) {
                        university.setName(readText(xpp));
                    } else if (name.equals("city")) {
                        university.setCity(readText(xpp));
                    }
                }
                list.add(university);
            }
        } else {
            throw new XmlPullParserException("Error XML format");
        }


        return list;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    public static class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider_05dp);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
