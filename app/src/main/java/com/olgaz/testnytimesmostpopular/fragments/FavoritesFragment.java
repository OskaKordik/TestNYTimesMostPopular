package com.olgaz.testnytimesmostpopular.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.olgaz.testnytimesmostpopular.DetailActivity;
import com.olgaz.testnytimesmostpopular.R;
import com.olgaz.testnytimesmostpopular.adapters.NewsAdapter;
import com.olgaz.testnytimesmostpopular.api.ApiConstants;
import com.olgaz.testnytimesmostpopular.model.News;

import java.util.ArrayList;
import java.util.List;
public class FavoritesFragment extends Fragment implements MostPopularView {
    private RecyclerView recyclerViewNews;
    private NewsAdapter adapter;
    private MostPopularPresenter presenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_most_popular, container, false);

        presenter = new MostPopularPresenter(this, getContext());

        recyclerViewNews = layout.findViewById(R.id.recyclerViewNewsMostPopular);
        adapter = new NewsAdapter();

        adapter.setNewsNews(new ArrayList<News>());
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        adapter.setOnNewsClickListener(new NewsAdapter.OnNewsClickListener() {
            @Override
            public void onNewsClick(int position) {
                Intent intent = new Intent(layout.getContext(), DetailActivity.class);
                News news = adapter.getNewsNews().get(position);
                String detailNewsUrl = news.getUrl();
                int isFavorites;
                if (presenter.isHasNewsInDB(news.getUrl())) isFavorites = 1;
                else isFavorites = 0;
                intent.putExtra("detailNewsUrl", detailNewsUrl);
                intent.putExtra("isFavorites", isFavorites);
                layout.getContext().startActivity(intent);
            }
        });
        recyclerViewNews.setAdapter(adapter);

        mSwipeRefreshLayout = layout.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        presenter.loadDataFromDB();
                    }
                }, 1000);
            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // load data from db
        presenter.loadDataFromDB();
    }

    @Override
    public void showData(List<News> news) {
        adapter.setNewsNews(news);
    }

    @Override
    public void showInfo(String info) {
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
        Log.i("MyInfo", info);
    }

    @Override
    public void onDestroy() {
        presenter.disposeDisposable();
        super.onDestroy();
    }
}
