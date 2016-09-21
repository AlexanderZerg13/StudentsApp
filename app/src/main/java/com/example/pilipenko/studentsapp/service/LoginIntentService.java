package com.example.pilipenko.studentsapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginIntentService extends IntentService {

    private static final String TAG = "LoginIntentService";
    
    public static final String BROADCAST_ACTION = "pilipenko.studentsapp.service.LoginIntentService.BROADCAST";

    public static final String LOGIN = "ws";
    public static final String PASS = "ws";
    private static final String ADDRESS_AUTH = "http://web-03:8080/InfoBase-Stud/hs/Authorization/Passwords";
    private static final String ADDRESS_GROUP = "http://web-03:8080/InfoBase-Stud/hs/Students/TimeTableGroups";
    private static final String ADDRESS_SPECIALTIES = "http://web-03:8080/InfoBase-Stud/hs/StudentsPlan/Specialties/Specialties";
    private static final String ADDRESS_PLAN = "http://web-03:8080/InfoBase-Stud/hs/StudentsPlan/Plans/Plans";

    public static final String KEY_EXTRA_NAME = "extra_name";
    public static final String KEY_EXTRA_PASSWORD = "extra_password";

    public static final String KEY_EXTRA_DATA = "extra_data";
    public static final String KEY_EXTRA_ERROR = "extra_error";


    
    public static Intent newIntent(Context context, String name, String password) {
        Intent intent = new Intent(context, LoginIntentService.class);
        intent.putExtra(KEY_EXTRA_NAME, name);
        intent.putExtra(KEY_EXTRA_PASSWORD, password);
        return intent;
    }

    public LoginIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        Intent resultIntent = performLogin(intent);

        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    private Intent performLogin(Intent intent) {
        boolean hasInternet = true;
        String name = intent.getStringExtra(KEY_EXTRA_NAME);
        String password = intent.getStringExtra(KEY_EXTRA_PASSWORD);

        Intent resultIntent = new Intent(BROADCAST_ACTION);
        AuthorizationObject authorizationObject = null;
        int idRes = R.string.fragment_login_tv_describe_error_access;

        if (!FetchUtils.isNetworkAvailableAndConnected(getApplicationContext())) {
            idRes = R.string.fragment_login_tv_describe_error_internet;
            hasInternet = false;
        }

        if (hasInternet) {
            try {
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(new Pair<>("login", name));
                params.add(new Pair<>("hash", new String(Hex.encodeHex(DigestUtils.sha1(password)))));

                byte[] bytes = FetchUtils.doPostRequest(LOGIN, PASS, ADDRESS_AUTH, params);

                authorizationObject = Utils.parseResponseAuthorizationObject(new ByteArrayInputStream(bytes));
                params.clear();
                if (authorizationObject.isSuccess()) {
                    params.add(new Pair<>("userId", authorizationObject.getId()));
                    bytes = FetchUtils.doPostRequest(LOGIN, PASS, ADDRESS_GROUP, params);
//----------------- может ли быть 0 групп????
                    List<StudentGroup> list = Utils.parseStudentsGroups(new ByteArrayInputStream(bytes));
                    long count = StudentGroupLab.get(getApplicationContext()).addStudentGroup(list);
                    if (count != 0) {
                        bytes = FetchUtils.doPostRequest(LOGIN, PASS, ADDRESS_SPECIALTIES, params);
                        String idSpecialty = Utils.parseSpecialities(new ByteArrayInputStream(bytes));

                        params.add(new Pair<>("specialty_id", idSpecialty));
                        bytes = FetchUtils.doPostRequest(LOGIN, PASS, ADDRESS_PLAN, params);
                        String idPlanes = Utils.parsePlanes(new ByteArrayInputStream(bytes));
                        authorizationObject.setPlan(idPlanes);

                    } else {
                        authorizationObject = null;
                    }
                }


                TimeUnit.SECONDS.sleep(2);

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                authorizationObject = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        resultIntent.putExtra(KEY_EXTRA_DATA, authorizationObject);
        resultIntent.putExtra(KEY_EXTRA_ERROR, idRes);

        return resultIntent;
    }
}
