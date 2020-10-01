package com.example.pocketalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu,menu);
        return true;
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