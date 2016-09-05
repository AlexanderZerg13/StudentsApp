package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.example.pilipenko.studentsapp.data.AuthorizationObject;
import com.example.pilipenko.studentsapp.data.Lesson;
import com.example.pilipenko.studentsapp.data.StudentGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        if (str.length != 3) {
            throw new IllegalArgumentException("Must be three parts");
        }
        return str[0] + " " + str[1].charAt(0) + "." + str[2].charAt(0) + ".";
    }

    public static String shortAudience(String input) {
        String audience = "АУДИТОРИЯ";
        int index = input.toUpperCase().indexOf(audience);
        if (index == 0) {
            return input.substring(index + audience.length());
        }
        return input;
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

    public static AuthorizationObject parseResponseAuthorizationObject(InputStream inputStream) throws XmlPullParserException, IOException {
        AuthorizationObject object = new AuthorizationObject();

        XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
        xpp.setInput(inputStream, null);
        xpp.next();

        int event = xpp.getEventType();
        if (event == XmlPullParser.START_TAG) {
            String type = xpp.getName();
            switch (type) {
                case "error":
                    while (xpp.next() != XmlPullParser.END_TAG) {
                        if (xpp.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String name = xpp.getName();
                        if (name.equals("code")) {
                            object.setCode(Integer.parseInt(readText(xpp)));
                        } else if (name.equals("description")) {
                            object.setDescription(readText(xpp));
                        } else {
                            skip(xpp);
                        }
                    }
                    break;
                case "user":
                    while (xpp.next() != XmlPullParser.END_TAG) {
                        if (xpp.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String name = xpp.getName();
                        if (name.equals("id")) {
                            object.setId(readText(xpp));
                        } else if (name.equals("name")) {
                            object.setName(readText(xpp));
                        } else if (name.equals("password")) {
                            object.setPassword(readText(xpp));
                        } else {
                            skip(xpp);
                        }
                    }
                    break;
                default:
                    throw new XmlPullParserException("Error XML format ");
            }
        } else {
            throw new XmlPullParserException("Error XML format ");
        }

        return object;
    }

    public static List<StudentGroup> parseStudentsGroups(InputStream inputStream) throws XmlPullParserException, IOException {
        List<StudentGroup> list = new ArrayList<>();

        XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
        xpp.setInput(inputStream, null);
        xpp.next();

        int event = xpp.getEventType();
        if (event == XmlPullParser.START_TAG) {
            if (xpp.getName().equals("Группы")) {
                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = xpp.getName();
                    if (name.equals("Группа")) {
                        StudentGroup studentGroup = new StudentGroup();
                        while (xpp.next() != XmlPullParser.END_TAG) {
                            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }
                            name = xpp.getName();
                            if (name.equals("ИдентификаторГруппы")) {
                                studentGroup.setIdentifier(readText(xpp));
                            } else if (name.equals("НаименованиеГруппы")) {
                                studentGroup.setGroupName(readText(xpp));
                            } else if (name.equals("НаименованиеСпециальности")) {
                                studentGroup.setSpecialityName(readText(xpp));
                            } else if (name.equals("ФормаОбучения")) {
                                studentGroup.setTeachingForm(readText(xpp));
                            } else {
                                skip(xpp);
                            }
                        }
                        list.add(studentGroup);
                    }
                }
            } else {
                throw new XmlPullParserException("Error XML format");
            }
        } else {
            throw new XmlPullParserException("Error XML format");
        }

        return list;
    }

    public static List<Lesson> parseLessons(InputStream inputStream, String date) throws XmlPullParserException, IOException {
        List<Lesson> list = new ArrayList<>();
        XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
        xpp.setInput(inputStream, null);
        xpp.next();

        int event = xpp.getEventType();
        if (event == XmlPullParser.START_TAG) {
            if (xpp.getName().equals("Расписание")) {
                while (xpp.next() != XmlPullParser.START_TAG) {
                    if (xpp.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (xpp.getName().equals("Занятия")) {
                        break;
                    }
                }

                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (xpp.getName().equals("Занятие")) {
                        Lesson lesson = new Lesson(true);
                        while (true) {
                            xpp.next();
                            if (xpp.getEventType() == XmlPullParser.END_TAG) {
                                if (xpp.getName().equals("Занятие")) {
                                    break;
                                }
                            }
                            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }
                            String name = xpp.getName();
                            lesson.setDate(date);
                            if (name.equals("ВремяНачала")) {
                                lesson.setTimeStart(readText(xpp));
                            } else if (name.equals("ВремяОкончания")) {
                                lesson.setTimeEnd(readText(xpp));
                            } else if (name.equals("НаименованиеДисциплины")) {
                                lesson.setIsEmpty(false);
                                lesson.setName(readText(xpp));
                            } else if (name.equals("ТипЗанятия")) {
                                lesson.setType("ЛЕК");
                            } else if (name.equals("НаименованиеПреподавателя")) {
                                lesson.setTeacherName(shortFio(readText(xpp)));
                            } else if (name.equals("НаименованиеАудитории")) {
                                lesson.setAudience(shortAudience(readText(xpp)));
                            } else {
//                                skip(xpp);
                            }
                        }
                        list.add(lesson);
                    }
                }
            } else {
                throw new XmlPullParserException("Error XML format");
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
