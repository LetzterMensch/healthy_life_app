package com.example.gr.model.database.entities;

import androidx.annotation.NonNull;

import com.example.gr.model.data.sample.PaiSample;
import com.example.gr.utils.DateTimeUtils;


public abstract class AbstractPaiSample extends AbstractTimeSample implements PaiSample {
    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "timestamp=" + DateTimeUtils.formatDateTime(DateTimeUtils.parseTimestampMillis(getTimestamp())) +
                ", paiLow=" + getPaiLow() +
                ", paiModerate=" + getPaiModerate() +
                ", paiHigh=" + getPaiHigh() +
                ", timeLow=" + getTimeLow() +
                ", timeModerate=" + getTimeModerate() +
                ", timeHigh=" + getTimeHigh() +
                ", paiToday=" + getPaiToday() +
                ", paiTotal=" + getPaiTotal() +
                ", userId=" + getUserId() +
                ", deviceId=" + getDeviceId() +
                "}";
    }
}
