package ru.infocom.university.network;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class ApiFactory {
    private static OkHttpClient sClient;

    private static StudyService sService;

    @NonNull
    public static StudyService getStudyService() {
        StudyService service = sService;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = sService;
                if (service == null) {
                    service = sService = createService();
                }
            }
        }
        return service;
    }

    @NonNull
    private static StudyService createService() {
        return new Retrofit.Builder()
                .baseUrl("http://81.177.140.25")
                .client(getClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(StudyService.class);
    }

    @NonNull
    private static OkHttpClient getClient() {
        OkHttpClient client = sClient;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = sClient;
                if (client == null) {
                    client = sClient = buildClient();
                }
            }
        }
        return client;
    }

    @NonNull
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new ApiAuthorizationInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }
}
