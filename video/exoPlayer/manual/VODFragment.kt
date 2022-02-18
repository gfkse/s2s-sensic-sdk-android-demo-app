package com.gfk.s2s.demo.video.exoPlayer.manual

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.VolumeContentObserver
import com.gfk.s2s.s2sagent.S2SAgent
import com.gfk.s2s.demo.video.exoPlayer.BaseVideoFragment
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player


class VODFragment : BaseVideoFragment() {
    override val videoURL = "https://demo-config-preproduction.sensic.net/video/video3.mp4"
    private val configUrl = "https://demo-config-preproduction.sensic.net/s2s-android.json"
    private val mediaId = "s2s-exoplayer-android-demo"
    private var volumeContentObserver: VolumeContentObserver? = null
    private var agent: S2SAgent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_vod)
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    var soughtOldPosition: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.prepareVideoPlayer()
        addVolumeObserver()

        agent = S2SAgent(configUrl, mediaId, context)

        agent?.setStreamPositionCallback {
            soughtOldPosition ?: (exoPlayer?.currentPosition ?: 0).toInt()
        }

        exoPlayer?.addListener(object : Player.Listener {

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                soughtOldPosition = oldPosition.positionMs.toInt()

            }


            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    soughtOldPosition = null
                    agent?.playStreamOnDemand(mediaId, videoURL, getOptions(), null)
                } else {
                    agent?.stop()
                }
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                if (exoPlayer?.isPlaying == true) {
                    agent?.stop()
                    agent?.playStreamOnDemand(mediaId, videoURL, getOptions(), null)
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        exoPlayer?.playWhenReady = false
        agent?.flushEventStorage()
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
                    agent?.volume(currentVolume.toString())
                }
            }

        requireActivity().applicationContext.contentResolver
            .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }
}