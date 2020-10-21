package com.example.pocketalert.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Future;

public class DeviceRepository {
    private DeviceDao deviceDao;
    private LiveData<List<Device>> devices;

    public DeviceRepository(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
        devices = retrieveDevices();
    }

    public LiveData<List<Device>> getDevices() {
        return devices;
    }

    private LiveData<List<Device>> retrieveDevices() {
        Future<LiveData<List<Device>>> devices =
                DeviceDatabase.dbExecutor.submit(() -> deviceDao.getAll());

        try {
            while (! devices.isDone()) Thread.sleep(1);
            return devices.get();
        } catch (Exception exception) {
            return null;
        }
    }

    Device getById(final String id) {
        Future<Device> device =
                DeviceDatabase.dbExecutor.submit(() -> deviceDao.getDevice(id));

        try {
            while (! device.isDone()) Thread.sleep(1);
            return device.get();
        } catch (Exception exception) {
            return null;
        }
    }

    List<String> getDeviceIds() {
        Future<List<String>> deviceIds =
                DeviceDatabase.dbExecutor.submit(() -> deviceDao.getAllId());

        try {
            while (! deviceIds.isDone()) Thread.sleep(1);
            return deviceIds.get();
        } catch (Exception exception) {
            return null;
        }
    }

    boolean exists(String deviceId) {
        return getById(deviceId) != null;
    }

    void insert(Device device) {
        DeviceDatabase.dbExecutor.execute(() -> deviceDao.insert(device));
    }

    void update(Device device) {
        DeviceDatabase.dbExecutor.execute(() -> deviceDao.update(device));
    }

    void delete(Device device) {
        DeviceDatabase.dbExecutor.execute(() -> deviceDao.delete(device));
    }
}
