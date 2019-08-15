package com.olgaz.testnytimesmostpopular.api;

import com.olgaz.testnytimesmostpopular.model.NewsResults;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("{articleType}/{period}.json")
    Observable<NewsResults> getResponseNews(@Path("articleType") String articleType, @Path("period") String period, @Query("api-key") String apiKey);
}

