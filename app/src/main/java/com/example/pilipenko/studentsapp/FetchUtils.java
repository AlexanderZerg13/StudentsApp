package com.example.pilipenko.studentsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.util.Pair;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public abstract class FetchUtils {

    public static byte[] doPostRequest(String login, String pass, String address, List<Pair<String, String>> pairs) throws IOException {
        URL url = new URL(address);

        String baseAuthStr = Base64.encodeToString((login + ":" + pass).getBytes(), Base64.DEFAULT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.addRequestProperty("Authorization", "Basic " + baseAuthStr);

        OutputStream os = conn.getOutputStream();

        BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        write.write(getQuery(pairs));
        write.flush();
        write.close();
        os.close();

        conn.connect();

        InputStream in = conn.getInputStream();

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        in.close();

        conn.disconnect();

        return result.toByteArray();
    }

    public static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

    private static String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair<String, String> pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second, "UTF-8"));
        }

        return result.toString();
    }

}
