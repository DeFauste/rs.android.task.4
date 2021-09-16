package com.example.friend.view.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.example.friend.R
import com.example.friend.view.MainActivity

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var owner: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        return
    }
}