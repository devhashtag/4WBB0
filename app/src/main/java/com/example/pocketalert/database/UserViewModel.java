package com.example.pocketalert.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    private LiveData<List<User>> allUsers;

    private List<String> allUsersID;


    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
        //allUsersID = repository.getAllUsersID();

    }

    /**
     * @return A LiveData<List<User>> of all the users in the database.
     */
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    //public List<String> getAllUsersID() { return allUsersID; }

    /**
     * Adds a new User to the database.
     *
     * @param user The User to be added.
     */
    public void insert(User user) {
        repository.insert(user);
    }

    /**
     * Finds and returns the User with a certain ID.
     *
     * @param id The ID of the User.
     * @return The User with the given ID.
     */
    public User getUser(String id) {
        return repository.getUser(id).get(0);
    }

    public List<String> getAllUsersID() {
        return repository.getUserIDS();
    }
    /**
     * Deletes the specified user from the database.
     *
     * @param user The User to be removed.
     */
    public void delete(User user) {
        repository.delete(user);
    }

    /**
     * Deletes the user with the specified ID from the database.
     *
     * @param id The ID of the user to be removed.
     */
    public void deleteById(String id) {
        delete(getUser(id));
    }

    /**
     * Deletes everyone from the database.
     */
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * Changes the data of the selected user in the database.
     *
     * @param user The user with updated data.
     */
    public void update(User user) {
        repository.update(user);
    }

}
