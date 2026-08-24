package com.gfk.s2s.demo.s2s.cmp

import android.app.Activity
import android.content.Context
import com.gfk.s2s.utils.SensicLogger
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

class DemoVideoConsentManager(context: Context) {

    private val consentInformation: ConsentInformation = UserMessagingPlatform.getConsentInformation(context)
    // Replace with your test device hashed ID
    // You can find your test device hashed ID in the logcat output when running the app on a test device.
    private val TEST_DEVICE_HASHED_ID = "YOUR_TEST_DEVICE_HASHED_ID"


    /** Helper variable to determine if the app can request ads. */
    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()


    /** Helper variable to determine if the privacy options form is required. */
    val isPrivacyOptionsRequired: Boolean
        get() =
            consentInformation.privacyOptionsRequirementStatus ==
                    ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    /**
     * Helper method to call the UMP SDK methods to request consent information and load/show a
     * consent form if necessary.
     */
    fun gatherConsent(
        activity: Activity,
        geography: Int,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener,
    ) {
        // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
//        ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_DISABLED = 0;
//        ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA = 1;
//        ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_REGULATED_US_STATE = 3;
//        ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_OTHER = 4;
        val debugSettings =
            ConsentDebugSettings.Builder(activity)
                .setDebugGeography(geography)
//                .addTestDeviceHashedId(TEST_DEVICE_HASHED_ID)
                .build()

        val params = ConsentRequestParameters.Builder()
            // Keep false for standard GDPR/TCF flow. If true, UMP may not provide a TCF string.
            .setTagForUnderAgeOfConsent(false)
            .setConsentDebugSettings(debugSettings).build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                SensicLogger().logD("Consent information update successful. Consent status: ${consentInformation.consentStatus}")
                showConsentFormIfRequired(activity, onConsentGatheringCompleteListener)
            },
            { requestConsentError ->
                SensicLogger().logError("Consent information update failed: ${requestConsentError.message}")
                onConsentGatheringCompleteListener.consentGatheringComplete(requestConsentError)
            },
        )
    }

    private fun showConsentFormIfRequired(
        activity: Activity,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener,
    ) {
        UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
            onConsentGatheringCompleteListener.consentGatheringComplete(formError)
        }
    }

    /** Helper method to call the UMP SDK method to show the privacy options form. */
    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: OnConsentFormDismissedListener,
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }

    fun resetConsentInformation() {
        SensicLogger().logD("CMP::: Resetting consent information")
        consentInformation.reset()
    }
}