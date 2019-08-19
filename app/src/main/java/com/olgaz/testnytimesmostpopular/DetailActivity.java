package com.olgaz.testnytimesmostpopular;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private boolean isFavorites;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState != null) isFavorites = savedInstanceState.getBoolean("isFavorites");
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isFavorites", isFavorites);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        presenter = new MostPopularPresenter(this, getApplicationContext());

        urlDetail = getIntent().getStringExtra("detailNewsUrl");
        int isFavoritesInt = getIntent().getIntExtra("isFavorites", 0);
        if (isFavoritesInt == 1) isFavorites = true;
        else isFavorites = false;

        webViewDetail = findViewById(R.id.webViewDetail);
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
                actionFavorites();
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

    private void actionFavorites() {
        if (isFavorites) {
            actionRemoveFromFavorites();
        } else {
            isFavorites = true;
            item.setIcon(R.drawable.ic_favorite_white_24dp);
            presenter.insertToFavorites(urlDetail);
        }
    }

    private void actionRemoveFromFavorites() {
        isFavorites = false;
        item.setIcon(R.drawable.ic_favorite_border_white_24dp);
        presenter.deleteNewsFromFavorites(urlDetail);
    }

    private void actionLoad() {
        //кэширования для загружаемого контента
        webViewDetail.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webViewDetail.loadUrl(urlDetail);
    }

    private void actionShare() {
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
        Snackbar snackbar = Snackbar.make(findViewById(R.id.webViewDetail), info, Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionFavorites();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        presenter.closeDB();
        super.onDestroy();
    }
}
