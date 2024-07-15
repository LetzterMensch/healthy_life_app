package com.example.gr.model;

import android.os.Build;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.gr.utils.DateTimeUtils;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "workout")

public class Workout implements Serializable,WorkoutItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    private Exercise exercise;
    private int diaryID;
    private int exerciseId;
    private int duration;//minute
    private int caloriesBurnt;
    private String createdAt;
    private long timestamp;
    public Workout(){}
    @Ignore
    public Workout(Exercise exercise, int duration, int weight){
        this.exercise = exercise;
        this.exerciseId = exercise.getId();
        this.duration = duration;
        this.caloriesBurnt = (int)exercise.getMet() * (duration/60) * weight;
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.createdAt = DateTimeUtils.formatDate(new Date((long)this.timestamp));
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
    public int getDuration(){
        return duration;
    }
    @Override
    public long getDurationInMillis() {
        return (long) duration * 60 * 1000;
    }

    @Override
    public int getWorkoutItemId() {
        return id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public int getType() {
        return 1;
    }
}
