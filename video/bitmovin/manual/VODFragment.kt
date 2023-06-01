package com.gfk.s2s.demo.video.bitmovin.manual

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.s2sagent.S2SAgent
import com.bitmovin.player.api.event.PlayerEvent
import com.bitmovin.player.api.event.on
import com.gfk.s2s.demo.VolumeContentObserver
import com.gfk.s2s.demo.video.bitmovin.BaseVideoFragment


class VODFragment : BaseVideoFragment() {
    override val videoURL = "https://demo-config-preproduction.sensic.net/video/video3.mp4"
    private val configUrl = "https://demo-config.sensic.net/s2s-android.json"
    private val mediaId = "s2s-bitmovinplayer-android-demo"
    private var agent: S2SAgent? = null
    private var volumeContentObserver: VolumeContentObserver? = null
    private val contentIdDefault = "default"
    private var previousPlaybackSpeed: Float? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_vod)
        return inflater.inflate(R.layout.bitmovin_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareVideoPlayer()
        addVolumeObserver()

        agent = S2SAgent(configUrl, mediaId, context)

        agent?.setStreamPositionCallback {
            ((player?.currentTime ?: 0.0)*1000).toInt()
        }

        player?.on<PlayerEvent.Playing> {
            agent?.playStreamOnDemand(contentIdDefault, videoURL, getOptions(), null)
        }
        player?.on<PlayerEvent.Paused> {
            agent?.stop()
        }
        player?.on<PlayerEvent.PlaybackFinished> {
            agent?.stop()
        }

        player?.on<PlayerEvent.Muted> {
            agent?.volume("0")
        }

        player?.on<PlayerEvent.Unmuted> {
            agent?.volume(volumeContentObserver?.getScaledCurrentVolume().toString())
        }

        player?.on<PlayerEvent.TimeChanged> {
            if(previousPlaybackSpeed != null && previousPlaybackSpeed != player?.playbackSpeed){
                agent?.stop()
                agent?.playStreamOnDemand(contentIdDefault, videoURL, getOptions(), null)
            }
            previousPlaybackSpeed = player?.playbackSpeed
        }

    }

    private fun getOptions() = mapOf(
        "volume" to if (player?.isMuted == true) "0" else volumeContentObserver?.getScaledCurrentVolume().toString(),
        "speed" to ((player?.playbackSpeed ?: "1.0").toString()),
    )

    override fun onStop() {
        super.onStop()
        agent?.flushEventStorage()
        volumeContentObserver?.let {
            requireActivity().contentResolver
                    .unregisterContentObserver(it)
        }
    }

    private fun addVolumeObserver() {
        volumeContentObserver =
                object : VolumeContentObserver(requireContext(), Handler(Looper.getMainLooper())) {
                    //This function will scale current volume between [0,100]
                    override fun volumeChanged(currentVolume: Int) {
                        agent?.volume(if(player?.isMuted == true) "0" else volumeContentObserver?.getScaledCurrentVolume().toString())
                    }
                }

        requireActivity().applicationContext.contentResolver
                .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }
}