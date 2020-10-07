package com.example.pocketalert;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fall_history_table")
public class History {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "Date")
    private int mId;

    private String mDate;

    private String mUserId;

    private String mName;

    public History(int id,String date, String userId, String name){
        this.mId = id;
        this.mDate = date;
        this.mUserId = userId;
        this.mName = name;
    }

    // Set for every column

    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    // Get for every column
    public int getId(){
        return this.mId;
    }
    public String getDate(){
        return this.mDate;
    }
    public String getUserId(){
        return this.mUserId;
    }
    public String getName(){
        return this.mName;
    }

}
