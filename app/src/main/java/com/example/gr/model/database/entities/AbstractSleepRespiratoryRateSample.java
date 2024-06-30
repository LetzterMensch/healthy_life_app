
package com.example.gr.model.database.entities;

import androidx.annotation.NonNull;

import com.example.gr.model.data.sample.SleepRespiratoryRateSample;
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
