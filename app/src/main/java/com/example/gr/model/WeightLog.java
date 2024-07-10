package com.example.gr.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.gr.ControllerApplication;
import com.example.gr.model.database.LocalDatabase;

import java.io.Serializable;
@Entity(tableName = "weightlog")
public class WeightLog implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userId;
    private float weight;
    private long timeStamp;
    public WeightLog(){}
    @Ignore
    public WeightLog(String userId, float weight, long timeStamp) {
        this.userId = userId;
        this.weight = weight;
        this.timeStamp = timeStamp;
    }
    public void updateWeightLog(){
        LocalDatabase.getInstance(ControllerApplication.getContext()).weightLogDAO().updateWeightLog(this);
    }
    public long saveWeightLog(){
        return LocalDatabase.getInstance(ControllerApplication.getContext()).weightLogDAO().insertWeightLog(this);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
