package com.example.gr.device.events;

import com.example.gr.device.GenericItem;
import com.example.gr.device.ItemWithDetails;

public class GBDeviceEventUpdateDeviceInfo extends GBDeviceEvent {
    public ItemWithDetails item;

    public GBDeviceEventUpdateDeviceInfo(final ItemWithDetails item) {
        this.item = item;
    }

    public GBDeviceEventUpdateDeviceInfo(final String name, final String details) {
        this(new GenericItem(name, details));
    }
}
