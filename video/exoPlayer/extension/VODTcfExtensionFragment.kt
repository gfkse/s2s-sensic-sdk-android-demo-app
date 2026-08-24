package com.gfk.s2s.demo.s2s.video.exoPlayer.extension

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import com.gfk.s2s.demo.s2s.DemoApplication.Companion.configURL
import com.gfk.s2s.demo.s2s.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.cmp.DemoVideoConsentManager
import com.gfk.s2s.demo.s2s.cmp.ReceivedConsent.Companion.getReceivedConsent
import com.gfk.s2s.demo.s2s.constants.DemoConstants.vdoVideoUrl
import com.gfk.s2s.demo.s2s.video.exoPlayer.BaseVideoFragment
import com.gfk.s2s.exoplayer.ExoplayerExtension
import com.gfk.s2s.s2sExtension.ContentMetadata
import com.gfk.s2s.s2sagent.S2SConfig
import com.gfk.s2s.tcf.Tcf23ConsentParams
import com.gfk.s2s.utils.SensicLogger

class VODTcfExtensionFragment : BaseVideoFragment() {

    override val videoURL = vdoVideoUrl
    private val configUrl = configURL
    private val mediaId = "s2s-exoplayer-android-demo"

    private lateinit var demoVideoConsentManager: DemoVideoConsentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_vod_tcf)
        return inflater.inflate(R.layout.exoplayer_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.tcf_panel)?.visibility = View.VISIBLE

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

            val customParams = HashMap<String, String>()
            customParams["cp1"] = "vod-demo-value-1"
            customParams["cp2"] = "vod-demo-value-2"

            val contentMetadata = ContentMetadata(customParams).apply {
                contentId = "vod-tcf-23-demo-content"
            }

            if (tcfEnabled) {
                demoVideoConsentManager.gatherConsent(activity, 1) { error ->
                    if (error != null) {
                        // Consent not obtained in current session.
                        SensicLogger().logD("CMP::: ${error.errorCode}: ${error.message}")
                    } else {
                        SensicLogger().logD("CMP::: Consent obtained successfully")
                        playVideo(optin, true, contentMetadata, applyButton, activity)
                    }

                    if (demoVideoConsentManager.canRequestAds) {
                        SensicLogger().logD("CMP::: Can request ads")
                    }

                    if (demoVideoConsentManager.isPrivacyOptionsRequired) {
                        // Regenerate the options menu to include a privacy setting.
                        SensicLogger().logD("CMP::: Privacy options required")
                    } else {
                        SensicLogger().logD("CMP::: Privacy options not required")
                    }
                }
            } else {
                playVideo(optin, false, contentMetadata, applyButton, activity)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        demoVideoConsentManager.resetConsentInformation()
    }

    private fun playVideo(optin: Boolean?, tcfEnabled: Boolean, contentMetadata: ContentMetadata, applyButton: Button, activity: Activity) {
        val receivedConsent = getReceivedConsent(activity)
        val config = createConfig(
            optin,
            tcfEnabled,
            receivedConsent.gdprApplies,
            receivedConsent.tcString
        )

        prepareVideoPlayer()

        ExoplayerExtension(
            exoPlayer!!,
            config,
            contentMetadata,
            requireContext(),
            this
        )

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

    private fun createConfig(
        optin: Boolean?,
        tcfEnabled: Boolean,
        gdprApplies: Tcf23ConsentParams.GdprApplies?,
        tcString: String?
    ): S2SConfig {
        val extId = HashMap<String, String>()
        extId["id1"] = "ID-1"
        extId["id2"] = "ID-2"
        return S2SConfig(mediaId, configUrl, optin, extId, Tcf23ConsentParams.build(
            tcfEnabled,
            gdprApplies,
            tcString
        ))
    }
}