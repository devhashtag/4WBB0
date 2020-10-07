package com.example.pocketalert;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

// This class is used to make the SwitchPreferences function and take values
public class SwitchPreference extends PreferenceFragmentCompat {

    // Make variables for the various functions in the settings
    SharedPreferences appSettingPrefs;
    Boolean darkModeEnabled; // Whether Dark Mode is enabled or not

    int Orientation; // The current orientation mode in a string
    SharedPreferences.Editor sharedPrefsEdit; // In order to edit preferences
    SharedPreferences.OnSharedPreferenceChangeListener listener; // This is made to make a listener to the various ways to edit the settings

    // This will be called once the function is created

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Use the preferences in order to make a nice layout
        addPreferencesFromResource(R.xml.root_preferences);
        load_Settings();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void setDarkMode(boolean darkModeSetting) {
        if (darkModeSetting) {
            darkModeEnabled = true;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            darkModeEnabled = false;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void setOrientationMode(String Orientation) {
        // Sets the current Orientation mode under the index of Rotation
        if (getActivity() != null) {
            try {
                switch (Orientation) {
                    case "1":
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        findPreference("Orientation").setSummary("Auto mode");
                        break;
                    case "2":
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        findPreference("Orientation").setSummary("Portrait mode");
                        break;
                    case "3":
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        findPreference("Orientation").setSummary("Landscape");
                        break;
                }
            } catch (NullPointerException e) {
                Log.e("SwitchPreference", e.toString());
            }
        }
    }

    private void load_Settings() {
        // Get the shared preferences
        if (getActivity() != null) {
            appSettingPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            // Set the dark mode according to the set mode
            setDarkMode(appSettingPrefs.getBoolean("darkmode", true));

            //
            setOrientationMode(appSettingPrefs.getString("Orientation", "2"));


            // Add a Listener for the values
            listener = (appSettingPrefs, key) -> {
                Log.d("abcdefg", key);
                switch (key) {
                    // If dark mode is called, the settings will be changed in such a way that the background changes
                    case "darkmode":
                        setDarkMode(appSettingPrefs.getBoolean(key, false));
                        break;
                    case "VibrationSwitchPreference":
                        MainActivity.vibrationEnabled = appSettingPrefs.getBoolean(key, true);
                        break;
                    case "Orientation":
                        Log.d("joehoe", String.valueOf(appSettingPrefs.getString(key, "2")));
                        setOrientationMode(appSettingPrefs.getString("Orientation", "2"));
                        break;
                }
            };
            appSettingPrefs.registerOnSharedPreferenceChangeListener(listener);
        }
    }
}