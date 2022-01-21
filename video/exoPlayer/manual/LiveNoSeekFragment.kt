package com.gfk.s2s.demo.video.exoPlayer.manual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R

class LiveNoSeekFragment : LiveFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_live_no_seekbar)
        return inflater.inflate(R.layout.video_fragment_no_seekbar, container, false)
    }
}