package com.example.gr.model.database.entities;


import com.example.gr.model.data.sample.ActivitySample;
import com.example.gr.model.data.sample.SampleProvider;
import com.example.gr.utils.DateTimeUtils;

public abstract class AbstractActivitySample implements ActivitySample {
    private SampleProvider mProvider;

    @Override
    public SampleProvider getProvider() {
        return mProvider;
    }

    public void setProvider(SampleProvider provider) {
        mProvider = provider;
    }

    @Override
    public int getKind() {
        return getProvider().normalizeType(getRawKind());
    }

    @Override
    public int getRawKind() {
        return NOT_MEASURED;
    }

    @Override
    public float getIntensity() {
        return getProvider().normalizeIntensity(getRawIntensity());
    }

    public void setRawKind(int kind) {
    }

    public void setRawIntensity(int intensity) {
    }

    public void setSteps(int steps) {
    }

    /**
     * Unix timestamp of the sample, i.e. the number of seconds since 1970-01-01 00:00:00 UTC.
     */
    public abstract void setTimestamp(int timestamp);

    public abstract void setUserId(long userId);

    @Override
    public void setHeartRate(int heartRate) {
    }

    @Override
    public int getHeartRate() {
        return NOT_MEASURED;
    }

    public abstract void setDeviceId(long deviceId);

    public abstract long getDeviceId();

    public abstract long getUserId();

    @Override
    public int getRawIntensity() {
        return NOT_MEASURED;
    }

    @Override
    public int getSteps() {
        return NOT_MEASURED;
    }

    @Override
    public String toString() {
        int kind = getProvider() != null ? getKind() : ActivitySample.NOT_MEASURED;
        float intensity = getProvider() != null ? getIntensity() : ActivitySample.NOT_MEASURED;
        return getClass().getSimpleName() + "{" +
                "timestamp=" + DateTimeUtils.formatDateTime(DateTimeUtils.parseTimeStamp(getTimestamp())) +
                ", intensity=" + intensity +
                ", steps=" + getSteps() +
                ", heartrate=" + getHeartRate() +
                ", type=" + kind +
                ", userId=" + getUserId() +
                ", deviceId=" + getDeviceId() +
                '}';
    }
}
