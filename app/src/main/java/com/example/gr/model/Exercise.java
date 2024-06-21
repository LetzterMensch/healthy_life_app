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
    private float met;
    private String category;
    private int defaultDuration;
    public Exercise(){
        defaultDuration = 60;
    }
    @Ignore
    public Exercise(String name, int defaultDuration) {
        this.name = name;
        this.defaultDuration = defaultDuration;
    }

    public float getMet() {
        return met;
    }

    public void setMet(float met) {
        this.met = met;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }
}
