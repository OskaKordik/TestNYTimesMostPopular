package com.olgaz.testnytimesmostpopular.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

@SuppressLint("ValidFragment")
public class MostPopularFragment extends Fragment implements MostPopularView {
    private RecyclerView recyclerViewNews;
    private NewsAdapter adapter;
    private MostPopularPresenter presenter;
    private String tabArticle;

    public MostPopularFragment(String tabArticle) {
        this.tabArticle = tabArticle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_most_popular, container, false);

        recyclerViewNews = layout.findViewById(R.id.recyclerViewNewsMostPopular);
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
        presenter.loadData(tabArticle);
    }

    @Override
    public void showData(List<Results> news) {
        adapter.setResultsNews(news);
    }

    @Override
    public void onDestroy() {
        presenter.disposeDisposable();
        super.onDestroy();
    }
}
