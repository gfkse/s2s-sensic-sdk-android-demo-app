package com.gfk.s2s.demo.audio.exoPlayer3

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import android.support.v4.media.session.MediaSessionCompat
import com.gfk.s2s.demo.s2s.R

@OptIn(UnstableApi::class)
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

        // Metadata
        val metadata = player.mediaMetadata
        val contentTitle = metadata.title ?: context.getString(R.string.audio_stream)
        val contentText = metadata.artist ?: context.getString(R.string.app_name)
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.media3_icon_album)

        val duration = player.duration
        val position = player.currentPosition
        // Actions


        // Build Notification
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.media3_icon_album)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0,1,2) // Show play/pause, next, and previous in compact view
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true) // Avoid sound/vibration for progress updates

        // Post Notification

        if (duration > 0) {
            notificationBuilder.setProgress(duration.toInt(), position.toInt(), false)
        } else {
            notificationBuilder.setProgress(0, 0, true) // Indeterminate if unknown
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun cancelNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }
}