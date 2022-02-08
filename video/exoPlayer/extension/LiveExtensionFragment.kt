package com.gfk.s2s.demo.video.exoPlayer.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.video.exoPlayer.BaseVideoFragment
import com.gfk.s2s.exoplayer.ExoplayerS2SExtension
import com.gfk.s2s.s2sExtension.GfkS2SConfig

open class LiveExtensionFragment : BaseVideoFragment() {

    override val videoURL = "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
    private val configUrl = "https://demo-config-preproduction.sensic.net/s2s-android.json"
    private val mediaId = "s2s-exoplayer-android-demo"
    private val contentId = "default"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_live)
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.prepareLiveVideoPlayer()
        val config = GfkS2SConfig(mediaId, configUrl, true, hashMapOf())
        ExoplayerS2SExtension(
            exoPlayer!!,
            config,
            contentId,
            hashMapOf(),
            requireContext(),
            this,
        )
    }

    override fun onStop() {
        super.onStop()
        exoPlayer?.pause()
    }
}