package com.example.pilipenko.studentsapp;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

public abstract class Utils {
    public static boolean checkContains (String text, String request) {
        String textLowerCase = text.toLowerCase();
        String requestLowerCase = request.toLowerCase();

        if (textLowerCase.contains(requestLowerCase)) {
            return true;
        }

        boolean rBoolean = true;
        String splitString[] = textLowerCase.split("[\\p{Punct}\\s]+");

        int k = 0, check = 0;
        for (int i = 0; i < requestLowerCase.length(); i++) {

            for(int j = k; j < splitString.length; j++) {
                char ch = splitString[j].charAt(0);
                if (requestLowerCase.charAt(i) == ch) {
                    k = j + 1;
                    check++;
                    break;
                }
            }

        }
        if (check != requestLowerCase.length()) {
            rBoolean = false;
        }
        return rBoolean;
    }

    public static SpannableString getSpannableStringMatches (String text, String request) {
        StyleSpan styleSpanBold = new android.text.style.StyleSpan(Typeface.BOLD);

        String textLowerCase = text.toLowerCase();
        String requestLowerCase = request.toLowerCase();

        SpannableString str = new SpannableString(text);
        if (textLowerCase.contains(requestLowerCase)) {
            int indexStart = textLowerCase.indexOf(requestLowerCase);
            str.setSpan(styleSpanBold, indexStart, indexStart + request.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return str;
        }

        return str;

    }

}
