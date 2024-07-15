package com.example.gr.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.gr.ControllerApplication;
import com.example.gr.model.database.LocalDatabase;

import java.io.Serializable;

@Entity(tableName = "recordedworkout")
public class RecordedWorkout implements Serializable, WorkoutItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private long baseActivitySummaryId;
    private String createdAt;
    private int activityKind;
    private long endTime;
    private long duration;//
    private int avgHeartRate = -1;
    private int minHeartRate = -1;
    private int maxHeartRate = -1;
    private int caloriesBurnt;
    private double distance;
    private long timestamp;
    private int diaryID;

    public RecordedWorkout(){}
    @Ignore
    public RecordedWorkout(int duration, String createdAt){
        this.duration = duration;
        this.createdAt = createdAt;
    }
    public void saveRecordedWorkout(RecordedWorkout recordedWorkout){
        LocalDatabase.getInstance(ControllerApplication.getContext()).recordedWorkoutDAO().insertRecordedWorkout(recordedWorkout);
    }
    @Ignore
    public RecordedWorkout(String name, long baseActivitySummaryId, String createdAt, long endTime, int duration, int avgHeartRate, int minHeartRate, int maxHeartRate, int caloriesBurnt, int distance) {
        this.name = name;
        this.baseActivitySummaryId = baseActivitySummaryId;
        this.createdAt = createdAt;
        this.endTime = endTime;
        this.duration = duration;
        this.avgHeartRate = avgHeartRate;
        this.minHeartRate = minHeartRate;
        this.maxHeartRate = maxHeartRate;
        this.caloriesBurnt = caloriesBurnt;
        this.distance = distance;
    }

    public int getDiaryID() {
        return diaryID;
    }

    public void setDiaryID(int diaryID) {
        this.diaryID = diaryID;
    }

    public int getActivityKind() {
        return activityKind;
    }

    public void setActivityKind(int activityKind) {
        this.activityKind = activityKind;
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

    public long getBaseActivitySummaryId() {
        return baseActivitySummaryId;
    }

    public void setBaseActivitySummaryId(long baseActivitySummaryId) {
        this.baseActivitySummaryId = baseActivitySummaryId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public long getDurationInMillis() {
        return duration;
    }

    @Override
    public int getWorkoutItemId() {
        return this.id;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getAvgHeartRate() {
        return avgHeartRate;
    }

    public void setAvgHeartRate(int avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
    }

    public int getMinHeartRate() {
        return minHeartRate;
    }

    public void setMinHeartRate(int minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public int getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public void setCaloriesBurnt(int caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int getType() {
        return 2;
    }
}
