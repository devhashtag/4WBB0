package com.example.pocketalert.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    private LiveData<List<User>> allUsers;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public List<User> getUser(String device_id) {
        return repository.getUser(device_id);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
