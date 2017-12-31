package com.developer.fabian.feedrss.ui.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.developer.fabian.feedrss.R;

public class DetailActivity extends AppCompatActivity {

    public static final String URL_EXTRA = "url-extra";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        String urlExtra = getIntent().getStringExtra(URL_EXTRA);
        WebView webview = findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(urlExtra);
    }
}
