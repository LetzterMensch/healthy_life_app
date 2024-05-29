
package com.example.gr.database.entities;

import androidx.annotation.NonNull;

import com.example.gr.data.sample.SleepRespiratoryRateSample;
import com.example.gr.utils.DateTimeUtils;

public abstract class AbstractSleepRespiratoryRateSample extends AbstractTimeSample implements SleepRespiratoryRateSample {
    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "timestamp=" + DateTimeUtils.formatDateTime(DateTimeUtils.parseTimestampMillis(getTimestamp())) +
                ", rate=" + getRate() +
                ", userId=" + getUserId() +
                ", deviceId=" + getDeviceId() +
                "}";
    }
}
