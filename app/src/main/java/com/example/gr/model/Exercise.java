package com.example.gr.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "exercise")
public class Exercise implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int caloriesBurntPerMin;
    private int defaultDuration;
    private int duration;
    private int caloriesBurntCount;
    public Exercise(){}
    @Ignore
    public Exercise(String name, int caloriesBurntPerMin, int defaultDuration) {
        this.name = name;
        this.caloriesBurntPerMin = caloriesBurntPerMin;
        this.defaultDuration = defaultDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCaloriesBurntCount() {
        return caloriesBurntCount;
    }

    public void setCaloriesBurntCount(int caloriesBurntCount) {
        this.caloriesBurntCount = caloriesBurntCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCaloriesBurntPerMin() {
        return caloriesBurntPerMin;
    }

    public void setCaloriesBurntPerMin(int caloriesBurntPerMin) {
        this.caloriesBurntPerMin = caloriesBurntPerMin;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }
}
