package com.gfk.s2s.demo.s2s.video.exoPlayer3.manual

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R

class LiveNoSeekFragment : LiveFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_live_no_seekbar)
        val view = inflater.inflate(R.layout.exoplayer3_video_fragment_no_seekbar, container, false)
        view?.findViewById<ImageButton>(R.id.exo_pause)?.setOnClickListener {
            exoPlayer?.pause()
        }
        view?.findViewById<ImageButton>(R.id.exo_play)?.setOnClickListener {
            exoPlayer?.play()
            exoPlayer?.seekToDefaultPosition()
        }
        return view
    }
}