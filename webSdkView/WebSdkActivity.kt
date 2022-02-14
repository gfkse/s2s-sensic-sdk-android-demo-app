package com.gfk.s2s.demo.webSdkView

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.gfk.s2s.demo.s2s.R

class WebSdkActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_sdk)
        val webview = findViewById<WebView>(R.id.webSdkView_webView)
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.contains("https://development.sensic-demo.gfk.com")) {
                    view.loadUrl(url)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.allowFileAccess = true
        webview.settings.allowContentAccess = true
        webview.loadUrl("https://development.sensic-demo.gfk.com/index.html")
    }
}