package com.gfk.s2s.demo.pixel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.gfk.s2s.demo.BaseFragment
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.util.*

class PixelRequestFragment : BaseFragment() {

    private var pixelTextView: TextView? = null
    private var checkImage: ImageView? = null
    private var pixelUrl =
        "https://demo-config.sensic.net/tp?ty=IM&gdpr={GDPR}&gdpr_consent={GDPR_CONSENT_758}&optin=false&m=s2sdemomediaid_ssa_android&c=pixel_native_android&pr=[timestamp]"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_pixel_request)

        val view = inflater.inflate(R.layout.pixel_request_fragment, container, false)
        pixelTextView = view.findViewById(R.id.pixelUrl)
        checkImage = view.findViewById(R.id.checkImage)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pixelUrl = pixelUrl.replace("[timestamp]", Date().time.toString())
        pixelTextView?.text = pixelUrl

        triggerPixelRequest()
    }

    private fun triggerPixelRequest() {
        pixelUrl.httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    result.getException().message?.let { Log.e("GfKlog", it) }
                    showRequestResult(false)
                }
                is Result.Success -> {
                    Log.d("GfKlog", "Successfully fired Pixel Request")
                    showRequestResult(true)

                }
            }
        }
    }

    private fun showRequestResult(isResultSuccessful: Boolean) {
        requireActivity().runOnUiThread {
            checkImage?.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    if (isResultSuccessful) android.R.color.holo_green_light else android.R.color.holo_red_light
                )
            )
        }
    }
}