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
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.utils.Logger
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.ads.interactivemedia.v3.api.player.AdMediaInfo
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.ext.ima.ImaServerSideAdInsertionMediaSource
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * class BaseVideoFragment has the code to show the exoplayer.
 *  The values for videoUrl, configUrl and mediaId are overridden in the
 *  fragments extending from this class.
 */

open class BaseVideoFragment : BaseFragment() {
    private var playerView: StyledPlayerView? = null
    var exoPlayer: ExoPlayer? = null
    var adsLoader: ImaAdsLoader? = null
    var playbackSpeedControlImageButton: ImageButton? = null
    open val videoURL = ""
    open var adURL = ""
    var savedPlayerPosition = 0L
    private var serverSideAdsLoader: ImaServerSideAdInsertionMediaSource.AdsLoader? = null
    open var adEventListener: AdEvent.AdEventListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.player_view)
        playbackSpeedControlImageButton =
            view.findViewById(R.id.playback_speed_control_image_button)

        // initialize adsloader with AdEventListener.
        val adsLoaderBuilder =
            ImaAdsLoader.Builder(requireContext()).setAdEventListener { adEvent ->
                adEventListener?.onAdEvent(adEvent)
            }
        adsLoader = adsLoaderBuilder.build()
    }

    fun prepareVideoPlayer() {
        val mediaSourceFactory: DefaultMediaSourceFactory = createMediaSourceFactory()
        val contentUri = Uri.parse(videoURL)
        val mediaItemBuild = MediaItem.Builder().setUri(contentUri);

        if (exoPlayer != null) {
            exoPlayer?.stop();
            exoPlayer?.release();
        }

        if (adURL.isEmpty()) {
            exoPlayer =
                ExoPlayer.Builder(requireContext()).setMediaSourceFactory(mediaSourceFactory)
                    .build()
        } else {
            mediaSourceFactory.setAdsLoaderProvider { adsLoader }
                .setAdViewProvider(playerView)
            mediaItemBuild.setAdsConfiguration(
                MediaItem.AdsConfiguration.Builder(Uri.parse(adURL)).build()
            )
            exoPlayer =
                ExoPlayer.Builder(requireContext()).setMediaSourceFactory(mediaSourceFactory)
                    .build()
            serverSideAdsLoader!!.setPlayer(exoPlayer!!)
            adsLoader?.setPlayer(exoPlayer)
        }

        playerView?.player = exoPlayer
        exoPlayer?.setMediaItem(mediaItemBuild.build())
        exoPlayer?.prepare()

        // Set PlayWhenReady. If true, content and ads will autoplay.
        exoPlayer?.playWhenReady = true
    }

    /**
     *  Creates the MediaSourceFactory for the Exoplayer.
     *  If adURL is set, it also creates ImaServerSideAdInsertionMediaSource.
     */
    private fun createMediaSourceFactory(): DefaultMediaSourceFactory {
        val defaultDataSourceFactory =
            DefaultDataSourceFactory(requireContext(), Util.getUserAgent(requireContext(), "app"))
        val mediaSourceFactory = DefaultMediaSourceFactory(defaultDataSourceFactory);

        if (adURL.isEmpty()) {
            return mediaSourceFactory
        }

        val serverSideAdLoaderBuilder =
            ImaServerSideAdInsertionMediaSource.AdsLoader.Builder(requireContext(),
                playerView!!
            )
        serverSideAdsLoader = serverSideAdLoaderBuilder.build()
        val imaServerSideAdInsertionMediaSourceFactory =
            ImaServerSideAdInsertionMediaSource.Factory(
                serverSideAdsLoader!!, mediaSourceFactory
            )

        return mediaSourceFactory.setAdsLoaderProvider { adsLoader }
            .setAdViewProvider(playerView)
            .setServerSideAdInsertionMediaSourceFactory(imaServerSideAdInsertionMediaSourceFactory)
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

    override fun onStop() {
        super.onStop()
        exoPlayer?.playWhenReady = false
    }

    override fun onDestroy() {
        adsLoader?.release()

        if (exoPlayer != null) {
            exoPlayer?.stop();
            exoPlayer?.release();
            exoPlayer = null;
        }
        super.onDestroy()
    }
}