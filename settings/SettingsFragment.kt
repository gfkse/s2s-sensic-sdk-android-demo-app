package com.gfk.s2s.demo.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import com.gfk.s2s.demo.BaseFragment
import com.gfk.s2s.demo.MainActivity
import com.gfk.s2s.demo.s2s.R

class SettingsFragment : BaseFragment() {

    private var optinSwitch: Switch? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.supportActionBar?.title =
            getString(R.string.fragment_title_pixel_request)
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        optinSwitch = view.findViewById<Switch>(R.id.optin_switch)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences: SharedPreferences =
            requireActivity().applicationContext.getSharedPreferences(
                "Settings",
                Context.MODE_PRIVATE
            )
        val editor = preferences.edit()

        optinSwitch?.isChecked = preferences.getBoolean("optin", true)

        optinSwitch?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            editor.putBoolean("optin", isChecked)
            editor.apply()
        }
    }
}