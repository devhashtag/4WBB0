package com.example.pocketalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static boolean vibrationEnabled = true;
    public SwitchPrefence  settings = new SwitchPrefence();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pair the xml with the mainactivity
        setContentView(R.layout.activity_main);

    }
    private void Load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_night = sp.getBoolean("darkmode",false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu,menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void closeApp(View view) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId()) {
            case R.id.SettingsActivity:
                Intent settingsIntent = new Intent(this,SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.itemTwo:
                return true;
            case R.id.itemThree:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setDarkMode(boolean darkModeSetting){
        if (darkModeSetting) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void load_Setting(){
        // Get the shared preferences
        SharedPreferences appSettingPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set the dark mode according to the set mode
        setDarkMode(appSettingPrefs.getBoolean("darkmode",true));

        setOrientationMode(Objects.requireNonNull(appSettingPrefs.getString("Orientation", "2")));
    }

    public void setOrientationMode(String Orientation) {
        // Sets the current Orientation mode under the index of Rotation
        switch (Orientation) {
            case "1":
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                break;
            case "2":
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3":
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    @Override
    protected void onResume() {
        load_Setting();
        super.onResume();
    }
}