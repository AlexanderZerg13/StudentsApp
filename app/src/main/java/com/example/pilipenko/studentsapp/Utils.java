package com.example.pilipenko.studentsapp;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;

import java.util.Arrays;

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
}
