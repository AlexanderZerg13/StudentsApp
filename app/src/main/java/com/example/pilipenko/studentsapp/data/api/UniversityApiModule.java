package com.example.pilipenko.studentsapp.data.api;

import android.app.Application;

import com.example.pilipenko.studentsapp.BuildConfig;
import com.example.pilipenko.studentsapp.manager.UserPreferenceManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by pilipenko on 20.03.2017.
 */

@Module
public class UniversityApiModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        //Add here headers for all requests
                        .build();
                return chain.proceed(request);
            }
        });

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRestAdapter(OkHttpClient okHttpClient, UserPreferenceManager userPreferenceManager) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(userPreferenceManager.getHostUniversity())
                .addConverterFactory(SimpleXmlConverterFactory.create());
        return builder.build();
    }

    @Provides
    @Singleton
    public UniversityApiService provideUniversityApiService(Retrofit retrofit) {
        return retrofit.create(UniversityApiService.class);
    }

    @Provides
    @Singleton
    public UserLoginManager provideUserLoginManager(UniversityApiService universityApiService) {
        return new UserLoginManager(universityApiService);
    }
}
