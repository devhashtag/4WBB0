package com.example.pocketalert;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(History history);

    @Update
    void update(History history);

    @Delete
    void delete(History history);

    @Query("DELETE FROM fall_history_table")
    void deleteAllHistory();

    @Query("SELECT * FROM fall_history_table ORDER BY mUserId DESC")
    LiveData<List<History>> getAllHistory();
}
