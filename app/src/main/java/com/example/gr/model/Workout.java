package com.example.gr.model;

import android.os.Build;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "workout")

public class Workout implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    private Exercise exercise;
    private int diaryID;
    private int exerciseId;
    private int duration;//minute
    private int caloriesBurnt;
    private String createdAt;
    public Workout(){}
    @Ignore
    public Workout(Exercise exercise, int duration, int weight){
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurnt = (int)exercise.getMet() * (duration/60) * weight;
        this.createdAt = new Date().toString();
    }
    @Ignore
    public Workout(int id, Exercise exercise, int duration, int calories_burnt) {
        this.id = id;
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurnt = calories_burnt;
    }

    public int getDiaryID() {
        return diaryID;
    }

    public void setDiaryID(int diaryID) {
        this.diaryID = diaryID;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public void setCaloriesBurnt(int caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
        this.exerciseId = exercise.getId();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.createdAt = createdAt.toString();
        }
    }
}
