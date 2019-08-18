package com.olgaz.testnytimesmostpopular;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    private String urlDetail;
    private WebView webViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webViewDetail = findViewById(R.id.webViewDetail);
        urlDetail = getIntent().getStringExtra("detailUrl");

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
                item.setIcon(R.drawable.ic_favorite_white_24dp);
                Toast.makeText(this, "добавлено в избранное", Toast.LENGTH_SHORT).show();
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
}
