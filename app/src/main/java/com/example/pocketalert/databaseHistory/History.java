package com.example.pocketalert.databaseHistory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fall_history_table")
public class History {

    @PrimaryKey
    @NonNull
    private Integer mId;

    @NonNull
    private String mDate;

    @NonNull
    private String mUserId;

    @NonNull
    private String mName;

    @NonNull
    private Double mLocationX;

    @NonNull
    private Double mLocationY;

    @NonNull
    private Boolean mOnGoing;

    public History(@NonNull Integer id,@NonNull String date,@NonNull String userId,@NonNull String name,@NonNull Double locationX,@NonNull Double locationY, @NonNull Boolean onGoing){
        this.mId = id;
        this.mDate = date;
        this.mUserId = userId;
        this.mName = name;
        this.mLocationX = locationX;
        this.mLocationY = locationY;
        this.mOnGoing = onGoing;
    }

    // Set for every column

    public void setmId(@NonNull Integer mId) {
        this.mId = mId;
    }

    public void setmDate(@NonNull String mDate) {
        this.mDate = mDate;
    }

    public void setmUserId(@NonNull String mUserId) {
        this.mUserId = mUserId;
    }

    public void setmName(@NonNull String mName) {
        this.mName = mName;
    }

    public void setmLocationX(@NonNull Double mLocationX) {
        this.mLocationX = mLocationX;
    }

    public void setmLocationY(@NonNull Double mLocationY) {
        this.mLocationY = mLocationY;
    }

    public void setmOnGoing(@NonNull Boolean mOnGoing) {
        this.mOnGoing = mOnGoing;
    }

    // Get for every column
    public Integer getId(){
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
    public Double getLocationX(){
        return this.mLocationX;
    }
    public Double getLocationY(){
        return this.mLocationY;
    }
    public Boolean getOnGoing(){
        return this.mOnGoing;
    }

    @Override
    @NonNull
    public String toString() {
        return "User{" +
                "id=" + mId +
                ", name='" + mName + '\'' +
                ", user id='" + mUserId + '\'' +
                ", date='" + mDate + '\'' +
                ", location X='" + mLocationX + '\'' +
                ", location Y='" + mLocationY + '\'' +
                ", on going ='" + mOnGoing + '\'' +
                '}';
    }

}
