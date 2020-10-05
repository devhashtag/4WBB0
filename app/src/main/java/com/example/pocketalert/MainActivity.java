package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketalert.database.User;
import com.example.pocketalert.database.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REGISTER_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_DETAILS_ACTIVITY_REQUEST_CODE = 2;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}