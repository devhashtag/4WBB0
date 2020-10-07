package com.example.pocketalert.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    UserRepository(Application application) {
        UserRoomDatabase db = UserRoomDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    void delete(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> userDao.delete(user));
    }

    void deleteAll() {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> userDao.deleteAll());
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> userDao.insert(user));
    }

    List<User> getUser(String id) {
        final ArrayList<User>[] user = new ArrayList[1];
        UserRoomDatabase.databaseWriteExecutor.execute(() -> user[0] = (ArrayList<User>) userDao.getUser(id));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user[0];
    }
}
