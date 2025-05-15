package com.gfk.s2s.demo.audio.exoPlayer2

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.gfk.s2s.demo.BaseFragment
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.exoplayer.ExoplayerExtension
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

open class BaseAudioFragment : BaseFragment() {

    private var playerView: StyledPlayerView? = null
    var exoPlayer: ExoPlayer? = null
    open val audioURL = ""
    var savedPlayerPosition = 0L
    private lateinit var mediaSession: MediaSessionCompat
    protected var extension: ExoplayerExtension? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.player_view)
        playerView?.useController = true
        playerView?.controllerShowTimeoutMs = 0
        playerView?.controllerAutoShow = true

        mediaSession = MediaSessionCompat(requireContext(), "SensicMediaSession").apply {
            isActive = true
            setPlaybackState(
                PlaybackStateCompat.Builder().build()
            )
        }
    }

    fun prepareAudioPlayer() {
        val contentUri = Uri.parse(audioURL)
        val mediaSource = createMediaSource(contentUri)

        exoPlayer?.stop()
        exoPlayer?.release()

        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        playerView?.player = exoPlayer

        exoPlayer?.prepare(mediaSource)
        exoPlayer?.playWhenReady = true

         ExoPlayerReceiver.setPlayer(exoPlayer!!, requireContext())
         ExoPlayerNotificationManager.showNotification(requireContext(), exoPlayer!!, mediaSession)
    }

    private fun createMediaSource(contentUri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), "app")
        )
        val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(contentUri)
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
    }


    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
            activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true &&
            activity?.isInPictureInPictureMode == true
        ) {
            return
        }
        exoPlayer?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
            activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true &&
            activity?.isInPictureInPictureMode == true
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
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer = null

        //ExoPlayerReceiver.setPlayer(null, requireContext())
        // ExoPlayerNotificationManager.cancelNotification(requireContext())

        super.onDestroy()
    }
}
