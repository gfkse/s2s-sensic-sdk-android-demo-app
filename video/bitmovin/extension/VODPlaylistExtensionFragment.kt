package com.gfk.s2s.demo.s2s.video.bitmovin.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitmovin.player.api.playlist.PlaylistConfig
import com.bitmovin.player.api.playlist.PlaylistOptions
import com.bitmovin.player.api.source.Source
import com.bitmovin.player.api.source.SourceConfig
import com.gfk.s2s.bitmovinplayer.BitmovinplayerExtension
import com.gfk.s2s.demo.s2s.DemoApplication.Companion.configURL
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.constants.DemoConstants.vdoVideoUrl
import com.gfk.s2s.demo.s2s.constants.DemoConstants.vdoVideoUrl1
import com.gfk.s2s.demo.s2s.constants.DemoConstants.vdoVideoUrl2
import com.gfk.s2s.demo.s2s.video.bitmovin.BaseVideoFragment
import com.gfk.s2s.s2sExtension.ContentMetadata
import com.gfk.s2s.s2sagent.S2SConfig

class VODPlaylistExtensionFragment : BaseVideoFragment() {
    private val configUrl = configURL
    private val mediaId = "s2s-bitmovin-player-android-demo"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_vod_playlist)
        return inflater.inflate(R.layout.bitmovin_video_playlist_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sourceConfig1 = SourceConfig(
            url = vdoVideoUrl
        ).apply {
            title = "First Video"
        }
        val source1 = Source.create(sourceConfig1)

        val sourceConfig2 = SourceConfig(
            url = vdoVideoUrl2
        ).apply {
            title = "Second Video"
        }
        val source2 = Source.create(sourceConfig2)

        val sourceConfig3 = SourceConfig(
            url = vdoVideoUrl1
        ).apply {
            title = "Third Video"
        }
        val source3 = Source.create(sourceConfig3)

        prepareVideoPlayerWithPlaylist(PlaylistConfig(
            sources = listOf(source1, source2, source3),
            options = PlaylistOptions()
        ))

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

        BitmovinplayerExtension(
            player!!,
            config,
            contentMetadata,
            requireContext(),
            this
        )
    }
}