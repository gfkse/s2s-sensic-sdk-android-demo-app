package com.gfk.s2s.demo.audio.exoPlayer2

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import android.support.v4.media.session.MediaSessionCompat
import com.gfk.s2s.demo.s2s.R
import com.google.android.exoplayer2.ExoPlayer

object ExoPlayerNotificationManager {
    private const val CHANNEL_ID = "player_channel"
    private const val CHANNEL_NAME = "Player Notifications"
    private const val NOTIFICATION_ID = 112

    fun showNotification(context: Context, player: ExoPlayer, @SuppressLint("RestrictedApi") mediaSession: MediaSessionCompat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // Since ExoPlayer 2.17.1 doesn't have player.mediaMetadata, use defaults
        val contentTitle = context.getString(R.string.audio_stream)
        val contentText = context.getString(R.string.app_name)
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.media3_icon_album)


        val position = player.currentPosition

        // Build Notification
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.media3_icon_album)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2) // Show play/pause, next, and previous
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true) // Avoid sound/vibration for progress updates

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun cancelNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }
}
