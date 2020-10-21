package com.example.pocketalert.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {
    private DeviceRepository repository;

    public DeviceViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceRepository(
                DeviceDatabase.getDatabase(application).deviceDao()
        );
    }


    public LiveData<List<Device>> getAll() {
        return repository.getDevices();
    }

    public List<String> getDeviceIds() {
        return repository.getDeviceIds();
    }

    public Device getDevice(String deviceId) {
        return repository.getById(deviceId);
    }

    public boolean exists(String deviceId) {return repository.exists(deviceId); }

    public void insert(Device device) {
        repository.insert(device);
    }

    public void update(Device device) {
        repository.update(device);
    }

    public void delete(Device device) {
        repository.delete(device);
    }
}
