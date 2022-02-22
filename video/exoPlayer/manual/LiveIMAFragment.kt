package com.gfk.s2s.demo.video.exoPlayer.manual

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.gfk.s2s.s2sagent.S2SAgent
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.VolumeContentObserver
import com.gfk.s2s.demo.video.exoPlayer.BaseVideoFragment
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player


open class LiveIMAFragment : BaseVideoFragment() {
    override val videoURL = "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
    private val configUrl = "https://demo-config-preproduction.sensic.net/s2s-android.json"
    private val mediaId = "s2s-exoplayer-android-demo"
    private val contentIdDefault = "default"
    private val contentIdAd = "ad"
    private var volumeContentObserver: VolumeContentObserver? = null
    private var contentAgent: S2SAgent? = null
    private var adAgent: S2SAgent? = null
    private var isPlayingAd = false
    var soughtPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_live_ima)
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adURL = getString(R.string.ad_pre_roll_linear_skippable)

        prepareVideoPlayer()
        addVolumeObserver()

        contentAgent = S2SAgent(configUrl, mediaId, context)
        adAgent = S2SAgent(configUrl, mediaId, context)

        adAgent?.setStreamPositionCallback {
            soughtPosition ?: (exoPlayer?.currentPosition ?: 0).toInt()
        }

        exoPlayer?.addListener(object : Player.Listener {

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                soughtPosition = oldPosition.positionMs.toInt()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                when {
                    exoPlayer?.isPlayingAd == true -> {
                        soughtPosition = null
                        adAgent?.playStreamOnDemand(contentIdAd, videoURL + "ads", getOptions(), null)
                    }
                    isPlaying -> {
                        contentAgent?.playStreamLive(
                            contentIdDefault,
                            "",
                            0,
                            configUrl,
                            getOptions(),
                            null
                        )
                    }
                    isPlayingAd -> {
                        adAgent?.stop()
                    }
                    else -> {
                        contentAgent?.stop()
                    }
                }

                isPlayingAd = exoPlayer?.isPlayingAd == true
                playbackSpeedControlImageButton?.isVisible = !isPlayingAd
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                if (exoPlayer?.isPlaying == true) {
                    contentAgent?.stop()
                    contentAgent?.playStreamLive(
                        contentIdDefault,
                        "",
                        0,
                        configUrl,
                        getOptions(),
                        null
                    )
                }

            }
        })
    }

    override fun onStop() {
        super.onStop()
        adAgent?.flushEventStorage()
        contentAgent?.flushEventStorage()
        volumeContentObserver?.let {
            requireActivity().contentResolver
                .unregisterContentObserver(it)
        }
    }

    //Please scale your volume between [0,100] as maxVolume is 100
    private fun getOptions() = mapOf(
        "volume" to volumeContentObserver?.getScaledCurrentVolume().toString(),
        "speed" to (exoPlayer?.playbackParameters?.speed?.toString() ?: "1.0")
    )


    private fun addVolumeObserver() {
        volumeContentObserver =
            object : VolumeContentObserver(requireContext(), Handler(Looper.getMainLooper())) {
                override fun volumeChanged(currentVolume: Int) {
                    contentAgent?.volume("" + currentVolume)
                }
            }
        requireActivity().applicationContext.contentResolver
            .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }
}