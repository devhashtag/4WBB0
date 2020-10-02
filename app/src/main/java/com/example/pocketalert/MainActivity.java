package com.example.pocketalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {
    public static boolean vibrationEnabled = true;
    Button vibrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pair the xml with the mainactivity
        setContentView(R.layout.activity_main);

        // Find the multiple Buttons
        vibrationButton = findViewById(R.id.button);

        //Initiate the multiple portrait modes
        com.example.pocketalert.ActivityHelper.initialize(this);

        // Vibrate the device if the setting is enabled
        vibrationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (vibrationEnabled) {
                    Vibrator vibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibration.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibration.vibrate(500);
                    }
                }
            }
        });

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
                Toast.makeText(this,"Item 2 Selected",Toast.LENGTH_SHORT);
                return true;
            case R.id.itemThree:
                Toast.makeText(this,"Item 3 Selected",Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}