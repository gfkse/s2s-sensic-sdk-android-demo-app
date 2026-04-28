package com.gfk.s2s.demo.s2s

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler


open class VolumeContentObserver protected constructor(
    private val context: Context,
    handler: Handler?
) : ContentObserver(handler) {

    private var am = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    private var maxVolume: Int = am?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        volumeChanged(getScaledCurrentVolume())
    }

    /**
     *  currentVolume * 100 / maxVolume
     */
    fun getScaledCurrentVolume(): Int =
        (am?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0) * 100 / maxVolume

    open fun volumeChanged(currentVolume: Int) {}
}