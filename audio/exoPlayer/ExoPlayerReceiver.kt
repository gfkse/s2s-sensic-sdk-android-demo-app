package com.gfk.s2s.demo.audio.exoPlayer2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import java.lang.ref.WeakReference

class ExoPlayerReceiver : BroadcastReceiver() {

    companion object {
        private const val TEN_SECONDS = 10_000L

        private var player: ExoPlayer? = null
        private var contextRef: WeakReference<Context>? = null
        private var mediaSession: MediaSessionCompat? = null

        private val handler = Handler(Looper.getMainLooper())
        private val progressRunnable = object : Runnable {
            override fun run() {
                val ctx = contextRef?.get()
                if (player != null && mediaSession != null && ctx != null && player!!.playWhenReady) {
                    ExoPlayerNotificationManager.showNotification(ctx, player!!, mediaSession!!)
                    handler.postDelayed(this, 1000) // Update every second
                }
            }
        }

        fun setPlayer(exoPlayer: ExoPlayer?, context: Context) {
            player = exoPlayer
            contextRef = WeakReference(context.applicationContext)

            mediaSession = MediaSessionCompat(context, "ExoPlayerMediaSession").apply {
                isActive = true
                setCallback(object : MediaSessionCompat.Callback() {
                    override fun onPlay() {
                        player?.playWhenReady = true
                    }

                    override fun onPause() {
                        player?.playWhenReady = false
                    }

                    override fun onSkipToNext() {
                        player?.seekTo((player!!.currentPosition + TEN_SECONDS))
                    }

                    override fun onSkipToPrevious() {
                        player?.seekTo((player!!.currentPosition - TEN_SECONDS).coerceAtLeast(0))
                    }
                })
            }

            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    contextRef?.get()?.let { ctx ->
                        updatePlaybackState(isPlaying)
                        ExoPlayerNotificationManager.showNotification(ctx, player!!, mediaSession!!)
                        if (isPlaying) {
                            handler.post(progressRunnable)
                        } else {
                            handler.removeCallbacks(progressRunnable)
                        }
                    }
                }
            })

        }

        private fun updatePlaybackState(isPlaying: Boolean) {
            val state = if (isPlaying) {
                PlaybackStateCompat.STATE_PLAYING
            } else {
                PlaybackStateCompat.STATE_PAUSED
            }

            mediaSession?.setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                                PlaybackStateCompat.ACTION_PAUSE or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    )
                    .setState(state, player?.currentPosition ?: 0, 1f)
                    .build()
            )
        }

        fun releaseResources() {
            handler.removeCallbacks(progressRunnable)
            player = null
            contextRef = null
            mediaSession?.release()
            mediaSession = null
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || player == null || intent?.action == null) {
            Log.w("ExoPlayerReceiver", "Invalid context, player, or intent")
            return
        }
    }
}
