package com.example.gr.model.database.entities;

import androidx.annotation.NonNull;

import com.example.gr.model.data.sample.StressSample;
import com.example.gr.utils.DateTimeUtils;


public abstract class AbstractStressSample extends AbstractTimeSample implements StressSample {
    public int getTypeNum() {
        return Type.UNKNOWN.getNum();
    }

    @Override
    public Type getType() {
        return Type.fromNum(getTypeNum());
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "timestamp=" + DateTimeUtils.formatDateTime(DateTimeUtils.parseTimestampMillis(getTimestamp())) +
                ", stress=" + getStress() +
                ", type=" + getType() +
                ", userId=" + getUserId() +
                ", deviceId=" + getDeviceId() +
                "}";
    }
}
