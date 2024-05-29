package com.example.gr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class BaseWorkout {
    private long id;
    private long start;
    private long end;
    private long duration;
    private LocalDateTime localDateTime;
    private int avgHeartRate = -1;
    private int maxHeartRate = -1;
    private int calorie;
    public BaseWorkout(long duration, LocalDateTime localDateTime){
        this.duration = duration;
        this.localDateTime = localDateTime;
    }
    public BaseWorkout(long id, long start, long end, long duration, int avgHeartRate, int maxHeartRate, int calorie) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.avgHeartRate = avgHeartRate;
        this.maxHeartRate = maxHeartRate;
        this.calorie = calorie;
    }

    @JsonIgnore
    public String getDateString() {
        return SimpleDateFormat.getDateTimeInstance().format(new Date(start));
    }
    @JsonIgnore
    public String getSafeDateString() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault()).format(new Date(start));
    }
    @JsonIgnore
    public boolean hasHeartRateData() {
        return avgHeartRate > 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getAvgHeartRate() {
        return avgHeartRate;
    }

    public void setAvgHeartRate(int avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }
}
