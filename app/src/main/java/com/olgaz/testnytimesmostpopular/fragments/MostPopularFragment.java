package com.olgaz.testnytimesmostpopular.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.olgaz.testnytimesmostpopular.model.News;

import java.util.ArrayList;
import java.util.List;

public class MostPopularFragment extends Fragment implements MostPopularView, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerViewNews;
    private NewsAdapter adapter;
    private MostPopularPresenter presenter;
    private String tabArticle;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // newInstance constructor for creating fragment with arguments
    public static MostPopularFragment newInstance(String tabArticle) {
        MostPopularFragment mostPopularFragment = new MostPopularFragment();
        Bundle args = new Bundle();
        args.putString("tabArticle", tabArticle);
        mostPopularFragment.setArguments(args);
        return mostPopularFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        tabArticle = getArguments().getString("tabArticle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_most_popular, container, false);

        presenter = new MostPopularPresenter(this);

        recyclerViewNews = layout.findViewById(R.id.recyclerViewNewsMostPopular);
        adapter = new NewsAdapter();

        adapter.setNewsNews(new ArrayList<News>());
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        adapter.setOnNewsClickListener(new NewsAdapter.OnNewsClickListener() {
            @Override
            public void onNewsClick(int position) {
                Intent intent = new Intent(layout.getContext(), DetailActivity.class);
                String detailNewsUrl = adapter.getNewsNews().get(position).getUrl();
                intent.putExtra("detailUrl", detailNewsUrl);
                layout.getContext().startActivity(intent);
            }
        });
        recyclerViewNews.setAdapter(adapter);

        mSwipeRefreshLayout = layout.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // load data from network
        presenter.loadData(tabArticle);
    }

    @Override
    public void showData(List<News> news) {
        adapter.setNewsNews(news);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), "Network unavailable", Toast.LENGTH_SHORT).show();
        Log.i("MyInfo", error);
    }

    @Override
    public void onDestroy() {
        presenter.disposeDisposable();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                presenter.loadData(tabArticle);
            }
        }, 1000);
    }
}
