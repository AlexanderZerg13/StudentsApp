package com.example.pilipenko.studentsapp.data.api;

import com.example.pilipenko.studentsapp.manager.UserPreferenceManager;

/**
 * Created by pilipenko on 20.03.2017.
 */

public class UserLoginManager {

    private UniversityApiService mUniversityApiService;

    public UserLoginManager(UniversityApiService universityApiService) {
        mUniversityApiService = universityApiService;
    }

    public void doLogin(String login, String password) {
        
    }

}
