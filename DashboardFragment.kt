package com.gfk.s2s.demo

import android.annotation.SuppressLint
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

    private var isStreamingVisible = false
    private var isContentVisible = false
    private var isOtherVisible = false

    private var isExoplayerImplVisible = false
    private var isExoplayerManualImplVisible = false
    private var isExoplayerExtensionImplVisible = false

    private var isExoplayer3ImplVisible = false
    private var isExoplayer3ManualImplVisible = false
    private var isExoplayer3ExtensionImplVisible = false

    private var isBitmovinImplVisible = false
    private var isBitmoviManualImplVisible = false
    private var isBitmovinExtensionImplVisible = false

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.dashboard_fragment, container, false)

        /******************************Streaming*****************************************/
        view.findViewById<Group>(R.id.streaming_group).isVisible = isStreamingVisible
        view.findViewById<TextView>(R.id.streaming_text_view).setOnClickListener{
            isStreamingVisible = !isStreamingVisible
            view.findViewById<Group>(R.id.streaming_group).isVisible = isStreamingVisible
            if(!isStreamingVisible){
                isStreamingVisible = false
                isExoplayerImplVisible = false
                isExoplayerManualImplVisible = false
                isExoplayerExtensionImplVisible = false

                isExoplayer3ImplVisible = false
                isExoplayer3ManualImplVisible = false
                isExoplayer3ExtensionImplVisible = false

                isBitmovinImplVisible = false
                isBitmoviManualImplVisible = false
                isBitmovinExtensionImplVisible = false
                view.findViewById<Group>(R.id.streaming_group).isVisible = isStreamingVisible
                view.findViewById<Group>(R.id.exoplayer_group).isVisible = isExoplayerImplVisible
                view.findViewById<Group>(R.id.exoplayer_manual_group).isVisible = isExoplayerManualImplVisible
                view.findViewById<Group>(R.id.exoplayer_extension_group).isVisible = isExoplayerExtensionImplVisible

                view.findViewById<Group>(R.id.exoplayer3_group).isVisible = isExoplayer3ImplVisible
                view.findViewById<Group>(R.id.exoplayer3_manual_group).isVisible = isExoplayer3ManualImplVisible
                view.findViewById<Group>(R.id.exoplayer3_extension_group).isVisible = isExoplayer3ExtensionImplVisible

                view.findViewById<Group>(R.id.bitmovin_group).isVisible = isBitmovinImplVisible
                view.findViewById<Group>(R.id.bitmovin_manual_group).isVisible = isBitmoviManualImplVisible
                view.findViewById<Group>(R.id.bitmovin_extension_group).isVisible = isBitmovinExtensionImplVisible

            }
        }

        /******************************Content*****************************************/

        view.findViewById<Group>(R.id.content_group).isVisible = isContentVisible
        view.findViewById<TextView>(R.id.content_text_view).setOnClickListener{
            isContentVisible = !isContentVisible
            view.findViewById<Group>(R.id.content_group).isVisible = isContentVisible
        }

        /******************************Other*****************************************/

        view.findViewById<Group>(R.id.other_group).isVisible = isOtherVisible
        view.findViewById<TextView>(R.id.other_text_view).setOnClickListener{
            isOtherVisible = !isOtherVisible
            view.findViewById<Group>(R.id.other_group).isVisible = isOtherVisible
        }


        /******************************Exoplayer*****************************************/
        view.findViewById<Button>(R.id.exoplayer_vod_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_vodFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_live_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_liveFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_live_timeshifted_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_liveTimeshiftedFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_live_no_seek_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_LiveNoSeekFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_vod_ima_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_vodImaFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_live_ima_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_liveImaFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_vod_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_vodExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_live_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_liveExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_live_timeshifted_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_liveTimeshiftedExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_live_no_seek_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_liveNoSeekExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_vod_ima_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_vodImaExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer_live_ima_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_liveImaExtensionFragment)
        }

        view.findViewById<Group>(R.id.exoplayer_group).isVisible = isExoplayerImplVisible
        view.findViewById<TextView>(R.id.exoplayer_text_view).setOnClickListener{
            isExoplayerImplVisible = !isExoplayerImplVisible
            view.findViewById<Group>(R.id.exoplayer_group).isVisible = isExoplayerImplVisible
            if(!isExoplayerImplVisible){
                isExoplayerManualImplVisible = false
                isExoplayerExtensionImplVisible = false
                view.findViewById<Group>(R.id.exoplayer_manual_group).isVisible = isExoplayerManualImplVisible
                view.findViewById<Group>(R.id.exoplayer_extension_group).isVisible = isExoplayerExtensionImplVisible
            }
        }

        view.findViewById<Group>(R.id.exoplayer_manual_group).isVisible = isExoplayerManualImplVisible
        view.findViewById<TextView>(R.id.exoplayer_manual_text_view).setOnClickListener{
            isExoplayerManualImplVisible = !isExoplayerManualImplVisible
            view.findViewById<Group>(R.id.exoplayer_manual_group).isVisible = isExoplayerManualImplVisible
        }

        view.findViewById<Group>(R.id.exoplayer_extension_group).isVisible = isExoplayerExtensionImplVisible
        view.findViewById<TextView>(R.id.exoplayer_extension_text_view).setOnClickListener{
            isExoplayerExtensionImplVisible = !isExoplayerExtensionImplVisible
            view.findViewById<Group>(R.id.exoplayer_extension_group).isVisible = isExoplayerExtensionImplVisible
        }

        /******************************Exoplayer3*****************************************/
        view.findViewById<Button>(R.id.exoplayer3_vod_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_vodFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_live_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_liveFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_live_timeshifted_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_liveTimeshiftedFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_live_no_seek_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_LiveNoSeekFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_vod_ima_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_vodImaFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_live_ima_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_liveImaFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_vod_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_vodExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_live_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_liveExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_live_timeshifted_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_liveTimeshiftedExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_live_no_seek_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_liveNoSeekExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_vod_ima_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_vodImaExtensionFragment)
        }
        view.findViewById<Button>(R.id.exoplayer3_live_ima_extension_button).setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_liveImaExtensionFragment)
        }

        view.findViewById<Group>(R.id.exoplayer3_group).isVisible = isExoplayer3ImplVisible
        view.findViewById<TextView>(R.id.exoplayer3_text_view).setOnClickListener{
            isExoplayer3ImplVisible = !isExoplayer3ImplVisible
            view.findViewById<Group>(R.id.exoplayer3_group).isVisible = isExoplayer3ImplVisible
            if(!isExoplayer3ImplVisible){
                isExoplayer3ManualImplVisible = false
                isExoplayer3ExtensionImplVisible = false
                view.findViewById<Group>(R.id.exoplayer3_manual_group).isVisible = isExoplayer3ManualImplVisible
                view.findViewById<Group>(R.id.exoplayer3_extension_group).isVisible = isExoplayer3ExtensionImplVisible
            }
        }

        view.findViewById<Group>(R.id.exoplayer3_manual_group).isVisible = isExoplayer3ManualImplVisible
        view.findViewById<TextView>(R.id.exoplayer3_manual_text_view).setOnClickListener{
            isExoplayer3ManualImplVisible = !isExoplayer3ManualImplVisible
            view.findViewById<Group>(R.id.exoplayer3_manual_group).isVisible = isExoplayer3ManualImplVisible
        }

        view.findViewById<Group>(R.id.exoplayer3_extension_group).isVisible = isExoplayer3ExtensionImplVisible
        view.findViewById<TextView>(R.id.exoplayer3_extension_text_view).setOnClickListener{
            isExoplayer3ExtensionImplVisible = !isExoplayer3ExtensionImplVisible
            view.findViewById<Group>(R.id.exoplayer3_extension_group).isVisible = isExoplayer3ExtensionImplVisible
        }

        /******************************Bitmovin*****************************************/

        view.findViewById<Button>(R.id.bitmovin_vod_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_vodFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_live_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_liveFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_live_timeshifted_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_liveTimeshiftedFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_live_no_seek_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_LiveNoSeekFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_vod_ima_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_vodIMAFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_live_ima_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_liveImaFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_vod_extension_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_vodExtensionFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_live_extension_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_liveExtensionFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_live_timeshifted_extension_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_liveTimeshiftedExtensionFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_live_no_seek_extension_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_liveNoSeekExtensionFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_vod_ima_extension_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_vodImaExtensionFragment)
        }
        view.findViewById<Button>(R.id.bitmovin_live_ima_extension_button).setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_liveImaExtensionFragment)
        }

        view.findViewById<Group>(R.id.bitmovin_group).isVisible = isBitmovinImplVisible
        view.findViewById<TextView>(R.id.bitmovin_text_view).setOnClickListener{
            isBitmovinImplVisible = !isBitmovinImplVisible
            view.findViewById<Group>(R.id.bitmovin_group).isVisible = isBitmovinImplVisible
            if(!isBitmovinImplVisible){
                isBitmoviManualImplVisible = false
                isBitmovinExtensionImplVisible = false
                view.findViewById<Group>(R.id.bitmovin_manual_group).isVisible = isBitmoviManualImplVisible
                view.findViewById<Group>(R.id.bitmovin_extension_group).isVisible = isBitmovinExtensionImplVisible
            }
        }

        view.findViewById<Group>(R.id.bitmovin_manual_group).isVisible = isBitmoviManualImplVisible
        view.findViewById<TextView>(R.id.bitmovin_manual_text_view).setOnClickListener{
            isBitmoviManualImplVisible = !isBitmoviManualImplVisible
            view.findViewById<Group>(R.id.bitmovin_manual_group).isVisible = isBitmoviManualImplVisible
        }

        view.findViewById<Group>(R.id.bitmovin_extension_group).isVisible = isBitmovinExtensionImplVisible
        view.findViewById<TextView>(R.id.bitmovin_extension_text_view).setOnClickListener{
            isBitmovinExtensionImplVisible = !isBitmovinExtensionImplVisible
            view.findViewById<Group>(R.id.bitmovin_extension_group).isVisible = isBitmovinExtensionImplVisible
        }

        /******************************Content buttons*****************************************/

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