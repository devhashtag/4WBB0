package com.example.pocketalert.databaseHistory;

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

    @Delete
    void delete(History history);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(History history);

    @Query("DELETE FROM fall_history_table")
    void deleteAllHistory();

    @Query("SELECT * FROM fall_history_table")
    LiveData<List<History>> getAllHistory();

    @Query("SELECT * FROM fall_history_table WHERE mId = :id_history")
    List<History>  getHistory(int id_history);
}
