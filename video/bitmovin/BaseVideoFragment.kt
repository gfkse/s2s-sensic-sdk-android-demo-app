package com.gfk.s2s.demo.video.bitmovin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.bitmovin.player.PlayerView
import com.bitmovin.player.api.Player
import com.bitmovin.player.api.PlayerConfig
import com.bitmovin.player.api.advertising.AdItem
import com.bitmovin.player.api.advertising.AdSource
import com.bitmovin.player.api.advertising.AdSourceType
import com.bitmovin.player.api.advertising.AdvertisingConfig
import com.bitmovin.player.api.source.SourceConfig
import com.gfk.s2s.demo.BaseFragment
import com.gfk.s2s.demo.s2s.R
import com.gfk.s2s.bitmovinplayer.BitmovinplayerExtension
import com.gfk.s2s.s2sExtension.ContentMetadata
import java.util.*

/**
 * class BaseVideoFragment has the code to show the exoplayer.
 *  The values for videoUrl, configUrl and mediaId are overridden in the
 *  fragments extending from this class.
 */

open class BaseVideoFragment : BaseFragment() {
    private var playerView: PlayerView? = null
    protected var player: Player? = null
    open val videoURL = ""
    private var selectedStreamStartDate: String = ""
    private var selectedStreamStartTime: String = ""
    protected var extension: BitmovinplayerExtension? = null

    protected var adSourcePreRoll: String? = null
    protected var adSourceMidRoll: String? = null
    protected var adSourcePostRoll: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.player_view)
    }

    protected fun prepareVideoPlayer() {
        val playerConfig = playerConfig()
        player = Player.create(this.requireContext(), playerConfig)
        playerView?.player = player
        player?.load(SourceConfig.fromUrl(videoURL))
    }

    protected open fun playerConfig(): PlayerConfig{
        val playerConfig = PlayerConfig()

        if(adSourcePreRoll != null && adSourceMidRoll == null) {
            val firstAdSource = AdSource(AdSourceType.Ima, adSourcePreRoll!!)
            val preRoll = AdItem("pre", firstAdSource)
            playerConfig.advertisingConfig = AdvertisingConfig(preRoll)
        } else if(adSourceMidRoll != null && adSourcePostRoll == null){
            val firstAdSource = AdSource(AdSourceType.Ima, adSourcePreRoll!!)
            val preRoll = AdItem("pre", firstAdSource)
            val secondAdSource = AdSource(AdSourceType.Ima, adSourceMidRoll!!)
            val midRoll = AdItem("10%", secondAdSource)
            playerConfig.advertisingConfig = AdvertisingConfig(preRoll, midRoll)
        } else if(adSourcePreRoll != null && adSourceMidRoll != null && adSourcePostRoll != null){
            val firstAdSource = AdSource(AdSourceType.Ima, adSourcePreRoll!!)
            val preRoll = AdItem("pre", firstAdSource)
            val secondAdSource = AdSource(AdSourceType.Ima, adSourceMidRoll!!)
            val midRoll = AdItem("10%", secondAdSource)
            val thirdAdSource = AdSource(AdSourceType.Ima, adSourcePostRoll!!)
            val postRoll = AdItem("post", thirdAdSource)
            playerConfig.advertisingConfig = AdvertisingConfig(preRoll, midRoll, postRoll)
        } else {
            playerConfig.playbackConfig.isAutoplayEnabled = true
        }

        return playerConfig
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
            activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true
            && activity?.isInPictureInPictureMode == true
        ) {
            return
        }
        player?.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
            activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true
            && activity?.isInPictureInPictureMode == true
        ) {
            return
        }
        player?.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val mediaFrame = view?.findViewById<FrameLayout>(R.id.main_media_frame)
        val params = mediaFrame?.layoutParams as? ConstraintLayout.LayoutParams

        if (newConfig.orientation != Configuration.ORIENTATION_PORTRAIT) {
            params?.height = FrameLayout.LayoutParams.MATCH_PARENT
            params?.dimensionRatio = null
        } else {
            params?.dimensionRatio = "1:0.666"
            params?.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        }
    }


    override fun onStop() {
        super.onStop()
        player?.onStop()
    }

    override fun onDestroy() {
        if (player != null) {
            player?.pause()
            player?.destroy()
        }
        super.onDestroy()
    }

    private fun onStreamStartInputClicked(streamStartInput: EditText, clearStreamStartButton: ImageButton){
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val cHour = c.get(Calendar.HOUR_OF_DAY)
        val cMinute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                val formattedHour = "00".substring(hour.toString().length) + hour.toString()
                val formattedMinute = "00".substring(minute.toString().length) + minute.toString()
                selectedStreamStartTime = "$formattedHour:$formattedMinute"
                streamStartInput.setText(getStreamStart())
                clearStreamStartButton.visibility = View.VISIBLE
                if (extension!=null){
                    val contentMetadata = ContentMetadata(hashMapOf())
                    contentMetadata.streamStart = getStreamStart()
                    extension?.setParameters(contentMetadata)
                }
            },
            cHour,
            cMinute,
            true
        )
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, monthOfYear, dayOfMonth ->
                val monthStr = (""+(monthOfYear+1))
                val formattedMonth = "00".substring(monthStr.length) + monthStr
                val formattedDay = "00".substring(dayOfMonth.toString().length) + dayOfMonth.toString()
                selectedStreamStartDate = "$selectedYear-$formattedMonth-$formattedDay"
                streamStartInput.setText(selectedStreamStartDate)
                timePickerDialog.show()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
    protected fun prepareStreamStartInput(view: View){
        val clearStreamStartButton = view.findViewById<ImageButton>(R.id.clear_stream_start)
        clearStreamStartButton.visibility = View.INVISIBLE

        val streamStartInput = view.findViewById<EditText>(R.id.stream_start_input)
        streamStartInput.isFocusable = false
        streamStartInput.setOnClickListener {
            onStreamStartInputClicked(streamStartInput, clearStreamStartButton)
        }

        clearStreamStartButton.setOnClickListener {
            selectedStreamStartDate = ""
            selectedStreamStartTime = ""
            streamStartInput.setText("")
            clearStreamStartButton.visibility = View.INVISIBLE
            if (extension!=null) {
                val contentMetadata = ContentMetadata(hashMapOf())
                contentMetadata.streamStart = ""
                extension?.setParameters(contentMetadata)
            }
        }
    }
    protected fun getStreamStart(): String {
        return if(selectedStreamStartDate != "" && selectedStreamStartTime != ""){
            selectedStreamStartDate+"T"+selectedStreamStartTime+":00+0100"
        } else {
            ""
        }
    }
}