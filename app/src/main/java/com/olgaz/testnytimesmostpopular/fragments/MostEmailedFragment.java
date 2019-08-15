package com.olgaz.testnytimesmostpopular.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olgaz.testnytimesmostpopular.R;
import com.olgaz.testnytimesmostpopular.adapters.NewsAdapter;
import com.olgaz.testnytimesmostpopular.api.ApiClient;
import com.olgaz.testnytimesmostpopular.api.ApiService;
import com.olgaz.testnytimesmostpopular.pojo.News;
import com.olgaz.testnytimesmostpopular.pojo.Results;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MostEmailedFragment extends Fragment {
    private RecyclerView recyclerViewNews;
    private NewsAdapter adapter;
    private static final String API_KEY = "7wSLHtDihxnGsyVtzvQJjAzDGARhsM0V";
    private static final String PERIOD = "30";
    private static final String EMAILED = "emailed";
    private static final String SHARED = "shared";
    private static final String VIEWED = "viewed";
    private Disposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //возврат к исходному состоянию
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_most_emailed, container, false);

        recyclerViewNews = layout.findViewById(R.id.recyclerViewNews2);
        adapter = new NewsAdapter();
        adapter.setContext(layout.getContext());
        adapter.setResultsNews(new ArrayList<Results>());
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        recyclerViewNews.setAdapter(adapter);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ApiClient apiClient = ApiClient.getInstance();
        ApiService apiService = apiClient.getApiService();
        disposable = apiService.getResponseNews(EMAILED, PERIOD, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<News>() {
                    @Override
                    public void accept(News news) throws Exception {
                        adapter.setResultsNews(news.getResults());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MyInfo", throwable.getMessage());
                    }
                });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //сохранение исходного состояния
    }

    @Override
    public void onDestroy() {
        if (disposable != null) disposable.dispose();
        super.onDestroy();
    }

}
