package com.example.gr.model;

public class RecordedWorkout extends Workout {
    private int id;
    private String name;
    private long baseActivitySummaryId;
    private String createdAt;
    private String endTime;
    private int duration;
    private int avgHeartRate = -1;
    private int minHeartRate = -1;
    private int maxHeartRate = -1;
    private int caloriesBurnt;
    private int distance;
    public RecordedWorkout(int duration, String createdAt){
        this.duration = duration;
        this.createdAt = createdAt;
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

    public String getStartTime() {
        return createdAt;
    }

    public void setStartTime(String startTime) {
        this.createdAt = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
