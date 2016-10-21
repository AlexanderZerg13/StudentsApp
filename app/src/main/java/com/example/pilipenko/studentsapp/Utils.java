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
import com.example.pilipenko.studentsapp.data.LessonPlan;
import com.example.pilipenko.studentsapp.data.LessonProgress;
import com.example.pilipenko.studentsapp.data.StudentGroup;
import com.example.pilipenko.studentsapp.data.University;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String shortTime(String time) {
        String timeSp[] = time.split(":");
        if (timeSp[0].startsWith("0")) {
            timeSp[0] = timeSp[0].substring(1);
        }
        return timeSp[0] + ":" + timeSp[1];
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
        String[] semesters = {"первый", "второй", "третий", "четвертый", "пятый", "шестой", "седьмой", "восьмой", "девятый", "десятый", "одиннадцатый", "двенадцатый"};
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
        long diff = date1.getTime() - date2.getTime();

        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
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
        if (event == XmlPullParser.START_TAG && xpp.getName().equals("Расписание")) {
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
                            lesson.setTimeStart(shortTime(readText(xpp)));
                        } else if (name.equals("ВремяОкончания")) {
                            lesson.setTimeEnd(shortTime(readText(xpp)));
                        } else if (name.equals("НаименованиеДисциплины")) {
                            lesson.setIsEmpty(false);
                            lesson.setName(readText(xpp));
                        } else if (name.equals("ТипЗанятия")) {
                            lesson.setType("ЛЕК");
                        } else if (name.equals("НаименованиеПреподавателя")) {
                            lesson.addTeacher(readText(xpp));
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
        if (list.size() == 0) {
            Lesson lesson = new Lesson(true);
            lesson.setDate(date);
            list.add(lesson);
        }
        return list;
    }

    public static List<LessonProgress> parseLessonsProgress(InputStream inputStream) throws XmlPullParserException, IOException, ParseException {
        SimpleDateFormat fromSDF = new SimpleDateFormat("dd.MM.yyyy h:mm:ss", new Locale("ru"));
        SimpleDateFormat toSDF = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru"));


        List<LessonProgress> list = new ArrayList<>();
        XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
        xpp.setInput(inputStream, null);
        xpp.next();

        int event = xpp.getEventType();
        if (event == XmlPullParser.START_TAG && xpp.getName().equals("response")) {
            xpp.nextTag();
            if (xpp.getName().equals("recordset")) {
                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (xpp.getName().equals("record")) {
                        LessonProgress lessonProgress = new LessonProgress();
                        while (true) {
                            xpp.next();
                            if (xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals("record")) {
                                break;
                            }
                            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }
                            String name = xpp.getName();
                            if (name.equals("ДатаОценки")) {
                                lessonProgress.setDate(toSDF.format(fromSDF.parse(readText(xpp))));
                            } else if (name.equals("Дисциплина")) {
                                lessonProgress.setLessonName(readText(xpp));
                            } else if (name.equals("ПериодКонтроля")) {
                                lessonProgress.setSemester(readText(xpp));
                            } else if (name.equals("Отметка")) {
                                lessonProgress.setMark(LessonProgress.Mark.fromString(readText(xpp)));
                            }
                        }
                        list.add(lessonProgress);
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

    public static List<LessonPlan> parseLessonsPlan(InputStream inputStream) throws XmlPullParserException, IOException, ParseException {
        List<LessonPlan> list = new ArrayList<>();
        XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
        xpp.setInput(inputStream, null);
        xpp.next();

        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("data")) {
            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() != XmlPullParser.START_TAG || !xpp.getName().equals("discipline")) {
                    continue;
                }
                LessonPlan plan = new LessonPlan();
                String load = null;
                while (xpp.next() != XmlPullParser.END_TAG) {
                    if (xpp.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = xpp.getName();
                    if (name.equals("name")) {
                        plan.setName(readText(xpp));
                    } else if (name.equals("period")) {
                        plan.setSemester(getSemesterFromString(readText(xpp)));
                    } else if (name.equals("load")) {
                        load = readText(xpp).toLowerCase();
                    } else if (name.equals("amount")) {
                        if (load == null) {
                            continue;
                        }
                        int amount = NumberFormat.getNumberInstance(Locale.FRANCE).parse(readText(xpp)).intValue();
                        switch (load) {
                            case "лабораторные":
                                plan.setLaboratoryHours(amount);
                                break;
                            case "лекции":
                                plan.setLectureHours(amount);
                                break;
                            case "самостоятельная работа":
                                plan.setSelfWorkHours(amount);
                                break;
                            case "практические":
                                plan.setPracticeHours(amount);
                                break;
                            case "экзамен":
                                plan.setExam(true);
                                break;
                            case "зачет":
                                plan.setSet(true);
                                break;
                            case "курсовая работа":
                                plan.setCourse(true);
                                break;
                        }
                    }

                }
                Log.i("TAG", "parseLessonsPlan: " + plan);
                if (list.size() == 0) {
                    list.add(plan);
                } else {
                    boolean found = false;
                    for (LessonPlan lessonPlan : list) {
                        if (lessonPlan.getName().equals(plan.getName())
                                && lessonPlan.getSemester() == plan.getSemester()) {
                            found = true;
                            lessonPlan.mergeLessonPlan(plan);
                            break;
                        }
                    }
                    if (!found) {
                        list.add(plan);
                    }

                }

            }

        } else {
            throw new XmlPullParserException("Error XML format");
        }


        return list;
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
                    }
                }
                list.add(university);
            }
        } else {
            throw new XmlPullParserException("Error XML format");
        }


        return list;
    }

    public static String parsePlanes(InputStream inputStream) throws IOException, XmlPullParserException {
        return parseSpecialities(inputStream);
    }

    public static String parseSpecialities(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
        xpp.setInput(inputStream, null);
        while (xpp.next() != XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (xpp.getName().equals("id")) {
                return readText(xpp);
            }
        }
        throw new XmlPullParserException("Can not be found");
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
