package com.gfk.s2s.demo.video.exoPlayer.manual

import android.net.Uri
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
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class IMAFragment : BaseVideoFragment() {
    override val videoURL = "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
    private var adsLoader: ImaAdsLoader? = null
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
        adsLoader = ImaAdsLoader.Builder(requireContext()).build()
        prepareVideoPlayer()
        addVolumeObserver()
    }

    override fun prepareVideoPlayer() {
        // Set up the factory for media sources, passing the ads loader and ad view providers.
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(
                requireContext(),
                Util.getUserAgent(requireContext(), getString(R.string.app_name))
            )

        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            .setAdsLoaderProvider { adsLoader }
            .setAdViewProvider(playerView)

        // Create a SimpleExoPlayer and set it as the player for content and ads.
        exoPlayer =
            SimpleExoPlayer.Builder(requireContext()).setMediaSourceFactory(mediaSourceFactory)
                .build()
        playerView?.player = exoPlayer
        adsLoader?.setPlayer(exoPlayer)

        // Create the MediaItem to play, specifying the content URI and ad tag URI.
        val contentUri = Uri.parse(getString(R.string.content_url))
        val adTagUri = Uri.parse(getString(R.string.ad_vmap_pods))
        val mediaItem = MediaItem.Builder().setUri(contentUri).setAdTagUri(adTagUri).build()

        // Prepare the content and ad to be played with the SimpleExoPlayer.
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        // Set PlayWhenReady. If true, content and ads will autoplay.
        exoPlayer?.playWhenReady = true
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
        exoPlayer?.playWhenReady = false
        volumeContentObserver?.let {
            requireActivity().contentResolver
                .unregisterContentObserver(it)
        }
    }

    override fun onDestroy() {
        adsLoader?.release()
        super.onDestroy()
    }
}