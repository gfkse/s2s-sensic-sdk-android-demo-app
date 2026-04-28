package com.gfk.s2s.demo.s2s.content

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.gfk.s2s.demo.s2s.DemoApplication
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.s2sagent.S2SAgent

class ContentActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var agent: S2SAgent

    private var mediaId = "s2s-exoplayer-android-demo"
    private var videoUrl = "http://sensic.net/"
    private val configUrl = DemoApplication.configURL

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        agent = S2SAgent(configUrl, mediaId, this)
        agent.impression("default", null)
        setContentView(R.layout.activity_content)
        webView = findViewById(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                /** Please use your country-specific custom params https://gfkconnect.gfk.com/portals/SENSIC/services/SENSIC/csc/Pages/Welcome-page.aspx */
                val customParams = mutableMapOf<String, String>()

                agent.impression("default", customParams)
            }
        }
        webView.settings.domStorageEnabled = true
        webView.loadUrl(videoUrl)
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        webView.saveState(outState)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        webView.restoreState(savedInstanceState)
//    }
}