package com.gfk.s2s.demo.s2s.cmp

import com.google.android.ump.FormError

/** Interface definition for a callback to be invoked when consent gathering is complete. */
fun interface OnConsentGatheringCompleteListener {
    fun consentGatheringComplete(error: FormError?)
}