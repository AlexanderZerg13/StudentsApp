package com.example.pilipenko.studentsapp.data.api;

import com.example.pilipenko.studentsapp.data.event.UniversitiesLoadEvent;
import com.example.pilipenko.studentsapp.data.respones.UniversitiesRespones;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by pilipenko on 21.03.2017.
 */

public class UniversityListManager {

    private UniversityApiService mUniversityApiService;

    public UniversityListManager(UniversityApiService universityApiService) {
        mUniversityApiService = universityApiService;
    }

    public void doFetchUniversities() {
        mUniversityApiService.getUniversities().enqueue(new Callback<UniversitiesRespones>() {
            @Override
            public void onResponse(Call<UniversitiesRespones> call, Response<UniversitiesRespones> response) {
                Timber.d("onResponse");
                EventBus.getDefault().postSticky(new UniversitiesLoadEvent(response.body().mUniversities));
            }

            @Override
            public void onFailure(Call<UniversitiesRespones> call, Throwable t) {
                Timber.d("onFailure");
            }
        });
    }
}
