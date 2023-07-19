package com.gfk.s2s.demo.video.exoPlayer.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.video.exoPlayer.BaseVideoFragment
import com.gfk.s2s.exoplayer.ExoplayerExtension
import com.gfk.s2s.s2sExtension.ContentMetadata
import com.gfk.s2s.s2sagent.S2SConfig

class VODIMAExtensionFragment : BaseVideoFragment() {

    override val videoURL = "https://demo-config-preproduction.sensic.net/video/video3.mp4"
    private val configUrl = "https://demo-config.sensic.net/s2s-android.json"
    private val mediaId = "s2s-exoplayer-android-demo"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_vod_ima)
        return inflater.inflate(R.layout.exoplayer_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adURL = getString(R.string.ad_vmap_pods)
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

        val extension = ExoplayerExtension(
                exoPlayer!!,
                config,
                contentMetadata,
                requireContext(),
                this
        )

        extension.activateNativeAdSupport()
    }
}