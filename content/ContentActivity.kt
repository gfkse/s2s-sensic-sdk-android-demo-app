package com.gfk.s2s.demo.content

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.s2sagent.S2SAgent

class ContentActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var agent: S2SAgent

    private var mediaId = "s2s-exoplayer-android-demo"
    private var videoUrl = "https://www.sensic.net"
    private val configUrl = "https://demo-config.sensic.net/s2s-android.json"

    @SuppressLint("SetJavaScriptEnabled")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        agent = S2SAgent(configUrl, mediaId, this)
        setContentView(R.layout.activity_content)
        webView = findViewById(R.id.webView)
        webView.webViewClient = MyWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(videoUrl)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            /** Please use your country-specific custom params https://confluence-docu.gfk.com/display/SENSIC/Client+specific+customizations  */
            val customParams = mutableMapOf<String, String>()

            ///Example custom params for Singapore
            //customParams.put("subscriber", "1");


            ///Example custom params for Spain
            //customParams.put("cp1", "appsBundleID");

            agent.impression(url, customParams)
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
        }
    }
}