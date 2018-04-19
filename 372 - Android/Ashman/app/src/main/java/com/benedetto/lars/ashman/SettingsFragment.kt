package com.benedetto.lars.ashman

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.app.Fragment


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        val prefs = preferenceScreen.sharedPreferences
        onSharedPreferenceChanged(prefs, "startGhosts")
        onSharedPreferenceChanged(prefs, "ghostsPerLevel")
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val pref = findPreference(key)
        pref.summary = Integer.toString(sharedPreferences.getInt(key, 2))
    }
}// Required empty public constructor

