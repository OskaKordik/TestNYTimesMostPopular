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

import com.olgaz.testnytimesmostpopular.fragments.MostPopularPresenter;
import com.olgaz.testnytimesmostpopular.fragments.MostPopularView;
import com.olgaz.testnytimesmostpopular.model.Media;
import com.olgaz.testnytimesmostpopular.model.News;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements MostPopularView {
    private String urlDetail;
    private WebView webViewDetail;
    private MostPopularPresenter presenter;
    private News news;
    private boolean isFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        presenter = new MostPopularPresenter(this, getApplicationContext());

        Bundle bundle = getIntent().getBundleExtra("newsObject");
        news = new News((long) 1, bundle.getString("detailUrl"), bundle.getString("section"),
                bundle.getString("title"), "test", "test", "test", new ArrayList<Media>());

        /*
        isFavorites = presenter.isHasNewsInDB(news.getId());
         */

        webViewDetail = findViewById(R.id.webViewDetail);
        urlDetail = news.getUrl();

        actionLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_favorites:
                actionAddToFavorites(item);
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

    private void actionAddToFavorites(MenuItem item) {
        if (isFavorites) {
            isFavorites = false;
            item.setIcon(R.drawable.ic_favorite_border_white_24dp);
            Log.i("MyInfo", "Удалено из избранного: " + news.getTitle());
        } else {
            isFavorites = true;
            item.setIcon(R.drawable.ic_favorite_white_24dp);
            Log.i("MyInfo", "Добавлено: " + news.getTitle());
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
