package com.gfk.s2s.demo.video.exoPlayer.manual

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.VolumeContentObserver
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.video.exoPlayer.BaseVideoFragment

class VODIMAFragment : BaseVideoFragment() {
    override val videoURL = "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
    override val adURL = getString(R.string.ad_vmap_pods)
    private var volumeContentObserver: VolumeContentObserver? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_ima)
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareVideoPlayer()
        addVolumeObserver()
    }

    private fun addVolumeObserver() {
        volumeContentObserver =
            object : VolumeContentObserver(requireContext(), Handler(Looper.getMainLooper())) {
                //This function will scale current volume between [0,100]
                override fun volumeChanged(currentVolume: Int) {

                }
            }

        requireActivity().applicationContext.contentResolver
            .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }

    override fun onStop() {
        super.onStop()
        volumeContentObserver?.let {
            requireActivity().contentResolver
                .unregisterContentObserver(it)
        }
    }
}