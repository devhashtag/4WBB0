package com.example.pocketalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends AppCompatActivity {
    // Make variables for the checkbox of darkmode
    CheckBox darkModeCheckBox;
    SharedPreferences appSettingPrefs;
    Boolean darkModeEnabled;
    SharedPreferences.Editor sharedPrefsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Dark Mode Checkbox + changes
        darkModeCheckBox = findViewById(R.id.darkmodeCheckbox);
        appSettingPrefs = getApplicationContext().getSharedPreferences("AppSettingPrefs",0);
        darkModeEnabled = appSettingPrefs.getBoolean("NightMode",false);
        sharedPrefsEdit  = appSettingPrefs.edit();
        darkModeCheckBox.setChecked(darkModeEnabled);

        darkModeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (darkModeCheckBox.isChecked()){
                    enableDarkMode();
                }
                else{
                    disableDarkMode();
                }
            }
        });
    }
    void enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        sharedPrefsEdit.putBoolean("NightMode",true);
        sharedPrefsEdit.apply();
    }
    void disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPrefsEdit.putBoolean("NightMode",false);
        sharedPrefsEdit.apply();
    }
}