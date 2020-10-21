package com.example.pocketalert.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "devices")
public class Device {
    @NonNull
    @PrimaryKey
    public String Id;
    public String OwnerFirstName;
    public String OwnerLastName;
    public String OwnerAddress;
    public String OwnerZipCode;
    public String DateOfBirth;
}
