package com.gfk.s2s.demo.video.exoPlayer

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import com.gfk.s2s.demo.BaseFragment
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.BaseMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * class BaseVideoFragment has the code to show the exoplayer.
 *  The values for videoUrl, configUrl and mediaId are overridden in the
 *  fragments extending from this class.
 */

open class BaseVideoFragment : BaseFragment() {
    var playerView: PlayerView? = null
    var exoPlayer: SimpleExoPlayer? = null
    var playbackSpeedControlImageButton: ImageButton? = null
    open val videoURL = ""
    var savedPlayerPosition = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.player_view)
    }


    fun prepareVodVideoPlayer() =
        prepareVideoPlayer {
            val userAgent = Util.getUserAgent(
                requireContext(), context?.getString(R.string.app_name)
                    ?: ""
            )
            ProgressiveMediaSource.Factory(
                DefaultDataSourceFactory(
                    requireContext(),
                    userAgent
                )
            ).createMediaSource(Uri.parse(videoURL))
        }

    fun prepareLiveVideoPlayer() =
        prepareVideoPlayer {
            val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory(
                Util.getUserAgent(requireContext(), getString(R.string.app_name))
            )

            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                Uri.parse(videoURL)
            )
        }

    private fun prepareVideoPlayer(mediaSourceProvider: () -> BaseMediaSource) {
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        playerView?.player = exoPlayer

        playbackSpeedControlImageButton =
            view?.findViewById(R.id.playback_speed_control_image_button)
        playbackSpeedControlImageButton?.setOnClickListener { showPlayerSpeedDialog() }

        exoPlayer?.prepare(mediaSourceProvider())
        exoPlayer?.seekTo(savedPlayerPosition)
        exoPlayer?.playWhenReady = true
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
        exoPlayer?.playWhenReady = false
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

    private fun showPlayerSpeedDialog() {
        val popupMenu = PopupMenu(requireContext(), playbackSpeedControlImageButton!!)

        val playerSpeedOptions = arrayOf(0.5f, 1.0f, 1.5f, 2.0f)

        playerSpeedOptions.forEachIndexed { i, s -> popupMenu.menu.add(i, i, i, "${s}x") }

        popupMenu.setOnMenuItemClickListener { item ->
            changePlaybackSpeed(playerSpeedOptions[item.itemId])
            false
        }

        popupMenu.show()
    }

    private fun changePlaybackSpeed(speed: Float): Unit? {
        return exoPlayer?.setPlaybackParameters(PlaybackParameters(speed))
    }
}