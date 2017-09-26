package ru.infocom.university.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alexander Pilipenko on 26.09.2017.
 */

public class ApiAuthorizationInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder().addHeader("Authorization", "Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6").build();

        return chain.proceed(request);
    }
}
