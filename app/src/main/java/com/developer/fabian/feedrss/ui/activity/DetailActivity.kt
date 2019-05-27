package com.developer.fabian.feedrss.ui.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

import com.developer.fabian.feedrss.R

class DetailActivity : AppCompatActivity() {

    companion object {
        const val URL_EXTRA = "url-extra"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayShowTitleEnabled(false)

        val urlExtra = intent.getStringExtra(URL_EXTRA)
        val webView = findViewById<WebView>(R.id.webview)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(urlExtra)
    }
}
