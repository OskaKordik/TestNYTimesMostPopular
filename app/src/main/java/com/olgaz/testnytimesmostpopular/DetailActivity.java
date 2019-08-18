package com.olgaz.testnytimesmostpopular;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.olgaz.testnytimesmostpopular.db.DBNewsContract;
import com.olgaz.testnytimesmostpopular.fragments.MostPopularPresenter;
import com.olgaz.testnytimesmostpopular.fragments.MostPopularView;
import com.olgaz.testnytimesmostpopular.model.Media;
import com.olgaz.testnytimesmostpopular.model.MediaMetadata;
import com.olgaz.testnytimesmostpopular.model.News;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements MostPopularView {
    private String urlDetail;
    private WebView webViewDetail;
    private MostPopularPresenter presenter;
    private News news;
    private boolean isFavorites;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        presenter = new MostPopularPresenter(this, getApplicationContext());

        Bundle bundle = getIntent().getBundleExtra("newsObject");
        String url = bundle.getString("detailUrl");
        String section = bundle.getString("section");
        String title = bundle.getString("title");
        String description = bundle.getString("description");
        String publishedDate = bundle.getString("publishedDate");
        String source = bundle.getString("source");
        String mediaUrl = bundle.getString("mediaUrl");

        MediaMetadata mediaMetadata = new MediaMetadata();
        mediaMetadata.setUrl(mediaUrl);

        ArrayList<MediaMetadata> mediaMetadataList = new ArrayList<>();
        mediaMetadataList.add(new MediaMetadata());
        mediaMetadataList.add(mediaMetadata);

        Media media = new Media();
        media.setMediaMetadata(mediaMetadataList);

        List<Media> mediaList = new ArrayList<>();
        mediaList.add(media);

        news = new News(url, section, title,description, publishedDate, source, mediaList);

        isFavorites = presenter.isHasNewsInDB(news.getUrl());

        webViewDetail = findViewById(R.id.webViewDetail);
        urlDetail = news.getUrl();

        actionLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        item = menu.getItem(0);
        if (isFavorites) item.setIcon(R.drawable.ic_favorite_white_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_favorites:
                actionAddToFavorites();
                return true;
            case R.id.action_share:
                actionShare();
                return true;
            case R.id.action_refresh:
                actionLoad();
                return true;
            case R.id.open_favorites:
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionAddToFavorites() {
        if (isFavorites) {
            isFavorites = false;
            item.setIcon(R.drawable.ic_favorite_border_white_24dp);
        } else {
            isFavorites = true;
            item.setIcon(R.drawable.ic_favorite_white_24dp);
            presenter.insertNewsToDB(news);
        }
    }

    private void actionLoad() {
        //кэширования для загружаемого контента
        webViewDetail.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webViewDetail.loadUrl(urlDetail);
    }

    private void actionShare(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, urlDetail);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void showData(List<News> news) {

    }

    @Override
    public void showInfo(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        presenter.closeDB();
        super.onDestroy();
    }
}
