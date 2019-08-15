package com.olgaz.testnytimesmostpopular.api;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient apiClient;
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.nytimes.com/svc/mostpopular/v2/";

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static ApiClient getInstance() {
        if (apiClient == null) apiClient = new ApiClient();
        return apiClient;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
