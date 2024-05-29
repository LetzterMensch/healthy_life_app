
package com.example.gr.database.entities;

import androidx.annotation.NonNull;

import com.example.gr.data.sample.TemperatureSample;
import com.example.gr.utils.DateTimeUtils;


public abstract class AbstractTemperatureSample extends AbstractTimeSample implements TemperatureSample {
    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "timestamp=" + DateTimeUtils.formatDateTime(DateTimeUtils.parseTimestampMillis(getTimestamp())) +
                ", temperature=" + getTemperature() +
                ", temperatureType=" + getTemperatureType() +
                ", userId=" + getUserId() +
                ", deviceId=" + getDeviceId() +
                "}";
    }
}
