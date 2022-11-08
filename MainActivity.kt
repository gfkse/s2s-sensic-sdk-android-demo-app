package com.gfk.s2s.demo

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.http.HttpResponseCache
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.multidex.MultiDex
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.utils.Logger
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    var usePictureInPictureByHomeButtonPress = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val httpCacheDir: File = File(cacheDir, "http")
            val httpCacheSize = (10 * 1024 * 1024).toLong() // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize)
            Logger.logD("HTTP response cache installed")
        } catch (e: IOException) {
            Logger.logD("HTTP response cache installation failed:$e")
        }
        setContentView(R.layout.activity_main)
        MultiDex.install(this)
        val actionBar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(actionBar)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (usePictureInPictureByHomeButtonPress) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N
                && packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true) {
                enterPictureInPictureMode()
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
                && packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true) {
                enterPictureInPictureMode(PictureInPictureParams.Builder().build())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation != Configuration.ORIENTATION_PORTRAIT) {
            supportActionBar?.hide()
        } else {
            supportActionBar?.show()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            supportActionBar?.hide()
        } else {
            supportActionBar?.show()
        }
    }
}