package com.gfk.s2s.demo.video.bitmovin.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitmovin.player.api.PlayerConfig
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R

class LiveNoSeekExtensionFragment : LiveExtensionFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_live_no_seekbar)
        return inflater.inflate(R.layout.bitmovin_video_fragment, container, false)
    }

    override fun playerConfig(): PlayerConfig {
        val playerConfig = PlayerConfig()
        playerConfig.playbackConfig.isTimeShiftEnabled = false
        return playerConfig
    }
}