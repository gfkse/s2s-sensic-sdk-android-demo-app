package com.gfk.s2s.demo

import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.multidex.MultiDex
import com.gfk.s2s.demo.s2s.R

class MainActivity : AppCompatActivity() {
    var usePictureInPictureByHomeButtonPress = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MultiDex.install(this)
        val actionBar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(actionBar)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (usePictureInPictureByHomeButtonPress) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true) {
                enterPictureInPictureMode()
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true) {
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
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            supportActionBar?.hide()
        } else {
            supportActionBar?.show()
        }
    }
}