package com.example.gr.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "weightlog")
public class WeightLog implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private float weight;
    private long timeStamp;
    public WeightLog(){}
    @Ignore
    public WeightLog(int id, int userId, float weight, long timeStamp) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
