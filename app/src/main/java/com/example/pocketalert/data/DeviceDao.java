package com.example.pocketalert.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeviceDao {
    @Insert
    void insert(Device device);

    @Delete
    void delete(Device device);

    @Update
    void update(Device device);

    @Query("SELECT * FROM devices")
    LiveData<List<Device>> getAll();

    @Query("SELECT * FROM devices WHERE id = :deviceId")
    Device getDevice(String deviceId);

    @Query("SELECT id FROM devices")
    List<String> getAllId();
}
