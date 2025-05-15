package com.gfk.s2s.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.demo.s2s.databinding.DashboardFragmentBinding

class DashboardFragment : BaseFragment() {

    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!

    private var isStreamingVideoVisible = false
    private var isStreamingAudioVisible = false
    private var isContentVisible = false
    private var isOtherVisible = false

    private var isExoplayerImplVisible = false
    private var isExoplayerManualImplVisible = false
    private var isExoplayerExtensionImplVisible = false

    private var isExoplayer3ImplVisible = false
    private var isExoplayer3ManualImplVisible = false
    private var isExoplayer3ExtensionImplVisible = false


    private var isExoplayerAudioImplVisible = false
    private var isExoplayerAudioManualImplVisible = false
    private var isExoplayerAudioExtensionImplVisible = false

    private var isExoplayer3AudioImplVisible = false
    private var isExoplayer3AudioManualImplVisible = false
    private var isExoplayer3AudioExtensionImplVisible = false


    private var isBitmovinImplVisible = false
    private var isBitmoviManualImplVisible = false
    private var isBitmovinExtensionImplVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)

        /******************************Streaming Audio*****************************************/
        binding.streamingAudioGroup.isVisible = isStreamingAudioVisible
        binding.streamingAudioTextView.setOnClickListener{
            isStreamingAudioVisible = !isStreamingAudioVisible
            binding.streamingAudioGroup.isVisible = isStreamingAudioVisible
            if(!isStreamingAudioVisible){
                isStreamingAudioVisible = false
                isExoplayerAudioImplVisible = false
                isExoplayerAudioManualImplVisible = false
                isExoplayerAudioExtensionImplVisible = false

                isExoplayer3AudioImplVisible = false
                isExoplayer3AudioManualImplVisible = false
                isExoplayer3AudioExtensionImplVisible = false


                binding.streamingAudioGroup.isVisible = isStreamingAudioVisible
                binding.exoplayerGroupAudio.isVisible = isExoplayerAudioImplVisible
                binding.exoplayerManualGroupAudio.isVisible = isExoplayerAudioManualImplVisible
                binding.exoplayerExtensionGroupAudio.isVisible = isExoplayerAudioExtensionImplVisible

                binding.exoplayer3GroupAudio.isVisible = isExoplayer3AudioImplVisible
                binding.exoplayer3ManualGroupAudio.isVisible = isExoplayer3AudioManualImplVisible
                binding.exoplayer3ExtensionGroupAudio.isVisible = isExoplayer3AudioExtensionImplVisible


            }
        }

        /******************************Streaming Video*****************************************/
        binding.streamingVideoGroup.isVisible = isStreamingVideoVisible
        binding.streamingVideoTextView.setOnClickListener{
            isStreamingVideoVisible = !isStreamingVideoVisible
            binding.streamingVideoGroup.isVisible = isStreamingVideoVisible
            if(!isStreamingVideoVisible){
                isStreamingVideoVisible = false
                isExoplayerImplVisible = false
                isExoplayerManualImplVisible = false
                isExoplayerExtensionImplVisible = false

                isExoplayer3ImplVisible = false
                isExoplayer3ManualImplVisible = false
                isExoplayer3ExtensionImplVisible = false

                isBitmovinImplVisible = false
                isBitmoviManualImplVisible = false
                isBitmovinExtensionImplVisible = false
                binding.streamingVideoGroup.isVisible = isStreamingVideoVisible
                binding.exoplayerGroup.isVisible = isExoplayerImplVisible
                binding.exoplayerManualGroup.isVisible = isExoplayerManualImplVisible
                binding.exoplayerExtensionGroup.isVisible = isExoplayerExtensionImplVisible

                binding.exoplayer3Group.isVisible = isExoplayer3ImplVisible
                binding.exoplayer3ManualGroup.isVisible = isExoplayer3ManualImplVisible
                binding.exoplayer3ExtensionGroup.isVisible = isExoplayer3ExtensionImplVisible

                binding.bitmovinGroup.isVisible = isBitmovinImplVisible
                binding.bitmovinManualGroup.isVisible = isBitmoviManualImplVisible
                binding.bitmovinExtensionGroup.isVisible = isBitmovinExtensionImplVisible

            }
        }

        /******************************Content*****************************************/

        binding.contentGroup.isVisible = isContentVisible
        binding.contentTextView.setOnClickListener{
            isContentVisible = !isContentVisible
            binding.contentGroup.isVisible = isContentVisible
        }

        /******************************Other*****************************************/

        binding.otherGroup.isVisible = isOtherVisible
        binding.otherTextView.setOnClickListener{
            isOtherVisible = !isOtherVisible
            binding.otherGroup.isVisible = isOtherVisible
        }


        /******************************Exoplayer*****************************************/
        binding.exoplayerVodButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_vodFragment)
        }
        binding.exoplayerLiveButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_liveFragment)
        }
        binding.exoplayerLiveTimeshiftedButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_liveTimeshiftedFragment)
        }
        binding.exoplayerLiveNoSeekButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_LiveNoSeekFragment)
        }
        binding.exoplayerVodImaButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_vodImaFragment)
        }
        binding.exoplayerLiveImaButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_liveImaFragment)
        }
        binding.exoplayerVodExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_vodExtensionFragment)
        }
        binding.exoplayerLiveExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_liveExtensionFragment)
        }
        binding.exoplayerLiveTimeshiftedExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_liveTimeshiftedExtensionFragment)
        }
        binding.exoplayerLiveNoSeekExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_liveNoSeekExtensionFragment)
        }
        binding.exoplayerVodImaExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_vodImaExtensionFragment)
        }
        binding.exoplayerLiveImaExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerAction_dashboardFragment_to_liveImaExtensionFragment)
        }

        binding.exoplayerGroup.isVisible = isExoplayerImplVisible
        binding.exoplayerTextView.setOnClickListener{
            isExoplayerImplVisible = !isExoplayerImplVisible
            binding.exoplayerGroup.isVisible = isExoplayerImplVisible
            if(!isExoplayerImplVisible){
                isExoplayerManualImplVisible = false
                isExoplayerExtensionImplVisible = false
                binding.exoplayerManualGroup.isVisible = isExoplayerManualImplVisible
                binding.exoplayerExtensionGroup.isVisible = isExoplayerExtensionImplVisible
            }
        }

        binding.exoplayerManualGroup.isVisible = isExoplayerManualImplVisible
        binding.exoplayerManualTextView.setOnClickListener{
            isExoplayerManualImplVisible = !isExoplayerManualImplVisible
            binding.exoplayerManualGroup.isVisible = isExoplayerManualImplVisible
        }

        binding.exoplayerExtensionGroup.isVisible = isExoplayerExtensionImplVisible
        binding.exoplayerExtensionTextView.setOnClickListener{
            isExoplayerExtensionImplVisible = !isExoplayerExtensionImplVisible
            binding.exoplayerExtensionGroup.isVisible = isExoplayerExtensionImplVisible
        }


        /******************************Exoplayer3*****************************************/
        binding.exoplayer3VodButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_vodFragment)
        }
        binding.exoplayer3LiveButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_liveFragment)
        }
        binding.exoplayer3LiveTimeshiftedButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_liveTimeshiftedFragment)
        }
        binding.exoplayer3LiveNoSeekButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_LiveNoSeekFragment)
        }
        binding.exoplayer3VodImaButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_vodImaFragment)
        }
        binding.exoplayer3LiveImaButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_liveImaFragment)
        }
        binding.exoplayer3VodExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_vodExtensionFragment)
        }
        binding.exoplayer3LiveExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_liveExtensionFragment)
        }
        binding.exoplayer3LiveTimeshiftedExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_liveTimeshiftedExtensionFragment)
        }
        binding.exoplayer3LiveNoSeekExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_liveNoSeekExtensionFragment)
        }
        binding.exoplayer3VodImaExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_vodImaExtensionFragment)
        }
        binding.exoplayer3LiveImaExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3Action_dashboardFragment_to_liveImaExtensionFragment)
        }

        binding.exoplayer3Group.isVisible = isExoplayer3ImplVisible
        binding.exoplayer3TextView.setOnClickListener{
            isExoplayer3ImplVisible = !isExoplayer3ImplVisible
            binding.exoplayer3Group.isVisible = isExoplayer3ImplVisible
            if(!isExoplayer3ImplVisible){
                isExoplayer3ManualImplVisible = false
                isExoplayer3ExtensionImplVisible = false
                binding.exoplayer3ManualGroup.isVisible = isExoplayer3ManualImplVisible
                binding.exoplayer3ExtensionGroup.isVisible = isExoplayer3ExtensionImplVisible
            }
        }

        binding.exoplayer3ManualGroup.isVisible = isExoplayer3ManualImplVisible
        binding.exoplayer3ManualTextView.setOnClickListener{
            isExoplayer3ManualImplVisible = !isExoplayer3ManualImplVisible
            binding.exoplayer3ManualGroup.isVisible = isExoplayer3ManualImplVisible
        }

        binding.exoplayer3ExtensionGroup.isVisible = isExoplayer3ExtensionImplVisible
        binding.exoplayer3ExtensionTextView.setOnClickListener{
            isExoplayer3ExtensionImplVisible = !isExoplayer3ExtensionImplVisible
            binding.exoplayer3ExtensionGroup.isVisible = isExoplayer3ExtensionImplVisible
        }

        /******************************Exoplayer Audio*****************************************/
        binding.exoplayer3AodManualButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_aodFragment)
        }
        binding.exoplayer3AudioLiveManualButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_audio_liveFragment)
        }

        binding.exoplayer3AudioAodExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_aodFragmentExtension)
        }
        binding.exoplayer3AudioLiveExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayer3ActionDashBoardFragment_to_audio_liveFragmentExtension)
        }

        binding.exoplayer2AudioAodExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_aodFragmentExtension)
        }
        binding.exoplayer2AudioLiveExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.exoplayerActionDashBoardFragment_to_audio_liveFragmentExtension)
        }

        binding.exoplayerGroupAudio.isVisible = isExoplayerAudioImplVisible
        binding.exoplayerTextViewAudio.setOnClickListener{
            isExoplayerAudioImplVisible = !isExoplayerAudioImplVisible
            binding.exoplayerGroupAudio.isVisible = isExoplayerAudioImplVisible
            if(!isExoplayerAudioImplVisible){
                isExoplayerAudioManualImplVisible = false
                isExoplayerAudioExtensionImplVisible = false
                binding.exoplayerManualGroupAudio.isVisible = isExoplayerAudioManualImplVisible
                binding.exoplayerExtensionGroupAudio.isVisible = isExoplayerAudioExtensionImplVisible
            }
        }

        binding.exoplayerManualGroupAudio.isVisible = isExoplayerAudioManualImplVisible

        binding.exoplayerExtensionGroupAudio.isVisible = isExoplayerAudioExtensionImplVisible
        binding.exoplayerExtensionTextViewAudio.setOnClickListener{
            isExoplayerAudioExtensionImplVisible = !isExoplayerAudioExtensionImplVisible
            binding.exoplayerExtensionGroupAudio.isVisible = isExoplayerAudioExtensionImplVisible
        }


        /******************************Exoplayer3*****************************************/


        binding.exoplayer3GroupAudio.isVisible = isExoplayer3AudioImplVisible
        binding.exoplayer3TextViewAudio.setOnClickListener{
            isExoplayer3AudioImplVisible = !isExoplayer3AudioImplVisible
            binding.exoplayer3GroupAudio.isVisible = isExoplayer3AudioImplVisible
            if(!isExoplayer3AudioImplVisible){
                isExoplayer3AudioManualImplVisible = false
                isExoplayer3AudioExtensionImplVisible = false
                binding.exoplayer3ManualGroupAudio.isVisible = isExoplayer3AudioManualImplVisible
                binding.exoplayer3ExtensionGroupAudio.isVisible = isExoplayer3AudioExtensionImplVisible
            }
        }

        binding.exoplayer3ManualGroupAudio.isVisible = isExoplayer3AudioManualImplVisible
        binding.exoplayer3ManualTextViewAudio.setOnClickListener{
            isExoplayer3AudioManualImplVisible = !isExoplayer3AudioManualImplVisible
            binding.exoplayer3ManualGroupAudio.isVisible = isExoplayer3AudioManualImplVisible
        }

        binding.exoplayer3ExtensionGroupAudio.isVisible = isExoplayer3AudioExtensionImplVisible
        binding.exoplayer3ExtensionTextViewAudio.setOnClickListener{
            isExoplayer3AudioExtensionImplVisible = !isExoplayer3AudioExtensionImplVisible
            binding.exoplayer3ExtensionGroupAudio.isVisible = isExoplayer3AudioExtensionImplVisible
        }


        /******************************Bitmovin*****************************************/

        binding.bitmovinVodButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_vodFragment)
        }
        binding.bitmovinLiveButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_liveFragment)
        }
        binding.bitmovinLiveTimeshiftedButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_liveTimeshiftedFragment)
        }
        binding.bitmovinLiveNoSeekButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_LiveNoSeekFragment)
        }
        binding.bitmovinVodImaButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_vodIMAFragment)
        }
        binding.bitmovinLiveImaButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_liveImaFragment)
        }
        binding.bitmovinVodExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_vodExtensionFragment)
        }
        binding.bitmovinLiveExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_liveExtensionFragment)
        }
        binding.bitmovinLiveTimeshiftedExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinActionDashBoardFragment_to_liveTimeshiftedExtensionFragment)
        }
        binding.bitmovinLiveNoSeekExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_liveNoSeekExtensionFragment)
        }
        binding.bitmovinVodImaExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_vodImaExtensionFragment)
        }
        binding.bitmovinLiveImaExtensionButton.setOnClickListener {
            findNavController().navigate(R.id.bitmovinAction_dashboardFragment_to_liveImaExtensionFragment)
        }

        binding.bitmovinGroup.isVisible = isBitmovinImplVisible
        binding.bitmovinTextView.setOnClickListener{
            isBitmovinImplVisible = !isBitmovinImplVisible
            binding.bitmovinGroup.isVisible = isBitmovinImplVisible
            if(!isBitmovinImplVisible){
                isBitmoviManualImplVisible = false
                isBitmovinExtensionImplVisible = false
                binding.bitmovinManualGroup.isVisible = isBitmoviManualImplVisible
                binding.bitmovinExtensionGroup.isVisible = isBitmovinExtensionImplVisible
            }
        }

        binding.bitmovinManualGroup.isVisible = isBitmoviManualImplVisible
        binding.bitmovinManualTextView.setOnClickListener{
            isBitmoviManualImplVisible = !isBitmoviManualImplVisible
            binding.bitmovinManualGroup.isVisible = isBitmoviManualImplVisible
        }

        binding.bitmovinExtensionGroup.isVisible = isBitmovinExtensionImplVisible
        binding.bitmovinExtensionTextView.setOnClickListener{
            isBitmovinExtensionImplVisible = !isBitmovinExtensionImplVisible
            binding.bitmovinExtensionGroup.isVisible = isBitmovinExtensionImplVisible
        }

        /******************************Content buttons*****************************************/

        binding.contentButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_contentActivity)
        }
        binding.webSdkButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_webSdkActivity)
        }
        binding.pixelRequestButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_pixelRequestFragment)
        }
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_settingsFragment)
        }

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as? MainActivity)?.supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as? MainActivity)?.supportActionBar?.title = getString(R.string.app_name)
        (activity as? MainActivity)?.usePictureInPictureByHomeButtonPress = false

    }
}