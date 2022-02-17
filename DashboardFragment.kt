package com.gfk.s2s.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.gfk.s2s.demo.s2s.R

class DashboardFragment : BaseFragment() {

    private var isManualImplVisible = false
    private var isExtensionImplVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.dashboard_fragment, container, false)

        view.findViewById<Button>(R.id.vod_button).setOnClickListener {
            findNavController().navigate(R.id.actionDashBoardFragment_to_VODFragment)
        }
        view.findViewById<Button>(R.id.live_button).setOnClickListener {
            findNavController().navigate(R.id.actionDashBoardFragment_to_LiveFragment)
        }
        view.findViewById<Button>(R.id.live_no_seek_button).setOnClickListener {
            findNavController().navigate(R.id.actionDashBoardFragment_to_LiveNoSeekFragment)
        }
        view.findViewById<Button>(R.id.ima_button).setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_imaFragment)
        }
        view.findViewById<Button>(R.id.vod_extension_button).setOnClickListener {
            findNavController().navigate(R.id.actionDashBoardFragment_to_VODExtensionFragment)
        }
        view.findViewById<Button>(R.id.live_extension_button).setOnClickListener {
            findNavController().navigate(R.id.actionDashBoardFragment_to_LiveExtensionFragment)
        }
        view.findViewById<Button>(R.id.live_no_seek_extension_button).setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_liveNoSeekExtensionFragment)
        }
        view.findViewById<Button>(R.id.content_button).setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_contentActivity)
        }
        view.findViewById<Button>(R.id.web_sdk_button).setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_webSdkActivity)
        }
        view.findViewById<Button>(R.id.pixel_request_button).setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_pixelRequestFragment)
        }

        view.findViewById<Button>(R.id.settings_button).setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_settingsFragment)
        }

        view.findViewById<Group>(R.id.manual_group).isVisible = isManualImplVisible
        view.findViewById<TextView>(R.id.manual_text_view).setOnClickListener{
            isManualImplVisible = !isManualImplVisible
            view.findViewById<Group>(R.id.manual_group).isVisible = isManualImplVisible
        }

        view.findViewById<Group>(R.id.extension_group).isVisible = isExtensionImplVisible
        view.findViewById<TextView>(R.id.extension_text_view).setOnClickListener{
            isExtensionImplVisible = !isExtensionImplVisible
            view.findViewById<Group>(R.id.extension_group).isVisible = isExtensionImplVisible
        }

        return view

    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as? MainActivity)?.supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as? MainActivity)?.supportActionBar?.title = getString(R.string.app_name)
        (activity as? MainActivity)?.usePictureInPictureByHomeButtonPress = false

    }
}