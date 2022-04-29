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
import com.gfk.s2s.s2sExtension.SensicEvent
import com.google.ads.interactivemedia.v3.api.AdEvent
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
    var soughtPosition: Int? = null
    var lastContentSensicEvent: SensicEvent? = SensicEvent.stop

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

        var lastAdPlay = 0L

        super.adEventListener = AdEvent.AdEventListener { adEvent ->
            when (adEvent.type) {
                AdEvent.AdEventType.STARTED -> {
                    lastAdPlay = System.currentTimeMillis() - (exoPlayer?.currentPosition ?: 0)
                    adAgent?.playStreamOnDemand(contentIdAd, videoURL + "ads", getOptions(), null
                    )
                }
                AdEvent.AdEventType.PAUSED, AdEvent.AdEventType.SKIPPED, AdEvent.AdEventType.AD_BUFFERING -> {
                    adAgent?.stop(System.currentTimeMillis() - lastAdPlay)
                }
                AdEvent.AdEventType.RESUMED -> {
                    adAgent?.playStreamOnDemand(contentIdAd, videoURL + "ads", getOptions(), null)
                    lastAdPlay <= System.currentTimeMillis() - (exoPlayer?.currentPosition ?: 0)
                }
                AdEvent.AdEventType.COMPLETED -> {
                    val position = System.currentTimeMillis() - lastAdPlay
                    adAgent?.stop(position)
                }
                else -> {}
            }
        }

        contentAgent = S2SAgent(configUrl, mediaId, context)
        adAgent = S2SAgent(configUrl, mediaId, context)

        contentAgent?.setStreamPositionCallback {
            soughtPosition ?: (exoPlayer?.currentPosition ?: 0).toInt()
        }

        adAgent?.setStreamPositionCallback {
            (System.currentTimeMillis() - lastAdPlay).toInt()
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
                playbackSpeedControlImageButton?.isVisible = exoPlayer?.isPlaying == false

                if (lastContentSensicEvent != SensicEvent.play && exoPlayer?.isPlaying == true && exoPlayer?.isPlayingAd == false) {
                    soughtPosition = null
                    contentAgent?.playStreamOnDemand(contentIdDefault, videoURL + "ads", getOptions(), null)
                } else if (lastContentSensicEvent != SensicEvent.stop && exoPlayer?.isPlaying == false && exoPlayer?.isPlayingAd == false) {
                    contentAgent?.stop()
                }
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                if (exoPlayer?.isPlaying == true && exoPlayer?.isPlayingAd == false) {
                    contentAgent?.stop()
                    contentAgent?.playStreamOnDemand(contentIdDefault, videoURL, getOptions(), null)
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
                    if (exoPlayer?.isPlayingAd == false) contentAgent?.volume("" + currentVolume)
                }
            }
        requireActivity().applicationContext.contentResolver
            .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }
}