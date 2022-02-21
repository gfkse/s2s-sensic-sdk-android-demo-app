package com.gfk.s2s.demo.video.exoPlayer.manual

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.VolumeContentObserver
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.video.exoPlayer.BaseVideoFragment
import com.gfk.s2s.s2sagent.S2SAgent
import com.gfk.s2s.utils.Logger
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player

class VODIMAFragment : BaseVideoFragment() {
    override val videoURL = "https://demo-config-preproduction.sensic.net/video/video3.mp4"
    private val configUrl = "https://demo-config-preproduction.sensic.net/s2s-android.json"
    private val mediaId = "s2s-exoplayer-android-demo"
    private val contentId = "default"
    private val adId = "ad"
    private var volumeContentObserver: VolumeContentObserver? = null
    private var contentAgent: S2SAgent? = null
    private var adAgent: S2SAgent? = null

    private var isPlayingAd = false
    private var isPreRollPlayed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_ima)
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    var soughtOldPosition: Int? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adURL = getString(R.string.ad_pre_roll_linear_skippable)

        prepareVideoPlayer()
        addVolumeObserver()

        contentAgent = S2SAgent(configUrl, mediaId, context)
        adAgent = S2SAgent(configUrl, mediaId, context)

        contentAgent?.setStreamPositionCallback {
            soughtOldPosition ?: (exoPlayer?.currentPosition ?: 0).toInt()
        }

        adAgent?.setStreamPositionCallback {
            soughtOldPosition ?: (exoPlayer?.currentPosition ?: 0).toInt()
        }

        exoPlayer?.addListener(object : Player.Listener {

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                log("onPositionDiscontinuity oldPosition = ${oldPosition.positionMs} newPosition = ${newPosition.positionMs}")

                soughtOldPosition = oldPosition.positionMs.toInt()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                log("isPlaying = $isPlaying isAd = ${exoPlayer?.isPlayingAd}")

                if (isPlaying) {
                    soughtOldPosition = null
                    if (exoPlayer?.isPlayingAd == true) {
                        log("ad play")

                        adAgent?.playStreamOnDemand(adId, videoURL + "ads", getOptions(), null)
                    } else {
                        log("content play  position = ${exoPlayer?.contentPosition} duration = ${exoPlayer?.duration}")
                        isPreRollPlayed = exoPlayer?.contentPosition ?: 0 > exoPlayer?.duration ?: 0
                        if (!isPreRollPlayed) {
                            contentAgent?.playStreamOnDemand(
                                contentId,
                                videoURL,
                                getOptions(),
                                null
                            )
                        }
                    }
                } else {
                    if (isPlayingAd) {
                        log("ad stop")
                        adAgent?.stop()
                    } else {
                        log("content stop")
                        if (!isPreRollPlayed) {
                            contentAgent?.stop()
                        }
                    }
                }

                isPlayingAd = exoPlayer?.isPlayingAd == true
                playbackSpeedControlImageButton?.isVisible = !isPlayingAd
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                if (exoPlayer?.isPlaying == true) {
                    contentAgent?.stop()
                    contentAgent?.playStreamOnDemand(contentId, videoURL, getOptions(), null)
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        contentAgent?.flushEventStorage()
        adAgent?.flushEventStorage()
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
                //This function will scale current volume between [0,100]
                override fun volumeChanged(currentVolume: Int) {
                    if (isPlayingAd) {
                        adAgent?.volume(currentVolume.toString())
                    } else {
                        contentAgent?.volume(currentVolume.toString())
                    }
                }
            }

        requireActivity().applicationContext.contentResolver
            .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }

    private fun log(message: String?) = Log.e("VODIMAFragment", message ?: "")

}