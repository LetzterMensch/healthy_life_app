
package com.example.gr.database.entities;

import androidx.annotation.NonNull;

import com.example.gr.data.sample.TimeSample;
import com.example.gr.utils.DateTimeUtils;


public abstract class AbstractTimeSample implements TimeSample {
    public abstract void setTimestamp(long timestamp);

    public abstract long getUserId();

    public abstract void setUserId(long userId);

    public abstract long getDeviceId();

    public abstract void setDeviceId(long deviceId);

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "timestamp=" + DateTimeUtils.formatDateTime(DateTimeUtils.parseTimestampMillis(getTimestamp())) +
                ", userId=" + getUserId() +
                ", deviceId=" + getDeviceId() +
                "}";
    }
}
