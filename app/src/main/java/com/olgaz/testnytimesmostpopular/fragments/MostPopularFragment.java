package com.olgaz.testnytimesmostpopular.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olgaz.testnytimesmostpopular.DetailActivity;
import com.olgaz.testnytimesmostpopular.R;
import com.olgaz.testnytimesmostpopular.adapters.NewsAdapter;
import com.olgaz.testnytimesmostpopular.model.News;

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

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.loadData(tabArticle);
    }

    @Override
    public void showData(List<News> news) {
        adapter.setNewsNews(news);
    }

    @Override
    public void onDestroy() {
        presenter.disposeDisposable();
        super.onDestroy();
    }
}
