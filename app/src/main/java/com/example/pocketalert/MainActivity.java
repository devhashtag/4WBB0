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

    // Request codes
    public static final int REGISTER_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_DETAILS_ACTIVITY_REQUEST_CODE = 2;

    public static boolean vibrationEnabled = true;
    public SwitchPreference settings = new SwitchPreference();
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // created the recycler view to show the list of all connected users.
        RecyclerView recyclerView = findViewById(R.id.usersRecyclerView);
        final UserListAdapter adapter = new UserListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Update the cached copy of the users in the adapter.
        userViewModel.getAllUsers().observe(this, adapter::setUsers);
    }

    /**
     * When another Activity returns a result, this is where that result gets processed.
     *
     * @param requestCode The request code that was used when the activity was started.
     * @param resultCode  The result code return by the activity.
     * @param data        The Intent data the activity return.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Adds a new user to the database
            String id = data.getStringExtra(RegisterActivity.REGISTER_ACTIVITY_REPLY);
            if (id != null) {
                User user = new User(id);
                userViewModel.insert(user);
            } else {
                Toast.makeText(getApplicationContext(), R.string.data_not_saved, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE) {
            // Device connection canceled
            Toast.makeText(getApplicationContext(), R.string.data_not_saved, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * When the FAB with the plus is clicked, go to the RegisterActivity.
     */
    public void addDevice(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_ACTIVITY_REQUEST_CODE);
    }

    /**
     * When the name of one of the users is pressed, go to the DetailActivity with the details of that user.
     */
    public void viewUser(View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        Button viewButton = (Button) view;
        putExtrasDetails(intent, viewButton.getText().toString());
        startActivity(intent);
    }

    /**
     * Puts the details of the user with the specified ID into the specified Intent.
     *
     * @param intent The intent to add the user's details to in the extras.
     * @param id     The ID of the user who's details should be put in the intent's extras.
     */
    private void putExtrasDetails(Intent intent, String id) {
        User user = userViewModel.getUser(id);
        intent.putExtra("id", id);
        intent.putExtra("name", user.getName());
        intent.putExtra("address", user.getAddress());
        intent.putExtra("phone", user.getPhone());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("birthday", user.getBirthday());
    }

    /**
     * When the delete button next to a user/device is pressed, that device gets removed from the database.
     */
    public void deleteUser(View view) {
        Button deleteButton = (Button) view;
        userViewModel.deleteById(deleteButton.getText().toString());

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