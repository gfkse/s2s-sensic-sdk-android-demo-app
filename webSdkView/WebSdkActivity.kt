package com.gfk.s2s.demo.s2s.webSdkView

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.gfk.s2s.demo.s2s.R


class WebSdkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_sdk)
        val webview = findViewById<WebView>(R.id.webSdkView_webView)
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val uri = request!!.url
                return "development.sensic-demo.gfk.com" != uri.host
            }
        }
        webview.settings.apply {
            javaScriptEnabled = true // Required for app

            // Explicitly disable local file access risks
            allowFileAccess = false
            allowContentAccess = false
            domStorageEnabled = false
            allowFileAccessFromFileURLs = false
            allowUniversalAccessFromFileURLs = false
        }
        webview.loadUrl("https://development.sensic-demo.gfk.com/index.html")

        onBackPressedDispatcher.addCallback(this) {
            if (webview.canGoBack()) {
                webview.goBack()
            } else {
                finish()
            }
        }
    }
}