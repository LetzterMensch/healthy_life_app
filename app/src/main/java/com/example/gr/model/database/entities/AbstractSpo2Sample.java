package com.example.gr.model.database.entities;

import androidx.annotation.NonNull;

import com.example.gr.model.data.sample.Spo2Sample;
import com.example.gr.utils.DateTimeUtils;


public abstract class AbstractSpo2Sample extends AbstractTimeSample implements Spo2Sample {
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
                ", spo2=" + getSpo2() +
                ", type=" + getType() +
                ", userId=" + getUserId() +
                ", deviceId=" + getDeviceId() +
                "}";
    }
}
