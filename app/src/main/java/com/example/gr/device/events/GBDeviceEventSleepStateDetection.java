
package com.example.gr.device.events;

import com.example.gr.device.model.SleepState;

import java.util.Locale;


public class GBDeviceEventSleepStateDetection extends GBDeviceEvent {
    public SleepState sleepState;

    @Override
    public String toString() {
        return super.toString() + String.format(Locale.ROOT, "sleepState=%s", sleepState);
    }
}
