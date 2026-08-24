package com.gfk.s2s.demo.s2s.cmp

import android.content.Context
import com.gfk.s2s.tcf.Tcf23ConsentParams

class ReceivedConsent(
    val tcString: String?,
    val gdprApplies: Tcf23ConsentParams.GdprApplies?
) {
    companion object {
        private val iabTcStringKey = "IABTCF_TCString"
        private val iabGdprApplies = "IABTCF_gdprApplies"
        private val umpInternalPrefs = "__GOOGLE_FUNDING_CHOICE_SDK_INTERNAL__"

        fun getReceivedConsent(context: Context): ReceivedConsent {
            val preferenceFiles = listOf(
                "${context.packageName}_preferences",
                umpInternalPrefs
            )

            for (prefName in preferenceFiles) {
                val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                val tcString = prefs.getString(iabTcStringKey, null)
                val gdpr = prefs.getInt(iabGdprApplies, -1).toString()
                val gdprApplies = when (gdpr) {
                    Tcf23ConsentParams.GdprApplies.GDPR_APPLIES.value -> Tcf23ConsentParams.GdprApplies.GDPR_APPLIES
                    Tcf23ConsentParams.GdprApplies.GDPR_DOES_NOT_APPLY.value -> Tcf23ConsentParams.GdprApplies.GDPR_DOES_NOT_APPLY
                    else -> null
                }

                if (!tcString.isNullOrBlank() || gdprApplies != null) {
                    return ReceivedConsent(tcString, gdprApplies)
                }
            }

            return ReceivedConsent(null, null)
        }
    }
}
