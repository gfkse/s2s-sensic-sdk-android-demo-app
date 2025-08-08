package com.gfk.s2s.demo.s2s.video.exoPlayer.manual

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.s2sagent.S2SAgent
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.VolumeContentObserver
import com.gfk.s2s.demo.s2s.video.exoPlayer.BaseVideoFragment
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player


open class LiveFragment : BaseVideoFragment() {

    override val videoURL = "https://mcdn.daserste.de/daserste/de/master.m3u8"
    private val configUrl = "https://demo-config.sensic.net/s2s-android.json"
    private val mediaId = "s2s-exoplayer-android-demo"
    private val contentIdDefault = "default"
    private var volumeContentObserver: VolumeContentObserver? = null
    private var agent: S2SAgent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_live)
        return inflater.inflate(R.layout.exoplayer_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareVideoPlayer()
        addVolumeObserver()

        agent = S2SAgent(configUrl, mediaId, context)
        exoPlayer?.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    agent?.playStreamLive(
                        contentIdDefault,
                        "",
                        0,
                        videoURL,
                        getOptions(),
                        null
                    )
                } else {
                    agent?.stop()
                }
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                if (exoPlayer?.isPlaying == true) {
                    agent?.stop()
                    agent?.playStreamLive(
                        contentIdDefault,
                        "",
                        0,
                        videoURL,
                        getOptions(),
                        null
                    )
                }

            }
        })
    }

    override fun onStop() {
        super.onStop()
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
                override fun volumeChanged(currentVolume: Int) {
                    agent?.volume("" + currentVolume)
                }
            }
        requireActivity().applicationContext.contentResolver
            .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }
}