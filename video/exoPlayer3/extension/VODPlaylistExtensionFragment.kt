package com.gfk.s2s.demo.s2s.video.exoPlayer3.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import com.gfk.s2s.demo.s2s.DemoApplication.Companion.configURL
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.constants.DemoConstants
import com.gfk.s2s.demo.s2s.video.exoPlayer3.BaseVideoFragment
import com.gfk.s2s.exoplayer.Exoplayer3Extension
import com.gfk.s2s.s2sExtension.ContentMetadata
import com.gfk.s2s.s2sagent.S2SConfig

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
        return inflater.inflate(R.layout.exoplayer3_video_fragment, container, false)
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

        Exoplayer3Extension(
            exoPlayer!!,
            config,
            contentMetadata,
            requireContext(),
            this
        )
    }
}