
package com.example.gr.device.events;


import com.example.gr.device.GBDevice;

public class GBDeviceEventUpdateDeviceState extends GBDeviceEvent {
    public GBDevice.State state;

    public GBDeviceEventUpdateDeviceState(final GBDevice.State state) {
        this.state = state;
    }
}
