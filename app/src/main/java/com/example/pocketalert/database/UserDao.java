package com.example.pocketalert.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE id = :device_id")
    List<User> getUser(String device_id);
}