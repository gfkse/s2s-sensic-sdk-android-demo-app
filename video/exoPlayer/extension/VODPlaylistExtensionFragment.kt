package com.gfk.s2s.demo.s2s.video.exoPlayer.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.demo.s2s.DemoApplication.Companion.configURL
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.constants.DemoConstants
import com.gfk.s2s.demo.s2s.video.exoPlayer.BaseVideoFragment
import com.gfk.s2s.exoplayer.ExoplayerExtension
import com.gfk.s2s.s2sExtension.ContentMetadata
import com.gfk.s2s.s2sagent.S2SConfig
import com.google.android.exoplayer2.MediaItem

class VODPlaylistExtensionFragment : BaseVideoFragment() {
    private val configUrl = configURL
    private val mediaId = "s2s-exoplayer-android-demo"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_vod_playlist)
        return inflater.inflate(R.layout.exoplayer_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstItem = MediaItem.fromUri(DemoConstants.vdoVideoUrl1)
        val secondItem = MediaItem.fromUri(DemoConstants.vdoVideoUrl2)
        val thirdItem = MediaItem.fromUri(DemoConstants.vdoVideoUrl)
        super.prepareVideoPlayerWithPlaylist(listOf(firstItem, secondItem, thirdItem))

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

        ExoplayerExtension(
            exoPlayer!!,
            config,
            contentMetadata,
            requireContext(),
            this
        )
    }
}