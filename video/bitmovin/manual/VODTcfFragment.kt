package com.gfk.s2s.demo.s2s.video.bitmovin.manual

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import com.bitmovin.player.api.event.PlayerEvent
import com.bitmovin.player.api.event.on
import com.gfk.s2s.demo.s2s.DemoApplication.Companion.configURL
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.VolumeContentObserver
import com.gfk.s2s.demo.s2s.cmp.DemoVideoConsentManager
import com.gfk.s2s.demo.s2s.cmp.ReceivedConsent.Companion.getReceivedConsent
import com.gfk.s2s.demo.s2s.constants.DemoConstants.vdoVideoUrl
import com.gfk.s2s.demo.s2s.video.bitmovin.BaseVideoFragment
import com.gfk.s2s.s2sagent.S2SAgent
import com.gfk.s2s.tcf.Tcf23ConsentParams
import com.gfk.s2s.utils.SensicLogger


class VODTcfFragment : BaseVideoFragment() {
    override val videoURL = vdoVideoUrl
    private val configUrl = configURL
    private val mediaId = "s2s-bitmovin-player-android-demo"
    private var agent: S2SAgent? = null
    private var volumeContentObserver: VolumeContentObserver? = null
    private val contentIdDefault = "default"
    private var previousPlaybackSpeed: Float? = null
    private lateinit var demoVideoConsentManager: DemoVideoConsentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_vod_tcf)
        return inflater.inflate(R.layout.bitmovin_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.tcf_panel)?.visibility = View.VISIBLE
        addVolumeObserver()
        prepareTcfControls(view)
    }

    private fun getOptions() = mapOf(
        "volume" to if (player?.isMuted == true) "0" else volumeContentObserver?.getScaledCurrentVolume().toString(),
        "speed" to ((player?.playbackSpeed ?: "1.0").toString()),
    )

    override fun onStop() {
        super.onStop()
        agent?.flushEventStorage()
        volumeContentObserver?.let {
            requireActivity().contentResolver
                    .unregisterContentObserver(it)
        }
    }

    private fun addVolumeObserver() {
        volumeContentObserver =
                object : VolumeContentObserver(requireContext(), Handler(Looper.getMainLooper())) {
                    //This function will scale current volume between [0,100]
                    override fun volumeChanged(currentVolume: Int) {
                        agent?.volume(if(player?.isMuted == true) "0" else volumeContentObserver?.getScaledCurrentVolume().toString())
                    }
                }

        requireActivity().applicationContext.contentResolver
                .registerContentObserver(Settings.System.CONTENT_URI, true, volumeContentObserver!!)
    }

    private fun prepareTcfControls(view: View) {
        val activity = requireActivity()
        demoVideoConsentManager = DemoVideoConsentManager(activity)
        val applyButton = view.findViewById<Button>(R.id.button_apply_tcf_options)
        val optinGroup = view.findViewById<RadioGroup>(R.id.radio_group_optin)
        val tcfGroup = view.findViewById<RadioGroup>(R.id.radio_group_tcf)

        optinGroup.setOnCheckedChangeListener { _, _ ->
            resetConcent(applyButton)
        }
        tcfGroup.setOnCheckedChangeListener { _, _ ->
            resetConcent(applyButton)
        }
        applyButton.setOnClickListener {
            val optin = resolveOptinSelection(view)
            val tcfEnabled = isTcfEnabled(view)
            if (tcfEnabled) {
                demoVideoConsentManager.gatherConsent(activity, 1) { error ->
                    if (error != null) {
                        // Consent not obtained in current session.
                        SensicLogger().logD("CMP::: ${error.errorCode}: ${error.message}")
                    } else {
                        SensicLogger().logD("CMP::: Consent obtained successfully")
                        playVideo(optin, true, applyButton, activity)
                    }
                }
            } else {
                playVideo(optin, false, applyButton, activity)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        demoVideoConsentManager.resetConsentInformation()
    }

    private fun playVideo(optin: Boolean?, tcfEnabled: Boolean, applyButton: Button, activity: Activity) {
        val receivedConsent = getReceivedConsent(activity)

        prepareVideoPlayer()

        agent = S2SAgent(configUrl, mediaId, optin,  Tcf23ConsentParams.build(
            tcfEnabled,
            receivedConsent.gdprApplies,
            receivedConsent.tcString
        ) ,context)

        agent?.setStreamPositionCallback {
            ((player?.currentTime ?: 0.0)*1000).toInt()
        }

        player?.on<PlayerEvent.Playing> {
            agent?.playStreamOnDemand(contentIdDefault, videoURL, getOptions(), null)
        }
        player?.on<PlayerEvent.Paused> {
            agent?.stop()
        }
        player?.on<PlayerEvent.PlaybackFinished> {
            agent?.stop()
        }

        player?.on<PlayerEvent.Muted> {
            agent?.volume("0")
        }

        player?.on<PlayerEvent.Unmuted> {
            agent?.volume(volumeContentObserver?.getScaledCurrentVolume().toString())
        }

        player?.on<PlayerEvent.TimeChanged> {
            if(previousPlaybackSpeed != null && previousPlaybackSpeed != player?.playbackSpeed){
                agent?.stop()
                agent?.playStreamOnDemand(contentIdDefault, videoURL, getOptions(), null)
            }
            previousPlaybackSpeed = player?.playbackSpeed
        }
        // Keep one active extension instance in this demo screen; reopen to retest with new values.
        applyButton.isEnabled = false
    }

    private fun resetConcent(applyButton: Button) {
        applyButton.isEnabled = true
        //Reset concent information to allow re-gathering consent with new values.
        demoVideoConsentManager.resetConsentInformation()
    }

    private fun resolveOptinSelection(view: View): Boolean? {
        return when (view.findViewById<RadioGroup>(R.id.radio_group_optin).checkedRadioButtonId) {
            R.id.radio_optin_false -> false
            R.id.radio_optin_true -> true
            else -> null
        }
    }

    private fun isTcfEnabled(view: View): Boolean {
        return view.findViewById<RadioGroup>(R.id.radio_group_tcf).checkedRadioButtonId == R.id.radio_tcf_on
    }
}