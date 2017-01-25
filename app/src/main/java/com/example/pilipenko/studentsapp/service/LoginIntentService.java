package com.example.pilipenko.studentsapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.pilipenko.studentsapp.FetchUtils;
import com.example.pilipenko.studentsapp.LoginAuthFragment;
import com.example.pilipenko.studentsapp.R;
import com.example.pilipenko.studentsapp.Utils;
import com.example.pilipenko.studentsapp.data.AuthorizationObject;
import com.example.pilipenko.studentsapp.data.StudentGroup;
import com.example.pilipenko.studentsapp.data.StudentGroupLab;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginIntentService extends IntentService {

    private static final String TAG = "LoginIntentService";
    
    public static final String BROADCAST_ACTION = "pilipenko.studentsapp.service.LoginIntentService.BROADCAST";

    public static final String LOGIN = "ws";
    public static final String PASS = "ws";
    private static final String ADDRESS_AUTH = "Authorization/Passwords";
    private static final String ADDRESS_GROUP = "Students/TimeTableGroups";
    private static final String ADDRESS_SPECIALTIES = "StudentsPlan/Specialties/Specialties";
    private static final String ADDRESS_PLAN = "StudentsPlan/Plans/Plans";

    public static final String KEY_EXTRA_NAME = "extra_name";
    public static final String KEY_EXTRA_PASSWORD = "extra_password";
    public static final String KEY_EXTRA_UNIVERSITY_ID = "university_id";

    public static final String KEY_EXTRA_DATA = "extra_data";
    public static final String KEY_EXTRA_ERROR = "extra_error";


    
    public static Intent newIntent(Context context, String name, String password, int id) {
        Intent intent = new Intent(context, LoginIntentService.class);
        intent.putExtra(KEY_EXTRA_NAME, name);
        intent.putExtra(KEY_EXTRA_PASSWORD, password);
        intent.putExtra(KEY_EXTRA_UNIVERSITY_ID, id);
        return intent;
    }

    public LoginIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        Intent resultIntent = performLogin(intent);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    private Intent performLogin(Intent intent) {
        boolean hasInternet = true;

        String host = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.settings_key_host_university), getString(R.string.settings_default_host_university));


        String name = intent.getStringExtra(KEY_EXTRA_NAME);
        String password = intent.getStringExtra(KEY_EXTRA_PASSWORD);
        int id = intent.getIntExtra(KEY_EXTRA_UNIVERSITY_ID, 0); //Проверить

        host = host + "/" + id;
        Log.i(TAG, "performLogin: " + host);

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(getString(R.string.settings_key_host), host).commit();
        host = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.settings_key_host), "n");

        Intent resultIntent = new Intent(BROADCAST_ACTION);
        AuthorizationObject authorizationObject = null;
        int idRes = R.string.fragment_login_tv_describe_error_access;

        if (!FetchUtils.isNetworkAvailableAndConnected(getApplicationContext())) {
            idRes = R.string.fragment_login_tv_describe_error_internet;
            hasInternet = false;
        }

        //test only!!!!!
        //host = "http://web-03:8080/InfoBase_1210_02/hs";

        if (hasInternet) {
            try {
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("login", name));
                params.add(new Pair<>("hash", new String(Hex.encodeHex(DigestUtils.sha1(password)))));

                System.out.println(Uri.parse(host).getPort());

                byte[] bytes = FetchUtils.doPostRequest(LOGIN, PASS, Uri.withAppendedPath(Uri.parse(host), ADDRESS_AUTH).toString(), params);
                System.out.println(new String(bytes));


                authorizationObject = Utils.parseResponseAuthorizationObject(new ByteArrayInputStream(bytes));
                params.clear();
                System.out.println(authorizationObject);
                if (authorizationObject.isSuccess()) {

                    switch (authorizationObject.getRole()) {
                        case STUDENT:
                            performLoginForStudent(host, params, authorizationObject);
                            break;
                        case TEACHER:
                            performLoginForTeacher(host, params, authorizationObject);
                            break;
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                authorizationObject = null;
            }
        }

        resultIntent.putExtra(KEY_EXTRA_DATA, authorizationObject);
        resultIntent.putExtra(KEY_EXTRA_ERROR, idRes);

        return resultIntent;
    }

    private void performLoginForStudent(String host, List<Pair<String, String>> params, AuthorizationObject authorizationObject) throws IOException, XmlPullParserException {
        params.add(new Pair<>("userId", authorizationObject.getId()));

        byte[] bytes = FetchUtils.doPostRequest(LOGIN, PASS, Uri.withAppendedPath(Uri.parse(host), ADDRESS_GROUP).toString(), params);
        //---- может ли быть 0 групп????
        System.out.println(new String(bytes));
        List<StudentGroup> list = Utils.parseStudentsGroups(new ByteArrayInputStream(bytes));
        long count = StudentGroupLab.get(getApplicationContext()).addStudentGroup(list);
        if (count != 0) {
            params.clear();
            params.add(new Pair<>("user_id", authorizationObject.getId()));
            System.out.println(params);
            bytes = FetchUtils.doPostRequest(LOGIN, PASS, Uri.withAppendedPath(Uri.parse(host), ADDRESS_SPECIALTIES).toString(), params);
            System.out.println(new String(bytes));
            String idSpecialty = Utils.parseSpecialities(new ByteArrayInputStream(bytes));

            params.add(new Pair<>("specialty_id", idSpecialty));
            bytes = FetchUtils.doPostRequest(LOGIN, PASS, Uri.withAppendedPath(Uri.parse(host), ADDRESS_PLAN).toString(), params);
            System.out.println(new String(bytes));
            String idPlanes = Utils.parsePlanes(new ByteArrayInputStream(bytes));
            authorizationObject.setPlan(idPlanes);
        } else {
            authorizationObject = null;
        }
    }

    private void performLoginForTeacher(String host, List<Pair<String, String>> params, AuthorizationObject authorizationObject) throws IOException, XmlPullParserException {
        //that is all
    }
}
