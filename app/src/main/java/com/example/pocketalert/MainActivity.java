package com.example.pocketalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketalert.database.User;
import com.example.pocketalert.database.UserViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final int REGISTER_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_DETAILS_ACTIVITY_REQUEST_CODE = 2;
    public static boolean vibrationEnabled = true;
    public SwitchPreference settings = new SwitchPreference();
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pair the xml with the mainactivity
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.usersRecyclerView);
        final UserListAdapter adapter = new UserListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Update the cached copy of the users in the adapter.
        userViewModel.getAllUsers().observe(this, adapter::setUsers);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Adds a new user to the database
            String id = data.getStringExtra(RegisterActivity.EXTRA_REPLY);
            User user = new User(id == null ? "0" : id);
            userViewModel.insert(user);
        } else if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE) {
            // Device connection canceled
            Toast.makeText(getApplicationContext(), R.string.data_not_saved, Toast.LENGTH_SHORT).show();
        }
    }

    public void addDevice(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_ACTIVITY_REQUEST_CODE);
    }

    public void viewUser(View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        Button viewButton = (Button) view;
        intent.putExtra("userData", compileUserData(viewButton.getText().toString()));
        startActivity(intent);
    }

    private String[] compileUserData(String id) {
        String[] userData = new String[6];

        User user = userViewModel.getUser(id).get(0);
        userData[0] = id;
        userData[1] = user.getName();
        userData[2] = user.getAddress();
        userData[3] = user.getPhone();
        userData[4] = user.getEmail();
        userData[5] = user.getBirthday();

        return userData;
    }

    public void deleteUser(View view) {
        Button deleteButton = (Button) view;
        userViewModel.delete(userViewModel.getUser(deleteButton.getText().toString()).get(0));

    }

    private void Load_setting() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_night = sp.getBoolean("darkmode", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu, menu);
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
        switch (item.getItemId()) {
            case R.id.SettingsActivity:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
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

    public void setDarkMode(boolean darkModeSetting) {
        if (darkModeSetting) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void load_Setting() {
        // Get the shared preferences
        SharedPreferences appSettingPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set the dark mode according to the set mode
        setDarkMode(appSettingPrefs.getBoolean("darkmode", true));

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