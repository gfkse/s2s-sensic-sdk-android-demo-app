package com.gfk.s2s.demo.audio.exoPlayer3

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.gfk.s2s.demo.BaseFragment
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.exoplayer.Exoplayer3Extension

/**
 * class BaseAudioFragment has the code to show the exoplayer.
 *  The values for audioUrl, configUrl and mediaId are overridden in the
 *  fragments extending from this class.
 */

open class BaseAudioFragment : BaseFragment() {

    private var playerView: PlayerView? = null
    var exoPlayer: ExoPlayer? = null
    open val audioURL = ""
    var savedPlayerPosition = 0L
    private lateinit var mediaSession: MediaSessionCompat
    protected var extension: Exoplayer3Extension? = null

    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.player_view)
        playerView?.useController = true
        playerView?.controllerShowTimeoutMs = 0
        playerView?.controllerAutoShow = true

        mediaSession = MediaSessionCompat(requireContext(), "SensicMediaSession").apply {
            isActive = true
            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .build()
            )
        }
    }

    @OptIn(UnstableApi::class)
    fun prepareAudioPlayer() {
        val mediaSourceFactory: DefaultMediaSourceFactory = createMediaSourceFactory()
        val contentUri = Uri.parse(audioURL)
        val mediaItemBuild = MediaItem.Builder().setUri(contentUri)

        if (exoPlayer != null) {
            exoPlayer?.stop()
            exoPlayer?.release()
        }
        exoPlayer =
            ExoPlayer.Builder(requireContext()).setMediaSourceFactory(mediaSourceFactory)
                .build()
        playerView?.player = exoPlayer

        exoPlayer?.setMediaItem(mediaItemBuild.build())
        exoPlayer?.prepare()

        // Set PlayWhenReady. If true, content and ads will autoplay.
        exoPlayer?.playWhenReady = true
        ExoPlayerReceiver.setPlayer(exoPlayer!!, requireContext())
        ExoPlayerNotificationManager.showNotification(requireContext(), exoPlayer!!, mediaSession)
    }

    /**
     *  Creates the MediaSourceFactory for the Exoplayer.
     *  If adURL is set, it also creates ImaServerSideAdInsertionMediaSource.
     */
    @OptIn(UnstableApi::class)
    private fun createMediaSourceFactory(): DefaultMediaSourceFactory {
        val defaultDataSourceFactory =
            DefaultDataSourceFactory(
                requireContext(),
                Util.getUserAgent(requireContext(), "app")
            )
        val mediaSourceFactory = DefaultMediaSourceFactory(defaultDataSourceFactory)

        return mediaSourceFactory
    }


    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
            activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true
            && activity?.isInPictureInPictureMode == true
        ) {
            return
        }
        exoPlayer?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
            activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true
            && activity?.isInPictureInPictureMode == true
        ) {
            return
        }
        savedPlayerPosition = exoPlayer?.currentPosition ?: 0

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val mediaFrame = view?.findViewById<FrameLayout>(R.id.main_media_frame)
        val params = mediaFrame?.layoutParams as? ConstraintLayout.LayoutParams

        if (newConfig.orientation != Configuration.ORIENTATION_PORTRAIT) {
            params?.height = FrameLayout.LayoutParams.MATCH_PARENT
            params?.dimensionRatio = null
        } else {
            params?.dimensionRatio = "1:0.666"
            params?.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        if (exoPlayer != null) {
            exoPlayer?.stop()
            exoPlayer?.release()
            exoPlayer = null
        }

        ExoPlayerReceiver.setPlayer(null, requireContext())
        ExoPlayerNotificationManager.cancelNotification(requireContext())

        super.onDestroy()
    }

}