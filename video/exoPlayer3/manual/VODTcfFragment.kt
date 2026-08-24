package com.gfk.s2s.demo.s2s.video.exoPlayer3.manual

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
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import com.gfk.s2s.demo.s2s.DemoApplication.Companion.configURL
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.VolumeContentObserver
import com.gfk.s2s.demo.s2s.cmp.DemoVideoConsentManager
import com.gfk.s2s.demo.s2s.cmp.ReceivedConsent.Companion.getReceivedConsent
import com.gfk.s2s.demo.s2s.constants.DemoConstants.vdoVideoUrl
import com.gfk.s2s.demo.s2s.video.exoPlayer3.BaseVideoFragment
import com.gfk.s2s.s2sagent.S2SAgent
import com.gfk.s2s.tcf.Tcf23ConsentParams
import com.gfk.s2s.utils.SensicLogger


class VODTcfFragment : BaseVideoFragment() {
    override val videoURL = vdoVideoUrl
    private val configUrl = configURL
    private val mediaId = "s2s-exoplayer3-android-demo"
    private val contentIdDefault = "default"
    private var volumeContentObserver: VolumeContentObserver? = null
    private var agent: S2SAgent? = null
    private lateinit var demoVideoConsentManager: DemoVideoConsentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_vod_tcf)
        return inflater.inflate(R.layout.exoplayer3_video_fragment, container, false)
    }

    var soughtOldPosition: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.tcf_panel)?.visibility = View.VISIBLE
        addVolumeObserver()
        prepareTcfControls(view)
    }

    override fun onStop() {
        super.onStop()
        agent?.flushEventStorage()
        volumeContentObserver?.let {
            requireActivity().contentResolver
                .unregisterContentObserver(it)
        }
    }

    //Please scale your volume between [0,100] as maxVolume is 100
    private fun getOptions() = mapOf(
        "volume" to volumeContentObserver?.getScaledCurrentVolume().toString(),
        "speed" to (exoPlayer?.playbackParameters?.speed?.toString() ?: "1.0")
    )

    private fun addVolumeObserver() {
        volumeContentObserver =
            object : VolumeContentObserver(requireContext(), Handler(Looper.getMainLooper())) {
                //This function will scale current volume between [0,100]
                override fun volumeChanged(currentVolume: Int) {
                    agent?.volume(currentVolume.toString())
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

        agent = S2SAgent(configUrl, mediaId, optin, Tcf23ConsentParams.build(
            tcfEnabled,
            receivedConsent.gdprApplies,
            receivedConsent.tcString
        ) ,context)

        agent?.setStreamPositionCallback {
            soughtOldPosition ?: (exoPlayer?.currentPosition ?: 0).toInt()
        }

        exoPlayer?.addListener(object : Player.Listener {

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                soughtOldPosition = oldPosition.positionMs.toInt()

            }


            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    soughtOldPosition = null
                    agent?.playStreamOnDemand(contentIdDefault, videoURL, getOptions(), null)
                } else {
                    agent?.stop()
                }
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                if (exoPlayer?.isPlaying == true) {
                    agent?.stop()
                    agent?.playStreamOnDemand(contentIdDefault, videoURL, getOptions(), null)
                }
            }
        })
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