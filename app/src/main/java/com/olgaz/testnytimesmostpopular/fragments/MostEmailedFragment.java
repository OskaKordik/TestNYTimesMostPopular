package com.olgaz.testnytimesmostpopular.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olgaz.testnytimesmostpopular.R;
import com.olgaz.testnytimesmostpopular.adapters.NewsAdapter;
import com.olgaz.testnytimesmostpopular.pojo.Results;

import java.util.ArrayList;
import java.util.List;

public class MostEmailedFragment extends Fragment implements MostPopularView {
    private RecyclerView recyclerViewNews;
    private NewsAdapter adapter;
    private static final String EMAILED = "emailed";
    private static final String SHARED = "shared";
    private static final String VIEWED = "viewed";
    private MostPopularPresenter presenter;

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
        presenter = new MostPopularPresenter(this);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.loadData(EMAILED);
    }

    @Override
    public void showData(List<Results> news) {
        adapter.setResultsNews(news);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //сохранение исходного состояния
    }

    @Override
    public void onDestroy() {
        presenter.disposeDisposable();
        super.onDestroy();
    }

}
