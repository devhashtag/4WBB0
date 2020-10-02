package com.example.pocketalert;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

// This class is used to make the SwitchPreferences function and take values
public class SwitchPrefence extends PreferenceFragment {

    // Make variables for the various functions in the settings
    SharedPreferences appSettingPrefs;
    Boolean darkModeEnabled; // Whether Dark Mode is enabled or not
    SharedPreferences.Editor sharedPrefsEdit; // In order to edit preferences
    SharedPreferences.OnSharedPreferenceChangeListener listener; // This is made to make a listener to the various ways to edit the settings

    // This will be called once the function is created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use the preferences in order to make a nice layout
        addPreferencesFromResource(R.xml.root_preferences);

        //  Getting the preferences from the context
        appSettingPrefs = getActivity().getApplicationContext()
                .getSharedPreferences("AppSettingPrefs",0);

        // Update the boolean for dark mode with the default value
        darkModeEnabled = appSettingPrefs.getBoolean("darkmode",false);

        // Sets the current Orientation mode under the index of Rotation
        if(ActivityHelper.OrientationMode==1){
            findPreference("Orientation").setSummary("Current mode is Automatic");
        }
        else if(ActivityHelper.OrientationMode==2){
            findPreference("Orientation").setSummary("Current mode is Portrait");
        }
        else if(ActivityHelper.OrientationMode==3){
            findPreference("Orientation").setSummary("Current mode is Landscape");
        }


        // Make a listener for multiple  ways to change the settings
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences appSettingPrefs, String key) {
                switch (key) {

                    // If dark mode is called, the settings will be changed in such a way that the background changes
                    case "darkmode":
                        if ((appSettingPrefs.getBoolean(key, false))) {
                            darkModeEnabled = true;
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        else{
                            darkModeEnabled = false;
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                        }
                    case "VibrationSwitchPreference":
                        MainActivity.vibrationEnabled = appSettingPrefs.getBoolean(key, true);
                        Toast.makeText(getActivity(),MainActivity.vibrationEnabled+" ",Toast.LENGTH_LONG).show();
                        break;
                    case "Orientation":
                        String orientation = appSettingPrefs.getString(key,"Portrait");
                        if("1".equals(orientation)){
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            findPreference(key).setSummary("Current mode is Automatic");
                            ActivityHelper.OrientationMode = 1;
                        }
                        else if("2".equals(orientation)){
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            findPreference(key).setSummary("Current mode is Portrait");
                            ActivityHelper.OrientationMode = 2;

                        }
                        else if("3".equals(orientation)){
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            findPreference(key).setSummary("Current mode is Landscape");
                            ActivityHelper.OrientationMode = 3;

                        }

                        break;
                }
            }
        };
        appSettingPrefs.registerOnSharedPreferenceChangeListener(listener);
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
}