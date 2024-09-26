package com.gfk.s2s.demo.video.exoPlayer3.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.video.exoPlayer3.BaseVideoFragment
import com.gfk.s2s.exoplayer.Exoplayer3Extension
import com.gfk.s2s.s2sExtension.ContentMetadata
import com.gfk.s2s.s2sagent.S2SConfig

class LiveImaExtensionFragment : BaseVideoFragment() {

    override val videoURL = "https://mcdn.daserste.de/daserste/de/master.m3u8"
    private val configUrl = "https://demo-config.sensic.net/s2s-android.json"
    private val mediaId = "s2s-exoplayer-android-demo"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_live_ima)
        return inflater.inflate(R.layout.exoplayer3_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adURL = getString(R.string.ad_pre_roll_linear_skippable)
        super.prepareVideoPlayer()

        val config = S2SConfig(
            mediaId,
            configUrl,
            true,
            null
        )

        val customParams = HashMap<String, String>()
        customParams["cp1"] = "<your new cp1 value here>"
        customParams["cp2"] = "<your new cp2 value here>"

        val contentMetadata = ContentMetadata(customParams)

        val extension = Exoplayer3Extension(
            exoPlayer!!,
            config,
            contentMetadata,
            requireContext(),
            this
        )

        extension.activateNativeAdSupport()
    }
}