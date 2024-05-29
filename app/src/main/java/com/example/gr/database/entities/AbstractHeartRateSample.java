package com.example.gr.database.entities;

import androidx.annotation.NonNull;

import com.example.gr.data.sample.HeartRateSample;
import com.example.gr.utils.DateTimeUtils;


public abstract class AbstractHeartRateSample extends AbstractTimeSample implements HeartRateSample {
    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "timestamp=" + DateTimeUtils.formatDateTime(DateTimeUtils.parseTimestampMillis(getTimestamp())) +
                ", hr=" + getHeartRate() +
                ", userId=" + getUserId() +
                ", deviceId=" + getDeviceId() +
                "}";
    }
}
