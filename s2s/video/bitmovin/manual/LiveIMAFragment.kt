package com.gfk.s2s.demo.s2s.video.bitmovin.manual

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitmovin.player.api.event.PlayerEvent
import com.bitmovin.player.api.event.on
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.VolumeContentObserver
import com.gfk.s2s.demo.s2s.video.bitmovin.BaseVideoFragment
import com.gfk.s2s.s2sagent.S2SAgent
import kotlin.math.abs
import kotlin.math.floor


class LiveIMAFragment : BaseVideoFragment() {
    override val videoURL = "https://mcdn.daserste.de/daserste/de/master.m3u8"
    private val configUrl = "https://demo-config.sensic.net/s2s-android.json"
    private val mediaId = "s2s-bitmovinplayer-android-demo"
    private var contentAgent: S2SAgent? = null
    private var adAgent: S2SAgent? = null
    private var volumeContentObserver: VolumeContentObserver? = null
    private val contentIdDefault = "default"
    private var previousPlaybackSpeed: Float? = null
    private var isSeeking = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_live_ima)
        return inflater.inflate(R.layout.bitmovin_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adSourcePreRoll = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator="

        var firstPlayEvent = false
        prepareVideoPlayer()
        addVolumeObserver()

        contentAgent = S2SAgent(configUrl, mediaId, context)
        adAgent = S2SAgent(configUrl, mediaId, context)


        contentAgent?.setStreamPositionCallback {
            ((player?.currentTime ?: 0.0)*1000).toInt()
        }

        adAgent?.setStreamPositionCallback {
            ((player?.currentTime ?: 0.0)*1000).toInt()
        }

        player?.on<PlayerEvent.Playing> {
            if(!isSeeking) {
                firstPlayEvent = true
                contentAgent?.playStreamLive(
                        contentIdDefault,
                        "",
                        getOffset(),
                        videoURL,
                        getOptions(),
                        null
                )
            }
        }
        player?.on<PlayerEvent.Paused> {
            if(firstPlayEvent && player?.isAd == false && !isSeeking){
                contentAgent?.stop()
            }
        }
        player?.on<PlayerEvent.PlaybackFinished> {
            contentAgent?.stop()
        }

        player?.on<PlayerEvent.Muted> {
            contentAgent?.volume("0")
        }

        player?.on<PlayerEvent.Unmuted> {
            contentAgent?.volume(volumeContentObserver?.getScaledCurrentVolume().toString())
        }

        player?.on<PlayerEvent.TimeChanged> {
            if(previousPlaybackSpeed != null && previousPlaybackSpeed != player?.playbackSpeed){
                contentAgent?.stop()
                contentAgent?.playStreamLive(
                        contentIdDefault,
                        "",
                        getOffset(),
                        videoURL,
                        getOptions(),
                        null
                )
            }
            previousPlaybackSpeed = player?.playbackSpeed
        }

        player?.on<PlayerEvent.TimeShift> {
            if(player?.isPlaying == true && !isSeeking){
                isSeeking = true
                contentAgent?.stop()
            }
        }

        player?.on<PlayerEvent.TimeShifted> {
            if(isSeeking){
                contentAgent?.playStreamLive(
                        contentIdDefault,
                        "",
                        getOffset(),
                        videoURL,
                        getOptions(),
                        null
                )
                isSeeking = false
            }
        }

        player?.on<PlayerEvent.AdStarted> {
            adAgent?.playStreamOnDemand("ad", videoURL, getOptions(), null)
        }

        player?.on<PlayerEvent.AdFinished> {
            adAgent?.stop()
        }

        player?.on<PlayerEvent.AdSkipped> {
            adAgent?.stop()
        }
    }

    private fun getOptions() = mapOf(
        "volume" to if (player?.isMuted == true) "0" else volumeContentObserver?.getScaledCurrentVolume().toString(),
        "speed" to ((player?.playbackSpeed ?: "1.0").toString()),
    )

    override fun onStop() {
        super.onStop()
        if(player?.isAd == true){
            adAgent?.stop()
        }
        contentAgent?.flushEventStorage()
        adAgent?.flushEventStorage()
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
                        contentAgent?.volume(if(player?.isMuted == true) "0" else volumeContentObserver?.getScaledCurrentVolume().toString())
                    }
                }

        requireActivity().applicationContext.contentResolver
                .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }

    private fun getOffset(): Int{
        return abs(floor((player?.timeShift ?: 0.0) * 1000)).toInt()
    }
}