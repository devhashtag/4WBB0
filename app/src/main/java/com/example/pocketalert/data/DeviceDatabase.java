package com.example.pocketalert.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Device.class}, version = 1, exportSchema = false)
public abstract class DeviceDatabase extends RoomDatabase {
    public abstract DeviceDao deviceDao();

    private static volatile DeviceDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService dbExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DeviceDatabase getDatabase(Context context) {
        DeviceDatabase tempInstance = INSTANCE;
        if (tempInstance != null) {
            return tempInstance;
        }

        synchronized (DeviceDatabase.class) {
            DeviceDatabase instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DeviceDatabase.class,
                    "deviceDatabase"
                ).build();
            INSTANCE = instance;
            return instance;
        }
    }
}
