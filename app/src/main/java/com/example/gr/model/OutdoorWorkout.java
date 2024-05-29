package com.example.gr.model;

import java.time.LocalDateTime;

public class OutdoorWorkout extends BaseWorkout {
    private int length;
    private double avgSpeed;
    private double topSpeed;
    private double avgPace;
    private float minElevationMSL;
    private float maxElevationMSL;

    private float ascent;

    private float descent;

    public OutdoorWorkout(long duration, LocalDateTime localDateTime){
        super(duration,localDateTime);
    }
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(double topSpeed) {
        this.topSpeed = topSpeed;
    }

    public double getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(double avgPace) {
        this.avgPace = avgPace;
    }

    public float getMinElevationMSL() {
        return minElevationMSL;
    }

    public void setMinElevationMSL(float minElevationMSL) {
        this.minElevationMSL = minElevationMSL;
    }

    public float getMaxElevationMSL() {
        return maxElevationMSL;
    }

    public void setMaxElevationMSL(float maxElevationMSL) {
        this.maxElevationMSL = maxElevationMSL;
    }

    public float getAscent() {
        return ascent;
    }

    public void setAscent(float ascent) {
        this.ascent = ascent;
    }

    public float getDescent() {
        return descent;
    }

    public void setDescent(float descent) {
        this.descent = descent;
    }
}
