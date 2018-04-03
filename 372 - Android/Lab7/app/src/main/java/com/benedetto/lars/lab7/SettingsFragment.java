package com.benedetto.lars.lab7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        onSharedPreferenceChanged(prefs, "stiffness");
        onSharedPreferenceChanged(prefs, "coils");
        onSharedPreferenceChanged(prefs, "displacement");
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        switch (key) {
            case "stiffness":
                String input = sharedPreferences.getString(key, "1.5");
                Float f;
                try {
                    f = Float.valueOf(input);
                    if (f < .5) f = .5f;
                    if (f > 10) f = 10f;
                } catch (NumberFormatException e) {
                    //If they didn't enter a number, reset to default
                    f = 1.5f;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, f.toString());
                editor.commit();
                pref.setSummary(f.toString());
                break;
            case "coils":
                pref.setSummary(Integer.toString(sharedPreferences.getInt(key, 11)));
                break;
            case "displacement":
                pref.setSummary(Integer.toString(sharedPreferences.getInt(key, 0)));
        }
    }
}
